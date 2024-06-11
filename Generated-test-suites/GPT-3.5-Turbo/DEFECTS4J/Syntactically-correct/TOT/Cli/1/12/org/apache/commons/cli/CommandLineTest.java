import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "option with argument", true));
        cmd.addOption(new Option("b", "option without argument"));

        // Test hasOption() on option with argument that is set
        assertTrue(cmd.hasOption("a"));

        // Test hasOption() on option without argument that is not set
        assertFalse(cmd.hasOption("b"));

        // Test hasOption() on non-existing option
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "option with argument", true));
        cmd.addOption(new Option("b", "option without argument"));

        // Test getOptionObject() on option with argument that is set
        assertEquals("argument", cmd.getOptionObject("a"));

        // Test getOptionObject() on option without argument that is set
        assertNull(cmd.getOptionObject("b"));

        // Test getOptionObject() on non-existing option
        assertNull(cmd.getOptionObject("c"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "option with argument", true));
        cmd.addOption(new Option("b", "option without argument"));

        // Test getOptionValue() on option with argument that is set
        assertEquals("argument", cmd.getOptionValue("a"));

        // Test getOptionValue() on option without argument that is set
        assertNull(cmd.getOptionValue("b"));

        // Test getOptionValue() on non-existing option
        assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "option with argument", true));
        cmd.addOption(new Option("b", "option without argument"));

        // Test getOptionValues() on option with argument that is set
        String[] expectedValues = {"argument"};
        assertArrayEquals(expectedValues, cmd.getOptionValues("a"));

        // Test getOptionValues() on option without argument that is set
        assertNull(cmd.getOptionValues("b"));

        // Test getOptionValues() on non-existing option
        assertNull(cmd.getOptionValues("c"));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "option with argument", true));
        cmd.addOption(new Option("b", "option without argument"));

        // Test getOptionValue() on option with argument that is set
        assertEquals("argument", cmd.getOptionValue("a", "default"));

        // Test getOptionValue() on option without argument that is set
        assertEquals("default", cmd.getOptionValue("b", "default"));

        // Test getOptionValue() on non-existing option
        assertEquals("default", cmd.getOptionValue("c", "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] expectedArgs = {"arg1", "arg2"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> expectedArgList = new ArrayList<>();
        expectedArgList.add("arg1");
        expectedArgList.add("arg2");
        assertEquals(expectedArgList, cmd.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> expectedArgList = new ArrayList<>();
        expectedArgList.add("arg1");
        expectedArgList.add("arg2");
        assertEquals(expectedArgList, cmd.getArgList());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", "option with argument", true);
        option1.addValue("argument1");
        Option option2 = new Option("b", "option without argument");
        cmd.addOption(option1);
        cmd.addOption(option2);

        // Test getOptionValues() on option with argument that is set
        String[] expectedValues = {"argument1"};
        assertArrayEquals(expectedValues, cmd.getOptionValues("a"));

        // Test getOptionValues() on option without argument that is set
        assertNull(cmd.getOptionValues("b"));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", "option with argument", true);
        Option option2 = new Option("b", "option without argument");
        cmd.addOption(option1);
        cmd.addOption(option2);

        Iterator<Option> iterator = cmd.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", "option with argument", true);
        Option option2 = new Option("b", "option without argument");
        cmd.addOption(option1);
        cmd.addOption(option2);

        Option[] expectedOptions = {option1, option2};
        assertArrayEquals(expectedOptions, cmd.getOptions());
    }

}