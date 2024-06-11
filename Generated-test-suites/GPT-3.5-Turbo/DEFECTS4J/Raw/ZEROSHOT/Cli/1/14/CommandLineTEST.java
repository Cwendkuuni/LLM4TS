import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOptionWithString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOptionWithChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObjectWithString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertNull(commandLine.getOptionObject("a"));
    }

    @Test
    public void testGetOptionObjectWithChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertNull(commandLine.getOptionObject('a'));
    }

    @Test
    public void testGetOptionValueWithString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertNull(commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValueWithChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertNull(commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValuesWithString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertNull(commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValuesWithChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertNull(commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValueWithDefaultWithString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertEquals("default", commandLine.getOptionValue("a", "default"));
    }

    @Test
    public void testGetOptionValueWithDefaultWithChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        assertEquals("default", commandLine.getOptionValue('a', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List<String> expected = new LinkedList<>();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next().getOpt());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, ""));
        Option[] expected = {new Option("a", false, "")};
        assertArrayEquals(expected, commandLine.getOptions());
    }
}