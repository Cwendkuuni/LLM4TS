import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", false, "an option without argument"));

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", false, "an option without argument"));

        assertTrue(cmd.hasOption('a'));
        assertFalse(cmd.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValue("optionA", "value");

        assertEquals("value", (String) cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject("b"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValue("optionA", "value");

        assertEquals("value", (String) cmd.getOptionObject('a'));
        assertNull(cmd.getOptionObject('b'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValue("optionA", "value");

        assertEquals("value", cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValue("optionA", "value");

        assertEquals("value", cmd.getOptionValue('a'));
        assertNull(cmd.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValues("optionA", new String[]{"value1", "value2"});

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValues("optionA", new String[]{"value1", "value2"});

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues('a'));
        assertNull(cmd.getOptionValues('b'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValue("optionA", "value");

        assertEquals("value", cmd.getOptionValue("a", "default"));
        assertEquals("default", cmd.getOptionValue("b", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        cmd.addOptionValue("optionA", "value");

        assertEquals("value", cmd.getOptionValue('a', "default"));
        assertEquals("default", cmd.getOptionValue('b', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));

        assertTrue(cmd.iterator().hasNext());
        assertEquals(Option.class, cmd.iterator().next().getClass());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", true, "an option with argument"));
        cmd.addOption(new Option("b", false, "an option without argument"));

        assertEquals(2, cmd.getOptions().length);
        assertEquals("a", cmd.getOptions()[0].getOpt());
        assertEquals("b", cmd.getOptions()[1].getOpt());
    }
}