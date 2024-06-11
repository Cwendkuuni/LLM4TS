import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("a"));

        cmd.addOption(new Option("a", "option a"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("a"));

        Option option = new Option("a", Option.OBJECT_TYPE, "option a");
        cmd.addOption(option);
        assertNull(cmd.getOptionObject("a"));

        option.addValue("value");
        assertNotNull(cmd.getOptionObject("a"));
        assertEquals("value", cmd.getOptionObject("a"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a"));

        cmd.addOption(new Option("a", "option a"));
        assertNull(cmd.getOptionValue("a"));

        cmd.addOption(new Option("b", Option.REQUIRED_ARGUMENT));
        assertNull(cmd.getOptionValue("b"));

        cmd.addOptionValue("b", "value");
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("a"));

        cmd.addOption(new Option("a", "option a"));
        assertNull(cmd.getOptionValues("a"));

        cmd.addOption(new Option("b", Option.MULTIPLE_VALUES));
        assertNull(cmd.getOptionValues("b"));

        cmd.addOptionValue("b", "value1");
        cmd.addOptionValue("b", "value2");
        assertEquals(2, cmd.getOptionValues("b").length);
        assertArrayEquals(new String[] {"value1", "value2"}, cmd.getOptionValues("b"));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a", "default"));

        cmd.addOption(new Option("a", "option a"));
        assertNull(cmd.getOptionValue("a", "default"));

        cmd.addOption(new Option("b", Option.REQUIRED_ARGUMENT));
        assertNull(cmd.getOptionValue("b", "default"));

        cmd.addOptionValue("b", "value");
        assertEquals("value", cmd.getOptionValue("b", "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgs().length);

        cmd.addArg("arg1");
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgList().size());

        cmd.addArg("arg1");
        assertEquals(1, cmd.getArgList().size());
        assertTrue(cmd.getArgList().contains("arg1"));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgs().length);

        cmd.addArg("arg1");
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getOptions().length);

        Option option = new Option("a", "option a");
        cmd.addOption(option);
        assertEquals(1, cmd.getOptions().length);
        assertSame(option, cmd.getOptions()[0]);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.iterator().hasNext());

        Option option = new Option("a", "option a");
        cmd.addOption(option);
        assertTrue(cmd.iterator().hasNext());
        assertSame(option, cmd.iterator().next());
    }
}