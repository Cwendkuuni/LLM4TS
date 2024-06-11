import org.junit.Assert;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Test 1
        Main.processInstruction("halt");
        Assert.assertEquals(0, Main.iPtr);

        // Test 2
        Main.processInstruction("out Hello");
        Assert.assertEquals("Hello", System.out.toString().trim());
        System.setOut(null);

        // Test 3
        Variable.setVariableValue("x", 10);
        Main.processInstruction("inc x");
        Assert.assertEquals(11, Variable.getVariableValue("x"));

        // Test 4
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 2);
        Main.processInstruction("add a,b");
        Assert.assertEquals(7, Variable.getVariableValue("a"));

        // Test 5
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 2);
        Main.processInstruction("cmp a,b,x");
        Assert.assertEquals(0, Variable.getVariableValue("x"));

        // Test 6
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 5);
        Main.processInstruction("cmp a,b,x");
        Assert.assertEquals(1, Variable.getVariableValue("x"));
    }

    @Test
    public void testRemoveComment() {
        // Test 1
        String result1 = Main.removeComment("var x; // Declare variable x");
        Assert.assertEquals("var x;", result1);

        // Test 2
        String result2 = Main.removeComment("add a, b // Add a and b");
        Assert.assertEquals("add a, b", result2);
    }

    @Test
    public void testSplitArguments() {
        // Test 1
        String[] result1 = Main.splitArguments("a, b, c");
        String[] expected1 = {"a", "b", "c"};
        Assert.assertArrayEquals(expected1, result1);

        // Test 2
        String[] result2 = Main.splitArguments("hello");
        String[] expected2 = {"hello"};
        Assert.assertArrayEquals(expected2, result2);
    }

    @Test
    public void testGetValue() {
        // Test 1
        Variable.setVariableValue("x", 10);
        int result1 = Main.getValue("x");
        Assert.assertEquals(10, result1);

        // Test 2
        int result2 = Main.getValue("5");
        Assert.assertEquals(5, result2);
    }

    @Test
    public void testGetJumpValue() {
        // Test 1
        int result1 = Main.getJumpValue("5");
        Assert.assertEquals(4, result1);

        // Test 2
        Label.addLabel("loop", 10);
        int result2 = Main.getJumpValue("loop");
        Assert.assertEquals(9, result2);
    }

    @Test
    public void testGetStringValue() {
        // Test 1
        String result1 = Main.getStringValue("hello");
        Assert.assertEquals("hello", result1);

        // Test 2
        Variable.setVariableValue("x", 5);
        String result2 = Main.getStringValue("x");
        Assert.assertEquals("5", result2);

        // Test 3
        Variable.setVariableValue("y", 10);
        String result3 = Main.getStringValue("\"Hello \" + y + \" World\"");
        Assert.assertEquals("Hello 10 World", result3);
    }

    @Test
    public void testIsNumeric() {
        // Test 1
        boolean result1 = Main.isNumeric("10");
        Assert.assertTrue(result1);

        // Test 2
        boolean result2 = Main.isNumeric("hello");
        Assert.assertFalse(result2);
    }
}