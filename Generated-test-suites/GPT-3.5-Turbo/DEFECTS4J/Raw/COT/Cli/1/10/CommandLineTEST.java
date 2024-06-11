import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("h", "help", false, "display help message"));
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("h", "help", false, "display help message"));
        assertTrue(cmd.hasOption('h'));
        assertFalse(cmd.hasOption('v'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test.txt");
        assertEquals("test.txt", cmd.getOptionObject("f"));
        assertNull(cmd.getOptionObject("h"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test.txt");
        assertEquals("test.txt", cmd.getOptionObject('f'));
        assertNull(cmd.getOptionObject('h'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test.txt");
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("h"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test.txt");
        assertEquals("test.txt", cmd.getOptionValue('f'));
        assertNull(cmd.getOptionValue('h'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test1.txt");
        cmd.addArg("test2.txt");
        String[] expected = {"test1.txt", "test2.txt"};
        assertArrayEquals(expected, cmd.getOptionValues("f"));
        assertNull(cmd.getOptionValues("h"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test1.txt");
        cmd.addArg("test2.txt");
        String[] expected = {"test1.txt", "test2.txt"};
        assertArrayEquals(expected, cmd.getOptionValues('f'));
        assertNull(cmd.getOptionValues('h'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test.txt");
        assertEquals("test.txt", cmd.getOptionValue("f", "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue("h", "default.txt"));
    }

    @Test
    public void testGetOptionValue_char_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test.txt");
        assertEquals("test.txt", cmd.getOptionValue('f', "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue('h', "default.txt"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test1.txt");
        cmd.addArg("test2.txt");
        String[] expected = {"test1.txt", "test2.txt"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        cmd.addArg("test1.txt");
        cmd.addArg("test2.txt");
        List<String> expected = new ArrayList<>();
        expected.add("test1.txt");
        expected.add("test2.txt");
        assertEquals(expected, cmd.getArgList());
    }

    // Additional tests for edge cases and exception handling scenarios identified in the code

    @Test
    public void testGetOptionObject_NonExistentOption() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("h"));
    }

    @Test
    public void testGetOptionObject_NullOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        assertNull(cmd.getOptionObject("f"));
    }

    @Test
    public void testGetOptionValues_NonExistentOption() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("h"));
    }

    @Test
    public void testGetOptionValue_DefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "input file"));
        assertNull(cmd.getOptionValue("f", "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue("h", "default.txt"));
    }

}