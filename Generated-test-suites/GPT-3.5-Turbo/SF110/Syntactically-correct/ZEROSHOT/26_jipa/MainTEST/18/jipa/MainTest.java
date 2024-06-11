import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Test case 1
        String instruction1 = "add, x, 5";
        Main.processInstruction(instruction1);
        assertEquals(5, Variable.getVariableValue("x"));

        // Test case 2
        String instruction2 = "mov, x, y";
        Variable.setVariableValue("y", 10);
        Main.processInstruction(instruction2);
        assertEquals(10, Variable.getVariableValue("x"));
        assertEquals(0, Variable.getVariableValue("y"));

        // Test case 3
        String instruction3 = "set, x, 20";
        Main.processInstruction(instruction3);
        assertEquals(20, Variable.getVariableValue("x"));

        // Test case 4
        String instruction4 = "del, x";
        Variable.addVariable("x");
        Main.processInstruction(instruction4);
        assertFalse(Variable.variables.containsKey("x"));

        // Test case 5
        String instruction5 = "cmp, x, y, z";
        Variable.setVariableValue("x", 10);
        Variable.setVariableValue("y", 10);
        Main.processInstruction(instruction5);
        assertEquals(1, Variable.getVariableValue("z"));
    }

    @Test
    public void testLoadInstructions() {
        // Test case 1
        String fileName1 = "testfile1.txt";
        String[] expectedInstructions1 = {"add, x, 5", "mov, x, y"};
        assertTrue(Main.loadInstructions(fileName1));
        assertArrayEquals(expectedInstructions1, Main.instruction);

        // Test case 2
        String fileName2 = "";
        String[] expectedInstructions2 = {"set, x, 10", "mul, x, 2"};
        assertTrue(Main.loadInstructions(fileName2));
        assertArrayEquals(expectedInstructions2, Main.instruction);
    }

    @Test
    public void testRemoveComment() {
        // Test case 1
        String line1 = "add, x, 5 ; Adding 5 to x";
        String expectedLine1 = "add, x, 5";
        assertEquals(expectedLine1, Main.removeComment(line1));

        // Test case 2
        String line2 = "mul, x, y"; // Multiply x by y
        String expectedLine2 = "mul, x, y";
        assertEquals(expectedLine2, Main.removeComment(line2));
    }

    @Test
    public void testSplitArguments() {
        // Test case 1
        String original1 = "add, x, y";
        String[] expectedArguments1 = {"add", "x", "y"};
        assertArrayEquals(expectedArguments1, Main.splitArguments(original1));

        // Test case 2
        String original2 = "set, x, 10, 20, 30";
        String[] expectedArguments2 = {"set", "x", "10, 20, 30"};
        assertArrayEquals(expectedArguments2, Main.splitArguments(original2));
    }

    @Test
    public void testGetValue() {
        // Test case 1
        String operand1 = "10";
        assertEquals(10, Main.getValue(operand1));

        // Test case 2
        String operand2 = "x";
        Variable.setVariableValue("x", 15);
        assertEquals(15, Main.getValue(operand2));
    }

    @Test
    public void testGetJumpValue() {
        // Test case 1
        String operand1 = "10";
        assertEquals(9, Main.getJumpValue(operand1));

        // Test case 2
        String operand2 = "label1";
        Label.labels.put("label1", 5);
        assertEquals(4, Main.getJumpValue(operand2));
    }

    @Test
    public void testIsNumeric() {
        // Test case 1
        String str1 = "10";
        assertTrue(Main.isNumeric(str1));

        // Test case 2
        String str2 = "x";
        assertFalse(Main.isNumeric(str2));
    }
}