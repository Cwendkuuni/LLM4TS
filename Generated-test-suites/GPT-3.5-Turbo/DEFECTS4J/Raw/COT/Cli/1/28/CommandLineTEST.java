import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        assertTrue(cmd.hasOption("t"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        assertTrue(cmd.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test").setType(Integer.class));
        cmd.addArg("123");
        assertEquals(Integer.valueOf(123), cmd.getOptionObject("t"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test").setType(Integer.class));
        cmd.addArg("123");
        assertEquals(Integer.valueOf(123), cmd.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("t"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("t"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValue_defaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        assertEquals("default", cmd.getOptionValue("t", "default"));
    }

    @Test
    public void testGetOptionValue_char_defaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test"));
        assertEquals("default", cmd.getOptionValue('t', "default"));
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
    }

    // Additional Test Cases for Potential Edge Cases and Exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateKey() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test1"));
        cmd.addOption(new Option("t", "test2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateKey_longOpt() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("t", "test1"));
        cmd.addOption(new Option("t2", "test1"));
    }

}