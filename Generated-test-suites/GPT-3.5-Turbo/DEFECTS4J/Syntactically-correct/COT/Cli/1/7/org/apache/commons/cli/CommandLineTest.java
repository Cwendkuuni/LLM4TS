import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine cmd;

    @Before
    public void setUp() {
        cmd = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(cmd.hasOption("test"));

        cmd.addOption(new Option("test", false, "Test option"));

        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(cmd.hasOption('t'));

        cmd.addOption(new Option('t', "test", false, "Test option"));

        assertTrue(cmd.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(cmd.getOptionObject("test"));

        cmd.addOption(new Option("test", true, "Test option"));

        cmd.addArg("value");

        assertEquals("value", cmd.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObjectChar() {
        assertNull(cmd.getOptionObject('t'));

        cmd.addOption(new Option('t', "test", true, "Test option"));

        cmd.addArg("value");

        assertEquals("value", cmd.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(cmd.getOptionValue("test"));

        cmd.addOption(new Option("test", true, "Test option"));

        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(cmd.getOptionValue('t'));

        cmd.addOption(new Option('t', "test", true, "Test option"));

        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(cmd.getOptionValues("test"));

        cmd.addOption(new Option("test", true, "Test option"));

        cmd.addArg("value1");
        cmd.addArg("value2");

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("test"));
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(cmd.getOptionValues('t'));

        cmd.addOption(new Option('t', "test", true, "Test option"));

        cmd.addArg("value1");
        cmd.addArg("value2");

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        assertEquals("default", cmd.getOptionValue("test", "default"));

        cmd.addOption(new Option("test", true, "Test option"));

        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue("test", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        assertEquals("default", cmd.getOptionValue('t', "default"));

        cmd.addOption(new Option('t', "test", true, "Test option"));

        cmd.addArg("value");

        assertEquals("value", cmd.getOptionValue('t', "default"));
    }

    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[]{}, cmd.getArgs());

        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        assertEquals(0, cmd.getArgList().size());

        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }
}