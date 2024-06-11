import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("opt1"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("opt1"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("opt1"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("opt1"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue("opt1", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue('o', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        assertArrayEquals(new String[]{"arg1"}, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("opt1", "option1", false, "option 1");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions()[0]);
    }
}