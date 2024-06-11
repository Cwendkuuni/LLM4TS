package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
    private static final String TEST_FILE_PATH = "test.txt";

    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    @Before
    public void setUp() throws FileNotFoundException {
        fileOutputStream = new FileOutputStream(TEST_FILE_PATH);
        fileInputStream = new FileInputStream(TEST_FILE_PATH);
    }

    @After
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        file.delete();
    }

    @Test
    public void testMain() {
        // TODO: Write test cases for the main method
    }

    @Test
    public void testProcessInstruction() {
        // TODO: Write test cases for the processInstruction method
    }

    @Test
    public void testGetFileName() {
        // TODO: Write test cases for the getFileName method
    }

    @Test
    public void testLoadInstructions() {
        // TODO: Write test cases for the loadInstructions method
    }

    @Test
    public void testRemoveComment() {
        String line = "instruction; // comment";
        assertEquals("instruction;", Main.removeComment(line));

        line = "instruction; \"string with ; comment\"";
        assertEquals("instruction; \"string with ; comment\"", Main.removeComment(line));
    }

    @Test
    public void testResetInstructions() {
        // Prepare test data
        Main.TOTAL_INSTRUCTIONS = 5;

        // Call the method
        Main.resetInstructions();

        // Check the result
        for (String instr : Main.instruction) {
            assertEquals("", instr);
        }
    }

    @Test
    public void testSplitArguments() {
        String original = "arg1, arg2, arg3";
        String[] expected = { "arg1", "arg2", "arg3" };
        String[] result = Main.splitArguments(original);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testGetValue() {
        // Prepare test data
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);

        // Test with variable operand
        String operand = "var1";
        assertEquals(10, Main.getValue(operand));

        // Test with numeric operand
        operand = "20";
        assertEquals(20, Main.getValue(operand));
    }

    @Test
    public void testGetJumpValue() {
        // Prepare test data
        Label.addLabel("label1");
        Label.setLabelValue("label1", 3);

        // Test with label operand
        String operand = "label1";
        assertEquals(2, Main.getJumpValue(operand));

        // Test with numeric operand
        operand = "5";
        assertEquals(4, Main.getJumpValue(operand));
    }

    @Test
    public void testReadValue() {
        // Prepare test data
        String input = "10";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Call the method
        int result = Main.readValue();

        // Check the result
        assertEquals(10, result);
    }

    @Test
    public void testGetStringValue() {
        // Prepare test data
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);

        // Test with string operand
        String operand = "\"string\"";
        String result = Main.getStringValue(operand);
        assertEquals("string", result);

        // Test with variable operand
        operand = "var1";
        result = Main.getStringValue(operand);
        assertEquals("10", result);
    }

    @Test
    public void testIsNumeric() {
        // Test with numeric string
        String str = "10";
        boolean result = Main.isNumeric(str);
        assertEquals(true, result);

        // Test with non-numeric string
        str = "abc";
        result = Main.isNumeric(str);
        assertEquals(false, result);
    }

    @Test
    public void testToOpcode() {
        // Test with valid opcode
        String str = "mov";
        Main.opcodes result = Main.opcodes.toOpcode(str);
        assertEquals(Main.opcodes.mov, result);

        // Test with invalid opcode
        str = "abc";
        result = Main.opcodes.toOpcode(str);
        assertEquals(Main.opcodes.novalue, result);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        // Prepare test data
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);

        // Serialize Variable object
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
        out.writeObject(Variable.getInstance());
        out.close();

        // Deserialize Variable object
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        Variable deserializedVariable = (Variable) in.readObject();
        in.close();

        // Check the deserialized Variable object
        int value = deserializedVariable.getVariableValue("var1");
        assertEquals(10, value);
    }
}