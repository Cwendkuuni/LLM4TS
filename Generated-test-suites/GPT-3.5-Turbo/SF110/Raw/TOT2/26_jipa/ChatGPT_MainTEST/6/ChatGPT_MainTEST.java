import org.junit.Test;
import static org.junit.Assert.*;

public class ChatGPT_MainTest {

    @Test
    public void testProcessInstruction_halt() {
        // Test the "halt" opcode
        String instruction = "halt";
        int iPtr = 0;
        Main.instruction[iPtr] = instruction;
        processInstruction(instruction);
        assertEquals(0, iPtr);
    }

    @Test
    public void testProcessInstruction_out() {
        // Test the "out" opcode
        String operand = "hello";
        String instruction = "out " + operand;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        processInstruction(instruction);
        assertEquals(operand + "\n", outContent.toString());
    }

    @Test
    public void testProcessInstruction_in() {
        // Test the "in" opcode
        String operand = "var1";
        String input = "123";
        String instruction = "in " + operand;
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        processInstruction(instruction);
        assertEquals(input, Variable.getVariableValue(operand));
    }

    @Test
    public void testProcessInstruction_mov() {
        // Test the "mov" opcode
        String operand1 = "var1";
        String operand2 = "var2";
        Variable.setVariableValue(operand1, 123);
        String instruction = "mov " + operand1 + "," + operand2;
        processInstruction(instruction);
        assertEquals(0, Variable.getVariableValue(operand1));
        assertEquals(123, Variable.getVariableValue(operand2));
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testGetFileName() {
        // test when user inputs a file name
        String expected = "testfile.txt";
        System.setIn(new ByteArrayInputStream(expected.getBytes()));
        String actual = Main.getFileName();
        assertEquals(expected, actual);

        // test when user inputs q to quit
        expected = "q";
        System.setIn(new ByteArrayInputStream(expected.getBytes()));
        actual = Main.getFileName();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveComment() {
        String input = "test ; comment";
        String expected = "test";
        String actual = Main.removeComment(input);
        assertEquals(expected, actual);
    }

    // More tests for other utility methods can be added here

}