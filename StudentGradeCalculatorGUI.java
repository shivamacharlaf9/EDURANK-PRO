import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;

class Student implements Serializable {
    String name;
    int rollNo;
    int[] marks = new int[5]; // Assuming 5 subjects
    int totalMarks = 0;
    double percentage;
    char grade;

    // Method to calculate percentage
    void calculatePercentage() {
        percentage = (double) totalMarks / 5; // Assuming each subject is out of 100
    }

    // Method to assign grade based on percentage
    void assignGrade() {
        if (percentage >= 90) {
            grade = 'A';
        } else if (percentage >= 80) {
            grade = 'B';
        } else if (percentage >= 70) {
            grade = 'C';
        } else if (percentage >= 60) {
            grade = 'D';
        } else {
            grade = 'F';
        }
    }

    // Method to display student details
    String displayResults() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student Name: ").append(name).append("\n")
          .append("Roll Number: ").append(rollNo).append("\n");
        for (int i = 0; i < 5; i++) {
            sb.append("Marks for subject ").append(i + 1).append(": ").append(marks[i]).append("\n");
        }
        sb.append("Total Marks: ").append(totalMarks).append("/500\n")
          .append("Percentage: ").append(percentage).append("%\n")
          .append("Grade: ").append(grade).append("\n");
        return sb.toString();
    }
}

public class StudentGradeCalculatorGUI extends JFrame {
    private final JTextField nameField, rollNoField;
    private final JTextField[] marksFields = new JTextField[5];
    private final JTextArea resultArea;
    private final Student[] students;
    private int currentIndex = 0;
    private final int numStudents;

    public StudentGradeCalculatorGUI() {
        setTitle("Student Grade Calculator");
        setSize(500, 600); // Adjusted size for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Name input
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Roll number input
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Roll Number:"), gbc);
        gbc.gridx = 1;
        rollNoField = new JTextField(20);
        add(rollNoField, gbc);

        // Marks input for 5 subjects
        for (int i = 0; i < 5; i++) {
            gbc.gridx = 0;
            gbc.gridy = 2 + i;
            add(new JLabel("Marks for Subject " + (i + 1) + ":"), gbc);
            gbc.gridx = 1;
            marksFields[i] = new JTextField(5);
            add(marksFields[i], gbc);
        }

        // Buttons for submitting data
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        buttonPanel.add(submitButton);
        JButton rankButton = new JButton("Rank Students");
        buttonPanel.add(rankButton);
        add(buttonPanel, gbc);

        // Result display area
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, gbc);

        // Action listener for the Submit button
        submitButton.addActionListener(e -> addStudentData());

        // Action listener for the Rank Students button
        rankButton.addActionListener(e -> displayRankedStudents());

        // Initialize student array based on number of students input
        numStudents = Integer.parseInt(JOptionPane.showInputDialog("Enter number of students: "));
        students = new Student[numStudents];
    }

    // Method to add student data to array
    private void addStudentData() {
        if (currentIndex < numStudents) {
            try {
                Student student = new Student();
                student.name = nameField.getText();
                student.rollNo = Integer.parseInt(rollNoField.getText());
                student.marks = new int[5];
                student.totalMarks = 0; // Reset totalMarks for each student

                // Input and validate marks
                for (int i = 0; i < 5; i++) {
                    int marks = Integer.parseInt(marksFields[i].getText());
                    if (marks < 0 || marks > 100) {
                        throw new IllegalArgumentException("Marks must be between 0 and 100");
                    }
                    student.marks[i] = marks;
                    student.totalMarks += marks;
                }

                student.calculatePercentage();
                student.assignGrade();
                students[currentIndex] = student;

                // Clear input fields for next entry
                nameField.setText("");
                rollNoField.setText("");
                for (JTextField field : marksFields) {
                    field.setText("");
                }

                // Display student data in result area
                resultArea.append("Student " + (currentIndex + 1) + " Added:\n" + student.displayResults() + "\n");

                currentIndex++;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "All student data has been entered.");
        }
    }

    // Method to rank students and display results
    private void displayRankedStudents() {
        if (currentIndex == numStudents) {
            Arrays.sort(students, (s1, s2) -> s2.totalMarks - s1.totalMarks); // Sort in descending order
            resultArea.append("\nClass Rankings:\n");
            for (int i = 0; i < students.length; i++) {
                resultArea.append((i + 1) + ". " + students[i].name + " - Total Marks: " + students[i].totalMarks + " Grade: " + students[i].grade + "\n");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter all student data first.");
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeCalculatorGUI frame = new StudentGradeCalculatorGUI();
            frame.setVisible(true);
        });
    }
}
