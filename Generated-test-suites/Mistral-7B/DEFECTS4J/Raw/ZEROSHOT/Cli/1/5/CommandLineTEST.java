import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommandLineTest {

    CommandLine commandLine;
    Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "append", false, "append the text");
        options.addOption("b", "boolean", true, "show boolean value");
        options.addOption("c", "count", 3, "number of times to print");
        commandLine = new CommandLine();
    }

    @After
    public void tearDown() {
        commandLine = null;
    }

    @Test
    public void testHasOptionFalse() throws ParseException {
        String[] args = new String[]{"test"};
        commandLine.parse(options, args);

        assertFalse(commandLine.hasOption("x"));
    }

    @Test
    public void testHasOptionTrue() throws ParseException {
        String[] args = new String[]{"-a"};
        commandLine.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOptionChar() throws ParseException {
        String[] args = new String[]{"-a"};
        commandLine.parse(options, args);

        assertTrue(commandLine.hasOption('a'));
    }

    @Test
    public void testGetOptionValueNull() throws ParseException {
        String[] args = new String[]{"test"};
        commandLine.parse(options, args);

        assertNull(commandLine.getOptionValue("x"));
    }

    @Test
    public void testGetOptionValueEmpty() throws ParseException {
        String[] args = new String[]{"-a"};
        commandLine.parse(options, args);

        assertNull(commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValueString() throws ParseException {
        String[] args = new String[]{"-a", "test"};
        commandLine.parse(options, args);

        assertEquals("test", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValueBooleanTrue() throws ParseException {
        String[] args = new String[]{"-b", "--boolean", "true"};
        commandLine.parse(options, args);

        assertTrue(commandLine.getOptionObject("b") instanceof Boolean);
        assertTrue((Boolean) commandLine.getOptionObject("b"));
    }

    @Test
    public void testGetOptionValueBooleanFalse() throws ParseException {
        String[] args = new String[]{"--boolean"};
        commandLine.parse(options, args);

        assertTrue(commandLine.getOptionObject("b") instanceof Boolean);
        assertFalse((Boolean) commandLine.getOptionObject("b"));
    }

    @Test
    public void testGetOptionValueInt() throws ParseException {
        String[] args = new String[]{"-c", "3", "test"};
        commandLine.parse(options, args);

        assertEquals(3, commandLine.getOptionObject("c"));
    }

    @Test
    public void testGetOptionValueList() throws ParseException {
        String[] args = new String[]{"--num", "1", "--num", "2", "--num", "3"};
        options.addOption("num", "number", true, "a list of numbers");

        commandLine.parse(options, args);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        assertArrayEquals(list.toArray(), commandLine.getOptionValues("num"));
    }

    @Test
    public void testGetArgs() throws ParseException {
        String[] args = new String[]{"test", "-a", "test"};
        commandLine.parse(options, args);

        String[] expected = new String[]{"test", "-a", "test"};
        assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() throws ParseException {
        String[] args = new String[]{"test", "-a", "test"};
        commandLine.parse(options, args);

        List<String> expected = new ArrayList<>();
        expected.add("test");
        expected.add("-a");
        expected.add("test");

        assertArrayEquals(expected.toArray(), commandLine.getArgList().toArray());
    }
}