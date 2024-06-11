import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("test", "testing"));

        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("t", "testing"));

        assertTrue(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("test", "testing"));
        commandLine.addArg("value");

        Object obj = commandLine.getOptionObject("test");
        assertNotNull(obj);
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("test", "testing"));
        commandLine.addArg("value");

        String value = commandLine.getOptionValue("test");
        assertNotNull(value);
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("test", "testing"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        String[] values = commandLine.getOptionValues("test");
        assertNotNull(values);
        assertEquals(2, values.length);
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine commandLine = new CommandLine();

        String value = commandLine.getOptionValue("test", "default");
        assertEquals("default", value);
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("test");

        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(1, args.length);
    }
}