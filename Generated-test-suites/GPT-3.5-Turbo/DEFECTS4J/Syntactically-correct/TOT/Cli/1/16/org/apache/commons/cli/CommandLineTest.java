
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", false, "option 'a'"));
        cmd.addOption(new Option("b", false, "option 'b'"));

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption('b'));
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", true, "option 'a'"));
        cmd.addOption(new Option("b", false, "option 'b'"));

        cmd.addOptionValue("a", "value1");
        cmd.addOptionValue("b", "value2");

        assertEquals("value1", cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject('c'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", true, "option 'a'"));
        cmd.addOption(new Option("b", false, "option 'b'"));

        cmd.addOptionValue("a", "value1");

        assertEquals("value1", cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue('c'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", true, "option 'a'"));
        cmd.addOption(new Option("b", false, "option 'b'"));

        cmd.addOptionValue("a", "value1");
        cmd.addOptionValue("a", "value2");

        String[] expected = {"value1", "value2"};
        assertArrayEquals(expected, cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues('c'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");

        assertEquals(expected, cmd.getArgList());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", false, "option 'a'");
        Option option2 = new Option("b", false, "option 'b'");
        cmd.addOption(option1);
        cmd.addOption(option2);

        Option[] expected = {option1, option2};
        assertArrayEquals(expected, cmd.getOptions());
    }
}

