import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));
        cmd.addOption(new Option("opt", "test option"));
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('o'));
        cmd.addOption(new Option('o', "test option"));
        assertTrue(cmd.hasOption('o'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("opt"));
        cmd.addOption(new Option("opt", "test option"));
        assertNotNull(cmd.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject('o'));
        cmd.addOption(new Option('o', "test option"));
        assertNotNull(cmd.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("opt"));
        cmd.addOption(new Option("opt", "test option"));
        assertNull(cmd.getOptionValue("opt"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('o'));
        cmd.addOption(new Option('o', "test option"));
        assertNull(cmd.getOptionValue('o'));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("opt"));
        cmd.addOption(new Option("opt", "test option"));
        assertNull(cmd.getOptionValues("opt"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues('o'));
        cmd.addOption(new Option('o', "test option"));
        assertNull(cmd.getOptionValues('o'));
        cmd.addArg("value1");
        cmd.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValue_String_defaultValue() {
        CommandLine cmd = new CommandLine();
        assertEquals("default", cmd.getOptionValue("opt", "default"));
        cmd.addOption(new Option("opt", "test option"));
        assertEquals("default", cmd.getOptionValue("opt", "default"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("opt", "default"));
    }

    @Test
    public void testGetOptionValue_char_defaultValue() {
        CommandLine cmd = new CommandLine();
        assertEquals("default", cmd.getOptionValue('o', "default"));
        cmd.addOption(new Option('o', "test option"));
        assertEquals("default", cmd.getOptionValue('o', "default"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue('o', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assertArrayEquals(new String[]{}, cmd.getArgs());
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgList().size());
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgList().size());
        assertTrue(cmd.getArgList().contains("arg1"));
        assertTrue(cmd.getArgList().contains("arg2"));
    }

}