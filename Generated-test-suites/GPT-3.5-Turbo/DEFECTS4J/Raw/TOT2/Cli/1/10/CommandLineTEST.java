import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("b", "test option"));
        assertTrue(cmd.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("c", "test option"));
        assertEquals("test option", cmd.getOptionObject("c"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("d", "test option"));
        assertEquals("test option", cmd.getOptionValue("d"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("e", "test option"));
        String[] values = {"value1", "value2"};
        assertEquals(values, cmd.getOptionValues("e"));
    }

    @Test
    public void testGetOptionValue_default() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "test option"));
        assertEquals("default", cmd.getOptionValue("f", "default"));
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
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("g", "test option 1");
        Option opt2 = new Option("h", "test option 2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Iterator<Option> iterator = cmd.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(opt1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(opt2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("i", "test option 1");
        Option opt2 = new Option("j", "test option 2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Option[] options = cmd.getOptions();
        Option[] expected = {opt1, opt2};
        assertArrayEquals(expected, options);
    }
}