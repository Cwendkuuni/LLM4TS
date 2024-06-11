import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOptionString() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("opt1"));
        commandLine.addOption(new Option("opt1", false));
        assertTrue(commandLine.hasOption("opt1"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('o'));
        commandLine.addOption(new Option("opt1", 'o', false));
        assertTrue(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObjectString() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("opt1"));
        commandLine.addOption(new Option("opt1", 1, "Opt 1"));
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionObject("opt1"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('o'));
        commandLine.addOption(new Option("opt1", 'o', true, "Opt 1"));
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValueString() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("opt1"));
        commandLine.addOption(new Option("opt1", 1, "Opt 1"));
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionValue("opt1"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('o'));
        commandLine.addOption(new Option("opt1", 'o', true, "Opt 1"));
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValuesString() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("opt1"));
        commandLine.addOption(new Option("opt1", 1, "Opt 1"));
        commandLine.addArg("value1");
        assertArrayEquals(new String[]{"value1"}, commandLine.getOptionValues("opt1"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('o'));
        commandLine.addOption(new Option("opt1", 'o', true, "Opt 1"));
        commandLine.addArg("value1");
        assertArrayEquals(new String[]{"value1"}, commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueStringString() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("opt1", "default"));
        commandLine.addOption(new Option("opt1", 1, "Opt 1"));
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionValue("opt1", "default"));
    }

    @Test
    public void testGetOptionValueCharString() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('o', "default"));
        commandLine.addOption(new Option("opt1", 'o', true, "Opt 1"));
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionValue('o', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[]{}, commandLine.getArgs());
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertEquals(new LinkedList<>(), commandLine.getArgList());
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        LinkedList<String> argList = new LinkedList<>();
        argList.add("arg1");
        argList.add("arg2");
        assertEquals(argList, commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("opt1", false));
        assertTrue(commandLine.hasOption("opt1"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("opt1", false));
        commandLine.addOption(new Option("opt2", false));
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("opt1", iterator.next().getLongOpt());
        assertTrue(iterator.hasNext());
        assertEquals("opt2", iterator.next().getLongOpt());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("opt1", false));
        commandLine.addOption(new Option("opt2", false));
        Option[] options = commandLine.getOptions();
        assertEquals(2, options.length);
        assertEquals("opt1", options[0].getLongOpt());
        assertEquals("opt2", options[1].getLongOpt());
    }
}