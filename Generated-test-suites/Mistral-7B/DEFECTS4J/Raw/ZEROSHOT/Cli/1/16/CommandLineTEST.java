import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommandLineTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        commandLine = new CommandLine();
    }

    @After
    public void tearDown() {
        commandLine = null;
        options = null;
    }

    @Test
    public void testHasOption() throws ParseException {
        options.addOption("option1", true);
        commandLine.parse(options.parse("--option1"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption('o'));
    }

    @Test
    public void testHasOptionFalse() throws ParseException {
        assertFalse(commandLine.hasOption("nonexistentoption"));
        assertFalse(commandLine.hasOption('z'));
    }

    @Test
    public void testGetOptionValue() throws ParseException {
        options.addOption("option1", true, "value1");
        commandLine.parse(options.parse("--option1=value2"));
        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue('o'));
        commandLine.parse(options.parse("--option1=value3"));
        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue('o'));
        commandLine.parse(options.parse("--option1=value1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertEquals("value1", commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValueWithDefault() throws ParseException {
        options.addOption("option1", true, "value1");
        commandLine.parse(options.parse("--option1"));
        assertEquals("true", commandLine.getOptionValue("option1", "defaultValue"));
        assertEquals("true", commandLine.getOptionValue('o', "defaultValue"));
    }

    @Test
    public void testGetOptionValues() throws ParseException {
        options.addOption("option1", true, "value1");
        commandLine.parse(options.parse("--option1=value1 --option1=value2 --option1=value3"));
        String[] expected = {"value1", "value2", "value3"};
        assertArrayEquals(expected, commandLine.getOptionValues("option1"));
        assertArrayEquals(expected, commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValuesEmpty() throws ParseException {
        commandLine.parse(options.parse("--option1"));
        assertArrayEquals(new String[0], commandLine.getOptionValues("option1"));
        assertArrayEquals(new String[0], commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValuesNone() throws ParseException {
        commandLine.parse(options.parse("--nonexistentoption=value1"));
        assertArrayEquals(new String[0], commandLine.getOptionValues("nonexistentoption"));
        assertArrayEquals(new String[0], commandLine.getOptionValues('n'));
    }

    @Test
    public void testGetArgs() {
        commandLine.parse(options.parse("--option1=value1 arg1 arg2 arg3"));
        List expected = List.of("arg1", "arg2", "arg3");
        assertArrayEquals(expected.toArray(new String[0]), commandLine.getArgs());
    }
}