import static org.junit.Assert.*;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple", false, "apple option"));

        assertFalse(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "orange", true, "orange option"));
        cmd.addArg("argument");

        assertEquals("argument", cmd.getOptionValue("o"));
        assertNull(cmd.getOptionValue("p"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNull() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(null);
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("g", "grape", true, "grape option", new String[]{"value1", "value2"}));

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("g"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmd.getArgs());
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "tomato", true, "tomato option", Integer.class));

        assertEquals(Integer.valueOf(10), cmd.getOptionObject("t"));
        assertNull(cmd.getOptionObject("c"));
    }

}