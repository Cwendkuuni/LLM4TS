import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        // Test case 1: Typical input ->  option exists
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        assertTrue(cmd.hasOption("a"));

        // Test case 2: Typical input -> option does not exist
        assertFalse(cmd.hasOption("b"));

        // Test case 3: Empty input
        assertFalse(cmd.hasOption(""));
    }

    @Test
    public void testHasOptionChar() {
        // Test case 1: Typical input -> option exists
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option('a'));
        assertTrue(cmd.hasOption('a'));

        // Test case 2: Typical input -> option does not exist
        assertFalse(cmd.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        // Test case 1: Typical input -> option exists with argument
        CommandLine cmd = new CommandLine();
        Option option = new Option("o");
        option.setType(Integer.class);
        option.addValue("10");
        cmd.addOption(option);
        assertEquals(10, cmd.getOptionObject("o"));

        // Test case 2: Typical input -> option exists without argument
        cmd.addOption(new Option("a"));
        assertNull(cmd.getOptionObject("a"));

        // Test case 3: Empty input
        assertNull(cmd.getOptionObject(""));
    }

    @Test
    public void testGetOptionObjectChar() {
        // Test case 1: Typical input -> option exists with argument
        CommandLine cmd = new CommandLine();
        Option option = new Option('o');
        option.setType(Integer.class);
        option.addValue("10");
        cmd.addOption(option);
        assertEquals(10, cmd.getOptionObject('o'));

        // Test case 2: Typical input -> option exists without argument
        cmd.addOption(new Option('a'));
        assertNull(cmd.getOptionObject('a'));
    }

    @Test
    public void testGetOptionValue() {
        // Test case 1: Typical input -> option exists with argument
        CommandLine cmd = new CommandLine();
        Option option = new Option("o");
        option.addValue("value");
        cmd.addOption(option);
        assertEquals("value", cmd.getOptionValue("o"));

        // Test case 2: Typical input -> option exists without argument
        cmd.addOption(new Option("a"));
        assertNull(cmd.getOptionValue("a"));

        // Test case 3: Empty input
        assertNull(cmd.getOptionValue(""));
    }

    @Test
    public void testGetOptionValueChar() {
        // Test case 1: Typical input -> option exists with argument
        CommandLine cmd = new CommandLine();
        Option option = new Option('o');
        option.addValue("value");
        cmd.addOption(option);
        assertEquals("value", cmd.getOptionValue('o'));

        // Test case 2: Typical input -> option exists without argument
        cmd.addOption(new Option('a'));
        assertNull(cmd.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValues() {
        // Test case 1: Typical input -> option exists with multiple arguments
        CommandLine cmd = new CommandLine();
        Option option = new Option("o");
        option.addValue("value1");
        option.addValue("value2");
        cmd.addOption(option);
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("o"));

        // Test case 2: Typical input -> option exists with single argument
        cmd.addOption(new Option("a"));
        assertNull(cmd.getOptionValues("a"));

        // Test case 3: Empty input
        assertNull(cmd.getOptionValues(""));
    }

    @Test
    public void testGetOptionValuesChar() {
        // Test case 1: Typical input -> option exists with multiple arguments
        CommandLine cmd = new CommandLine();
        Option option = new Option('o');
        option.addValue("value1");
        option.addValue("value2");
        cmd.addOption(option);
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues('o'));

        // Test case 2: Typical input -> option exists with single argument
        cmd.addOption(new Option('a'));
        assertNull(cmd.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        // Test case 1: Typical input -> option exists with argument
        CommandLine cmd = new CommandLine();
        Option option = new Option("o");
        option.addValue("value");
        cmd.addOption(option);
        assertEquals("value", cmd.getOptionValue("o", "default"));

        // Test case 2: Typical input -> option exists without argument
        cmd.addOption(new Option("a"));
        assertEquals("default", cmd.getOptionValue("a", "default"));

        // Test case 3: Empty input
        assertEquals("default", cmd.getOptionValue("", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefaultValue() {
        // Test case 1: Typical input -> option exists with argument
        CommandLine cmd = new CommandLine();
        Option option = new Option('o');
        option.addValue("value");
        cmd.addOption(option);
        assertEquals("value", cmd.getOptionValue('o', "default"));

        // Test case 2: Typical input -> option exists without argument
        cmd.addOption(new Option('a'));
        assertEquals("default", cmd.getOptionValue('a', "default"));
    }

    @Test
    public void testGetArgs() {
        // Test case 1: Typical input -> multiple arguments
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());

        // Test case 2: Typical input -> single argument
        CommandLine cmd2 = new CommandLine();
        cmd2.addArg("arg");
        assertArrayEquals(new String[]{"arg"}, cmd2.getArgs());

        // Test case 3: Empty input
        assertArrayEquals(new String[]{}, new CommandLine().getArgs());
    }

    @Test
    public void testGetArgList() {
        // Test case 1: Typical input -> multiple arguments
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals(cmd.getArgList().size(), 2);

        // Test case 2: Typical input -> single argument
        CommandLine cmd2 = new CommandLine();
        cmd2.addArg("arg");
        assertEquals(cmd2.getArgList().size(), 1);

        // Test case 3: Empty input
        assertEquals(new CommandLine().getArgList().size(), 0);
    }

    @Test
    public void testAddArg() {
        // Test case 1: Typical input -> add multiple arguments
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals(cmd.getArgList().size(), 2);

        // Test case 2: Typical input -> add single argument
        CommandLine cmd2 = new CommandLine();
        cmd2.addArg("arg");
        assertEquals(cmd2.getArgList().size(), 1);

        // Test case 3: Empty input
        CommandLine cmd3 = new CommandLine();
        cmd3.addArg("");
        assertEquals(cmd3.getArgList().size(), 1);
    }

    @Test
    public void testAddOption() {
        // Test case 1: Typical input -> add multiple options
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o1"));
        cmd.addOption(new Option("o2"));
        assertEquals(cmd.getOptions().length, 2);

        // Test case 2: Typical input -> add single option
        CommandLine cmd2 = new CommandLine();
        cmd2.addOption(new Option("o"));
        assertEquals(cmd2.getOptions().length, 1);

        // Test case 3: Empty input
        CommandLine cmd3 = new CommandLine();
        cmd3.addOption(new Option(""));
        assertEquals(cmd3.getOptions().length, 1);
    }

    @Test
    public void testIterator() {
        // Test case 1: Typical input -> multiple options added
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o1"));
        cmd.addOption(new Option("o2"));
        assertEquals(cmd.iterator().hasNext(), true);

        // Test case 2: Typical input -> single option added
        CommandLine cmd2 = new CommandLine();
        cmd2.addOption(new Option("o"));
        assertEquals(cmd2.iterator().hasNext(), true);

        // Test case 3: Empty input
        CommandLine cmd3 = new CommandLine();
        assertEquals(cmd3.iterator().hasNext(), false);
    }

    @Test
    public void testGetOptions() {
        // Test case 1: Typical input -> multiple options added
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o1"));
        cmd.addOption(new Option("o2"));
        assertEquals(cmd.getOptions().length, 2);

        // Test case 2: Typical input -> single option added
        CommandLine cmd2 = new CommandLine();
        cmd2.addOption(new Option("o"));
        assertEquals(cmd2.getOptions().length, 1);

        // Test case 3: Empty input
        CommandLine cmd3 = new CommandLine();
        assertEquals(cmd3.getOptions().length, 0);
    }
}