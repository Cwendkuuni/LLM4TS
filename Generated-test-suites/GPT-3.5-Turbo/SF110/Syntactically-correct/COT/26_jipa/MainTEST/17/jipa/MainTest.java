import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void testProcessInstruction() {
        Main.processInstruction("move a, b");
        assertEquals(0, Variable.getVariableValue("a"));
        assertEquals(0, Variable.getVariableValue("b"));
    }

    @Test
    public void testGetFileName() {
        String expected = "test/file/path.txt";
        
        // Use ByteArrayInputStream to simulate user input
        InputStream inputStream = new ByteArrayInputStream(expected.getBytes());
        System.setIn(inputStream);
        
        String result = Main.getFileName();
        
        assertEquals(expected, result);
    }

    @Test
    public void testLoadInstructions() {
        String fileName = "test/file/path.txt";
        boolean result = Main.loadInstructions(fileName);
        
        assertTrue(result);
        assertEquals(0, Main.TOTAL_INSTRUCTIONS);
        assertArrayEquals(new String[0], Main.instruction);
    }

    @Test
    public void testRemoveComment() {
        String line = "instruction ; comment";
        String result = Main.removeComment(line);
        assertEquals("instruction", result);
        
        line = "instruction \"comment;\"";
        result = Main.removeComment(line);
        assertEquals(line, result);
        
        line = "instruction";
        result = Main.removeComment(line);
        assertEquals(line, result);
    }

    @Test
    public void testSplitArguments() {
        String original = "arg1, arg2 , arg3";
        String[] expected = { "arg1", "arg2", "arg3" };
        String[] result = Main.splitArguments(original);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetValue() {
        Variable.setVariableValue("a", 10);
        
        int result = Main.getValue("a");
        assertEquals(10, result);
        
        result = Main.getValue("5");
        assertEquals(5, result);
    }

    @Test
    public void testGetJumpValue() {
        int result = Main.getJumpValue("5");
        assertEquals(4, result);
        
        Label.addLabel("label", 5);
        result = Main.getJumpValue("label");
        assertEquals(4, result);
    }

    @Test
    public void testReadValue() {
        String input = "10";
        
        // Use ByteArrayInputStream to simulate user input
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        int result = Main.readValue();
        assertEquals(10, result);
    }

    @Test
    public void testGetStringValue() {
        Variable.setVariableValue("a", 10);
        
        String result = Main.getStringValue("a");
        assertEquals("10", result);
        
        result = Main.getStringValue("\"Hello\"");
        assertEquals("Hello", result);
        
        result = Main.getStringValue("\"String\"");
        assertEquals("String", result);
        
        result = Main.getStringValue("\"Hello\"a\"World\"");
        assertEquals("Hello10World", result);
    }

    @Test
    public void testIsNumeric() {
        boolean result = Main.isNumeric("10");
        assertTrue(result);
        
        result = Main.isNumeric("abc");
        assertFalse(result);
    }
}