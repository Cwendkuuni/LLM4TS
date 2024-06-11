import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption("opt"));
        assertFalse(commandLine.hasOption('o'));

        commandLine.addOption(new Option("opt", "option", false, "Option"));

        assertTrue(commandLine.hasOption("opt"));
        assertTrue(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(commandLine.getOptionObject("opt"));
        assertNull(commandLine.getOptionObject('o'));

        commandLine.addOption(new Option("opt", "option", false, "Option"));

        assertNotNull(commandLine.getOptionObject("opt"));
        assertNotNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));

        commandLine.addOption(new Option("opt", "option", false, "Option"));

        assertNull(commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));

        commandLine.addOptionValue("opt", "value");

        assertEquals("value", commandLine.getOptionValue("opt"));
        assertEquals("value", commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));

        commandLine.addOption(new Option("opt", "option", true, "Option"));

        assertNull(commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));

        commandLine.addOptionValue("opt", "value1");
        commandLine.addOptionValue("opt", "value2");

        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("opt"));
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        assertEquals("default", commandLine.getOptionValue("opt", "default"));
        assertEquals("default", commandLine.getOptionValue('o', "default"));

        commandLine.addOption(new Option("opt", "option", false, "Option"));

        assertNull(commandLine.getOptionValue("opt", "default"));
        assertNull(commandLine.getOptionValue('o', "default"));

        commandLine.addOptionValue("opt", "value");

        assertEquals("value", commandLine.getOptionValue("opt", "default"));
        assertEquals("value", commandLine.getOptionValue('o', "default"));
    }

    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[0], commandLine.getArgs());

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        List<String> argList = commandLine.getArgList();

        assertNotNull(argList);
        assertEquals(0, argList.size());

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        argList = commandLine.getArgList();

        assertNotNull(argList);
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testAddArg() {
        List<String> argList = commandLine.getArgList();

        assertNotNull(argList);
        assertEquals(0, argList.size());

        commandLine.addArg("arg1");

        assertEquals(1, argList.size());
        assertEquals("arg1", argList.get(0));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", false, "Option");

        assertEquals(0, commandLine.getOptions().length);

        commandLine.addOption(option);

        Option[] options = commandLine.getOptions();
        assertEquals(1, options.length);
        assertSame(option, options[0]);
    }
}