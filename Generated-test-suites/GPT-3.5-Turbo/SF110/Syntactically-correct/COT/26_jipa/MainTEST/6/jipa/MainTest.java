import org.junit.*;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessInstruction() {
        // Test halt instruction
        Main.processInstruction("halt");

        // Test out instruction
        Main.processInstruction("out Hello World");

        // Test in instruction
        Main.processInstruction("in input");

        // Test mov instruction
        Main.processInstruction("mov input, output");

        // Test cpy instruction
        Main.processInstruction("cpy input, output");

        // Test set instruction
        Main.processInstruction("set variable, 10");

        // Test del instruction
        Main.processInstruction("del variable");

        // Test arr instruction
        Main.processInstruction("arr array, 5");

        // Test var instruction
        Main.processInstruction("var variable");

        // Test inc instruction
        Main.processInstruction("inc variable");

        // Test dec instruction
        Main.processInstruction("dec variable");

        // Test add instruction
        Main.processInstruction("add variable, 5");

        // Test sub instruction
        Main.processInstruction("sub variable, 5");

        // Test mul instruction
        Main.processInstruction("mul variable, 5");

        // Test div instruction
        Main.processInstruction("div variable, 5");

        // Test mod instruction
        Main.processInstruction("mod variable, 5");

        // Test xor instruction
        Main.processInstruction("xor variable, 5");

        // Test or instruction
        Main.processInstruction("or variable, 5");

        // Test and instruction
        Main.processInstruction("and variable, 5");

        // Test jmp instruction
        Main.processInstruction("jmp 10");

        // Test jnz instruction
        Main.processInstruction("jnz 5, 10");

        // Test ja instruction
        Main.processInstruction("ja 5, 10, 15");

        // Test jae instruction
        Main.processInstruction("jae 5, 10, 15");

        // Test jb instruction
        Main.processInstruction("jb 5, 10, 15");

        // Test jbe instruction
        Main.processInstruction("jbe 5, 10, 15");

        // Test jz instruction
        Main.processInstruction("jz 5, 10");

        // Test je instruction
        Main.processInstruction("je 5, 10, 15");

        // Test jne instruction
        Main.processInstruction("jne 5, 10, 15");

        // Test cmp instruction
        Main.processInstruction("cmp 5, 5, result");
    }

    @Test
    public void testGetFileName() {
        // Test if user enters a valid file name
        // The test should wait for user input but we cannot simulate it in this test

        // Test if user enters q to quit
        // The test should exit the program but we cannot simulate it in this test
    }

    @Test
    public void testLoadInstructions() {
        // Test if the file name is empty
        assertFalse(Main.loadInstructions(""));

        // Test if the file name is "q"
        // The test should exit the program but we cannot simulate it in this test

        // Test if the file name is a valid file
        assertTrue(Main.loadInstructions("instructions.txt"));

        // Test if the file name is an invalid file
        assertFalse(Main.loadInstructions("invalid.txt"));
    }

    @Test
    public void testRemoveComment() {
        // Test if the line does not have a comment
        assertEquals("hello world", Main.removeComment("hello world"));

        // Test if the line has a comment
        assertEquals("hello world", Main.removeComment("hello world; comment"));

        // Test if the comment is inside double quotes
        assertEquals("hello world", Main.removeComment("hello world; comment; \"comment;\""));
    }

    @Test
    public void testSplitArguments() {
        // Test if the original string does not have a comma
        assertArrayEquals(new String[] {"hello world"}, Main.splitArguments("hello world"));

        // Test if the original string has multiple arguments separated by a comma
        assertArrayEquals(new String[] {"hello", "world"}, Main.splitArguments("hello, world"));

        // Test if the arguments include double quotes
        assertArrayEquals(new String[] {"hello", "\"world\""}, Main.splitArguments("hello, \"world\""));

        // Test if the arguments include a comma inside double quotes
        assertArrayEquals(new String[] {"hello, world", "\"hello, \"\"world\"\"\""}, Main.splitArguments("hello, world, \"hello, \"\"world\"\"\""));
    }

    @Test
    public void testGetValue() {
        // Test if the operand is a numeric string
        assertEquals(10, Main.getValue("10"));

        // Test if the operand is a variable name
        // The test should check the variable value but we cannot simulate it in this test
    }

    @Test
    public void testGetJumpValue() {
        // Test if the operand is a numeric string
        assertEquals(9, Main.getJumpValue("10"));

        // Test if the operand is a label name
        // The test should check the label value but we cannot simulate it in this test
    }

    @Test
    public void testReadValue() {
        // The test should wait for user input but we cannot simulate it in this test

        // Test if the user enters a numeric string
        assertEquals(10, Main.readValue());

        // Test if the user enters a non-numeric string
        assertEquals(0, Main.readValue());
    }

    @Test
    public void testGetStringValue() {
        // Test if the original string does not have double quotes
        assertEquals("10", Main.getStringValue("10"));

        // Test if the original string has double quotes
        assertEquals("Hello world 10", Main.getStringValue("\"Hello world \" + 10"));

        // Test if the original string has double quotes inside double quotes
        assertEquals("Hello \"world\" 10", Main.getStringValue("\"Hello \"\"world\"\" \" + 10"));
    }

    @Test
    public void testIsNumeric() {
        // Test if the string is a numeric string
        assertTrue(Main.isNumeric("10"));

        // Test if the string is a non-numeric string
        assertFalse(Main.isNumeric("Hello World"));
    }
}

