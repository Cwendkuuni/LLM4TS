import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", false, "test option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("t"));
        assertFalse(commandLine.hasOption("xyz"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", true, "test option");
        option.addValue("value");
        commandLine.addOption(option);

        assertEquals("value", commandLine.getOptionValue("t"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", true, "test option");
        option.addValue("value1");
        option.addValue("value2");
        commandLine.addOption(option);

        String[] values = commandLine.getOptionValues("t");
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", true, "test option");
        option.addValue("10");
        commandLine.addOption(option);

        Object obj = commandLine.getOptionObject("t");
        assertTrue(obj instanceof Integer);
        assertEquals(10, obj);
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("argument1");

        assertEquals(1, commandLine.getArgs().length);
        assertEquals("argument1", commandLine.getArgs()[0]);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", false, "test option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("t"));
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option1 = new Option("t", "test", false, "test option 1");
        Option option2 = new Option("x", "extra", false, "test option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] options = commandLine.getOptions();
        assertEquals(2, options.length);
        assertTrue(options[0].getOpt().equals("t") || options[0].getOpt().equals("x"));
        assertTrue(options[1].getOpt().equals("t") || options[1].getOpt().equals("x"));
    }

}