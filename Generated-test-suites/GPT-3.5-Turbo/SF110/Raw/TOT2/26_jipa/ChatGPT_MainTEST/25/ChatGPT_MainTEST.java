import org.junit.Test;
import static org.junit.Assert.*;

public class ChatGPT_MainTest {

    @Test
    public void testProcessInstruction() {
        String[] args = new String[1];
        args[0] = "instruction.txt";
        Main.loadInstructions(args[0]);
        Main.processInstruction("out hello");
        assertEquals("hello\n", Main.getStringValue("hello"));
    }

    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "instruction.txt";
        Main.main(args);
    }

    @Test
    public void testHalt() {
        Main.processInstruction("halt");
        assertTrue(true);
    }

    @Test
    public void testOut() {
        Main.processInstruction("out hello");
        assertEquals("hello\n", Main.getStringValue("hello"));
    }

    @Test
    public void testIn() {
        Main.processInstruction("in hello");
        assertNotNull(Main.getValue("hello"));
    }

    @Test
    public void testMov() {
        Variable.setVariableValue("source", 42);
        Main.processInstruction("mov source destination");
        assertEquals(0, Variable.getVariableValue("source"));
        assertEquals(42, Variable.getVariableValue("destination"));
    }

    @Test
    public void testCpy() {
        Variable.setVariableValue("source", 42);
        Main.processInstruction("cpy source destination");
        assertEquals(42, Variable.getVariableValue("destination"));
    }

    @Test
    public void testSet() {
        Main.processInstruction("set variable 42");
        assertEquals(42, Main.getValue("variable"));
    }

    @Test
    public void testDel() {
        Variable.addVariable("variable");
        Main.processInstruction("del variable");
        assertNull(Variable.getVariableValue("variable"));
    }

    @Test
    public void testArr() {
        Main.processInstruction("arr array 5");
        assertNotNull(Variable.getVariableValue("array"));
        for (int i = 1; i < 5; ++i) {
            assertNotNull(Variable.getVariableValue("array" + i));
        }
    }

    @Test
    public void testVar() {
        Main.processInstruction("var variable");
        assertNotNull(Variable.getVariableValue("variable"));
    }

    @Test
    public void testInc() {
        Variable.setVariableValue("variable", 42);
        Main.processInstruction("inc variable");
        assertEquals(43, Variable.getVariableValue("variable"));
    }

    @Test
    public void testDec() {
        Variable.setVariableValue("variable", 42);
        Main.processInstruction("dec variable");
        assertEquals(41, Variable.getVariableValue("variable"));
    }

    @Test
    public void testAdd() {
        Variable.setVariableValue("variable", 42);
        Main.processInstruction("add variable 8");
        assertEquals(50, Variable.getVariableValue("variable"));
    }

    @Test
    public void testSub() {
        Variable.setVariableValue("variable", 42);
        Main.processInstruction("sub variable 8");
        assertEquals(34, Variable.getVariableValue("variable"));
    }

    @Test
    public void testMul() {
        Variable.setVariableValue("variable", 6);
        Main.processInstruction("mul variable 7");
        assertEquals(42, Variable.getVariableValue("variable"));
    }

    @Test
    public void testDiv() {
        Variable.setVariableValue("variable", 42);
        Main.processInstruction("div variable 6");
        assertEquals(7, Variable.getVariableValue("variable"));
    }

    @Test
    public void testProcessInstruction_halt() {
        String instruction = "halt";
        int iPtr = 0;
        Main.instruction[iPtr] = instruction;
        processInstruction(instruction);
        assertEquals(0, iPtr);
    }

    @Test
    public void testProcessInstruction_out() {
        String operand = "hello";
        String instruction = "out " + operand;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        processInstruction(instruction);
        assertEquals(operand + "\n", outContent.toString());
    }

    @Test
    public void testProcessInstruction_in() {
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
        String operand1 = "var1";
        String operand2 = "var2";
        Variable.setVariableValue(operand1, 123);
        String instruction = "mov " + operand1 + "," + operand2;
        processInstruction(instruction);
        assertEquals(0, Variable.getVariableValue(operand1));
        assertEquals(123, Variable.getVariableValue(operand2));
    }

    @Test
    public void testProcessInstruction_cpy() {
        String operand1 = "var1";
        String operand2 = "var2";
        Variable.setVariableValue(operand1, 123);
        Variable.setVariableValue(operand2, 456);
        String instruction = "cpy " + operand1 + "," + operand2;
        processInstruction(instruction);
        assertEquals(123, Variable.getVariableValue(operand2));
    }

    @Test
    public void testProcessInstruction_set() {
        String operand1 = "var1";
        int value = 123;
        String instruction = "set " + operand1 + "," + value;
        processInstruction(instruction);
        assertEquals(value, Variable.getVariableValue(operand1));
    }

    @Test
    public void testSet() {
        Variable.clear();
        Main.processInstruction("set a, 42");
        assertEquals(42, Variable.getVariableValue("a"));
    }

    @Test
    public void testDel() {
        Variable.clear();
        Variable.setVariableValue("a", 42);
        Main.processInstruction("del a");
        assertFalse(Variable.isVariableDefined("a"));
        assertTrue(Variable.isVariableDefined("b"));
    }

    @Test
    public void testArr() {
        Variable.clear();
        Main.processInstruction("arr a, 5");
        assertTrue(Variable.isVariableDefined("a"));
        for (int i = 0; i < 5; i++) {
            assertTrue(Variable.isVariableDefined("a" + i));
        }
        assertEquals(0, Variable.getVariableValue("a"));
    }

    @Test
    public void testVar() {
        Variable.clear();
        Main.processInstruction("var a");
        assertTrue(Variable.isVariableDefined("a"));
        assertEquals(0, Variable.getVariableValue("a"));
    }

    @Test
    public void testInc() {
        Variable.clear();
        Variable.setVariableValue("a", 42);
        Main.processInstruction("inc a");
        assertEquals(43, Variable.getVariableValue("a"));
    }

    @Test
    public void testDec() {
        Variable.clear();
        Variable.setVariableValue("a", 42);
        Main.processInstruction("dec a");
        assertEquals(41, Variable.getVariableValue("a"));
    }

    @Test
    public void testAdd() {
        Variable.clear();
        Variable.setVariableValue("a", 42);
        Main.processInstruction("add a, 8");
        assertEquals(50, Variable.getVariableValue("a"));
    }

    @Test
    public void testSub() {
        Variable.clear();
        Variable.setVariableValue("a", 42);
        Main.processInstruction("sub a, 8");
        assertEquals(34, Variable.getVariableValue("a"));
    }

    @Test
    public void testGetFileName() {
        String expected = "testfile.txt";
        System.setIn(new ByteArrayInputStream(expected.getBytes()));
        String actual = Main.getFileName();
        assertEquals(expected, actual);

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

    @Test
    public void testResetInstructions() {
        Main.TOTAL_INSTRUCTIONS = 5;
        Main.instruction = new String[Main.TOTAL_INSTRUCTIONS];
        Main.iPtr = 0;
        Main.instruction[0] = "test";
        Main.instruction[2] = "test";
        Main.resetInstructions();
        for (int i = 0; i < Main.TOTAL_INSTRUCTIONS; i++) {
            assertEquals("", Main.instruction[i]);
        }
    }

    @Test
    public void testSplitArguments() {
        String input = "test1, test2, \"test 3\"";
        String[] expected = {"test1", "test2", "test 3"};
        String[] actual = Main.splitArguments(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetValue() {
        String operand = "5";
        int expected = 5;
        int actual = Main.getValue(operand);
        assertEquals(expected, actual);

        operand = "x";
        Variable.setVariableValue("x", 10);
        expected = 10;
        actual = Main.getValue(operand);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetJumpValue() {
        String operand = "5";
        int expected = 4;
        int actual = Main.getJumpValue(operand);
        assertEquals(expected, actual);

        operand = "label";
        Label.setLabel("label", 10);
        expected = 9;
        actual = Main.getJumpValue(operand);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadValue() {
        String input = "5";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int expected = 5;
        int actual = Main.readValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStringValue() {
        String input = "5";
        String expected = "5";
        String actual = Main.getStringValue(input);
        assertEquals(expected, actual);

        input = "\"hello, world! My name is \" + name";
        Variable.setVariableValue("name", "Alice");
        expected = "hello, world! My name is Alice";
        actual = Main.getStringValue(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNumeric() {
        String str = "123";
        assertTrue(Main.isNumeric(str));

        str = "abc";
        assertFalse(Main.isNumeric(str));
    }

}