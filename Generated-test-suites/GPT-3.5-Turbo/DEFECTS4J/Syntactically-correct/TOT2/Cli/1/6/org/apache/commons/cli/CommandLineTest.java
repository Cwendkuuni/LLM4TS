import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("test"));
        cmd.addOption(new Option("test", "Test option"));
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("value", "Value option"));
        assertEquals("Value", cmd.getOptionObject("value"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("arg", "Argument option"));
        assertEquals("Argument", cmd.getOptionValue("arg"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("args", "Arguments option", true));
        String[] values = {"arg1", "arg2"};
        Option option = new Option("args", "Arguments option", true);
        option.setValues(values);
        cmd.addOption(option);
        assertArrayEquals(values, cmd.getOptionValues("args"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = {"arg1", "arg2"};
        assertArrayEquals(args, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("opt", "Option");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        cmd.addOption(option1);
        cmd.addOption(option2);
        int count = 0;
        for (Option opt : cmd.getOptions()) {
            count++;
        }
        assertEquals(2, count);
    }

}