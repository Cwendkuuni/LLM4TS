import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("test"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue("test", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue('t', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg");
        assertEquals(1, commandLine.getArgList().size());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("test", "Test option"));
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getOptions().length);
    }
}