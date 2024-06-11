import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();

        // Test when option is present
        commandLine.addOption(new Option("a", "optionA", false, "Option A"));
        assertTrue(commandLine.hasOption("a"));

        // Test when option is not present
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();

        // Test when option is present
        Option option = new Option("a", "optionA", true, "Option A");
        option.addValue("value1");
        commandLine.addOption(option);
        assertEquals("value1", commandLine.getOptionObject("a"));

        // Test when option is not present
        assertNull(commandLine.getOptionObject("b"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();

        // Test when option is present
        Option option = new Option("a", "optionA", true, "Option A");
        option.addValue("value1");
        commandLine.addOption(option);
        assertEquals("value1", commandLine.getOptionValue("a"));

        // Test when option is not present
        assertNull(commandLine.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();

        // Test when option is present
        Option option = new Option("a", "optionA", true, "Option A");
        option.addValue("value1");
        option.addValue("value2");
        commandLine.addOption(option);
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));

        // Test when option is not present
        assertNull(commandLine.getOptionValues("b"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();

        // Test when there are args
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());

        // Test when there are no args
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();

        // Test when there are args
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());

        // Test when there are no args
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();

        // Test adding args
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();

        // Test adding options
        commandLine.addOption(new Option("a", "optionA", false, "Option A"));
        assertEquals(1, commandLine.getOptions().length);
    }
}