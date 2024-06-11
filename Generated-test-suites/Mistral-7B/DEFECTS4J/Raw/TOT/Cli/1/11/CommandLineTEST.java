import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CommandLine} class.
 */
public class CommandLineTest {

    /**
     * Test method for {@link CommandLine#CommandLine()}.
     */
    @Test
    public void testEmptyCommandLine() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getArgs());
        assertTrue(commandLine.getArgList().isEmpty());
        assertFalse(commandLine.hasOption("option"));
    }

    /**
     * Test method for {@link CommandLine#CommandLine(String[])}.
     */
    @Test
    public void testCommandLineWithOptions() throws ParseException {
        String[] args = new String[]{"-a", "--optionB=valueB", "arg1", "arg2"};
        CommandLine commandLine = new CommandLine(args);
        String[] expectedArgs = new String[]{"arg1", "arg2"};

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption('a'));
        assertTrue(commandLine.hasOption("optionB"));
        assertTrue(commandLine.hasOption('B'));
        assertFalse(commandLine.hasOption("nonexistentOption"));

        assertEquals("valueB", commandLine.getOptionValue("optionB"));
        assertEquals("valueB", commandLine.getOptionValue('B'));
        assertNull(commandLine.getOptionValue("nonexistentOption"));

        assertArrayEquals(expectedArgs, commandLine.getArgs());
        assertEquals(expectedArgs, commandLine.getArgList().toArray());
    }

    /**
     * Test method for {@link CommandLine#getOptionValues(String)}.
     */
    @Test
    public void testGetOptionValues() {
        String[] args = new String[]{"--optionA=value1", "--optionA=value2"};
        CommandLine commandLine = new CommandLine(args);

        String[] expectedValues = new String[]{"value1", "value2"};
        String[] actualValues = commandLine.getOptionValues("optionA");

        assertArrayEquals(expectedValues, actualValues);
        assertArrayEquals(expectedValues, commandLine.getOptionValues('A'));
    }

    /**
     * Test method for {@link CommandLine#getOptionValues(String, String)}.
     */
    @Test
    public void testGetOptionValuesWithDefaultValue() {
        String[] args = new String[]{"--optionA=value1", "--optionB=value2"};
        CommandLine commandLine = new CommandLine(args);

        String defaultValue = "defaultValue";
        String[] expectedValues = new String[]{"value1"};

        String[] actualValues = commandLine.getOptionValues("optionA", defaultValue);
        assertArrayEquals(expectedValues, actualValues);
        assertNull(commandLine.getOptionValues("nonexistentOption", defaultValue));
        assertEquals(defaultValue, commandLine.getOptionValues("optionC", defaultValue));
    }

    /**
     * Test method for {@link CommandLine#parse(String[])}.
     */
    @Test
    public void testParseCommandLine() throws ParseException {
        String[] args = new String[]{"--optionA=value1", "--optionB=value2", "arg1", "arg2"};
        CommandLine commandLine = new CommandLine();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        parser.parse(args, commandLine, true, true, formatter, null);

        String[] expectedArgs = new String[]{"arg1", "arg2"};

        assertTrue(commandLine.hasOption("optionA"));
        assertTrue(commandLine.hasOption('A'));
        assertTrue(commandLine.hasOption("optionB"));
        assertTrue(commandLine.hasOption('B'));

        assertEquals("value1", commandLine.getOptionValue("optionA"));
        assertEquals("value1", commandLine.getOptionValue('A'));
        assertEquals("value2", commandLine.getOptionValue("optionB"));
        assertEquals("value2", commandLine.getOptionValue('B'));

        assertArrayEquals(expectedArgs, commandLine.getArgs());
        assertEquals(expectedArgs, commandLine.getArgList().toArray());
    }

    /**
     * Test method for {@link CommandLine#parse(String[])}.
     */
    @Test(expected = ParseException.class)
    public void testParseCommandLineWithError() throws ParseException {
        String[] args = new String[]{"--optionA=value1", "--optionB=value2", "arg1", "--nonexistentOption"};
        CommandLine commandLine = new CommandLine();
        CommandLineParser parser = new DefaultParser();

        parser.parse(args, commandLine, true, true, null, null);

        // This test should fail with a ParseException.
    }

    /**
     * Test method for {@link CommandLine#toString()}.
     */
    @Test
    public void testToString() {
        String[] args = new String[]{"--optionA=value1", "--optionB=value2", "arg1", "arg2"};
        CommandLine commandLine = new CommandLine(args);

        StringBuilder expected = new StringBuilder();
        expected.append("[ CommandLine: [ options: [Option: optionA=value1], [Option: optionB=value2]] [ args: [arg1, arg2]] ] ]");

        assertEquals(expected.toString(), commandLine.toString());
    }

    private void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Expected array has different length: " + expected.length + " != " + actual.length);
        }

        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Expected: " + expected[i] + " but was: " + actual[i]);
            }
        }
    }
}