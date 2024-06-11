import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    // Test case for hasOption(String opt)
    @Test
    public void testHasOptionString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));

        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    // Test case for hasOption(char opt)
    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));

        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption('b'));
    }

    // Test case for getOptionObject(String opt)
    @Test
    public void testGetOptionObjectString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "String"));

        assertNull(commandLine.getOptionObject("a"));
        commandLine.addArg("value");
        assertEquals("value", (String) commandLine.getOptionObject("a"));
    }

    // Test case for getOptionObject(char opt)
    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "Integer"));

        assertNull(commandLine.getOptionObject('a'));
        commandLine.addArg("1");
        assertEquals(1, (int) commandLine.getOptionObject('a'));
    }

    // Test case for getOptionValue(String opt)
    @Test
    public void testGetOptionValueString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));

        assertNull(commandLine.getOptionValue("a"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    // Test case for getOptionValue(char opt)
    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));

        assertNull(commandLine.getOptionValue('a'));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue('a'));
    }

    // Test case for getOptionValues(String opt)
    @Test
    public void testGetOptionValuesString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        String[] values = commandLine.getOptionValues("a");
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    // Test case for getOptionValues(char opt)
    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        String[] values = commandLine.getOptionValues('a');
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    // Test case for getOptionValue(String opt, String defaultValue)
    @Test
    public void testGetOptionValueStringDefaultValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));

        assertEquals("default", commandLine.getOptionValue("a", "default"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("a", "default"));
    }

    // Test case for getOptionValue(char opt, String defaultValue)
    @Test
    public void testGetOptionValueCharDefaultValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a"));

        assertEquals("default", commandLine.getOptionValue('a', "default"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue('a', "default"));
    }

    // Test case for getArgs()
    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        String[] args = commandLine.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    // Test case for getArgList()
    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        List<String> argList = commandLine.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

}