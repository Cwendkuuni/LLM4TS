import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOptionString() {
        assertFalse(commandLine.hasOption("test"));
        commandLine.addOption(new Option("test"));
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(commandLine.hasOption('t'));
        commandLine.addOption(new Option('t'));
        assertTrue(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObjectString() {
        assertNull(commandLine.getOptionObject("test"));
        commandLine.addOption(new Option("test", "Test option", true, "String option"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObjectChar() {
        assertNull(commandLine.getOptionObject('t'));
        commandLine.addOption(new Option('t', "Test option", true, "String option"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValueString() {
        assertNull(commandLine.getOptionValue("test"));
        commandLine.addOption(new Option("test", "Test option", true, "String option"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(commandLine.getOptionValue('t'));
        commandLine.addOption(new Option('t', "Test option", true, "String option"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValuesString() {
        assertNull(commandLine.getOptionValues("test"));
        commandLine.addOption(new Option("test", "Test option", true, "String option"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        String[] values = commandLine.getOptionValues("test");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(commandLine.getOptionValues('t'));
        commandLine.addOption(new Option('t', "Test option", true, "String option"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        String[] values = commandLine.getOptionValues('t');
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testGetOptionValueStringString() {
        String defaultValue = "default";
        assertEquals(defaultValue, commandLine.getOptionValue("test", defaultValue));
        commandLine.addOption(new Option("test", "Test option", true, "String option"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("test", defaultValue));
    }

    @Test
    public void testGetOptionValueCharString() {
        String defaultValue = "default";
        assertEquals(defaultValue, commandLine.getOptionValue('t', defaultValue));
        commandLine.addOption(new Option('t', "Test option", true, "String option"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue('t', defaultValue));
    }

    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addArg("arg3");
        assertArrayEquals(args, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addArg("arg3");
        assertEquals(args, commandLine.getArgList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test option", true, "String option");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions()[0]);
    }
}