import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));

        cmd.addOption(new Option("opt", false, "option description"));
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("unknown"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('o'));

        cmd.addOption(new Option("opt", false, "option description"));
        assertTrue(cmd.hasOption('o'));
        assertFalse(cmd.hasOption('u'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("opt"));

        cmd.addOption(new Option("opt", false, "option description"));
        assertNotNull(cmd.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject('o'));

        cmd.addOption(new Option("opt", false, "option description"));
        assertNotNull(cmd.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("opt"));

        cmd.addOption(new Option("opt", true, "option description"));
        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('o'));

        cmd.addOption(new Option("opt", true, "option description"));
        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("opt"));

        cmd.addOption(new Option("opt", true, "option description"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        cmd.addArg("value3");

        assertArrayEquals(new String[]{"value1", "value2", "value3"}, cmd.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues('o'));

        cmd.addOption(new Option("opt", true, "option description"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        cmd.addArg("value3");

        assertArrayEquals(new String[]{"value1", "value2", "value3"}, cmd.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("opt", "default"));

        cmd.addOption(new Option("opt", true, "option description"));
        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue("opt", "default"));
        assertEquals("default", cmd.getOptionValue("unknown", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('o', "default"));

        cmd.addOption(new Option("opt", true, "option description"));
        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue('o', "default"));
        assertEquals("default", cmd.getOptionValue('u', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assertArrayEquals(new String[]{}, cmd.getArgs());

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgList().size());

        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        assertEquals(3, cmd.getArgList().size());
    }
}