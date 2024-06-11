import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption_True() {
        // Test when the option is set
        commandLine.addOption(new Option("a"));
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_False() {
        // Test when the option is not set
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_Char_True() {
        // Test when the option is set
        commandLine.addOption(new Option("a"));
        assertTrue(commandLine.hasOption('a'));
    }

    @Test
    public void testHasOption_Char_False() {
        // Test when the option is not set
        assertFalse(commandLine.hasOption('a'));
    }

    @Test
    public void testGetOptionValue_SingleValue() {
        // Test when the option has a single value
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value");
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValue_MultipleValues() {
        // Test when the option has multiple values
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value1");
        commandLine.getArgList().add("value2");
        assertEquals("value1", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValue_NoValue() {
        // Test when the option has no value
        commandLine.addOption(new Option("a", true));
        assertNull(commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValue_Char_SingleValue() {
        // Test when the option has a single value
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value");
        assertEquals("value", commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValue_Char_MultipleValues() {
        // Test when the option has multiple values
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value1");
        commandLine.getArgList().add("value2");
        assertEquals("value1", commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValue_Char_NoValue() {
        // Test when the option has no value
        commandLine.addOption(new Option("a", true));
        assertNull(commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValues_SingleValue() {
        // Test when the option has a single value
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value");
        assertArrayEquals(new String[]{"value"}, commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValues_MultipleValues() {
        // Test when the option has multiple values
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value1");
        commandLine.getArgList().add("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValues_NoValue() {
        // Test when the option has no value
        commandLine.addOption(new Option("a", true));
        assertNull(commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValues_Char_SingleValue() {
        // Test when the option has a single value
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value");
        assertArrayEquals(new String[]{"value"}, commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValues_Char_MultipleValues() {
        // Test when the option has multiple values
        commandLine.addOption(new Option("a", true));
        commandLine.getArgList().add("value1");
        commandLine.getArgList().add("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValues_Char_NoValue() {
        // Test when the option has no value
        commandLine.addOption(new Option("a", true));
        assertNull(commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValue_DefaultValue() {
        // Test when the option has no value, return default value
        commandLine.addOption(new Option("a", true));
        assertNull(commandLine.getOptionValue("a", "default"));
    }

    @Test
    public void testGetOptionValue_Char_DefaultValue() {
        // Test when the option has no value, return default value
        commandLine.addOption(new Option("a", true));
        assertNull(commandLine.getOptionValue('a', "default"));
    }

    @Test
    public void testGetArgs() {
        // Test retrieving remaining arguments
        commandLine.getArgList().add("arg1");
        commandLine.getArgList().add("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        // Test retrieving remaining arguments as a list
        commandLine.getArgList().add("arg1");
        commandLine.getArgList().add("arg2");
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().contains("arg1"));
        assertTrue(commandLine.getArgList().contains("arg2"));
    }

    @Test
    public void testAddArg() {
        // Test adding a new unrecognized argument
        commandLine.addArg("arg");
        assertEquals(1, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().contains("arg"));
    }

    @Test
    public void testAddOption() {
        // Test adding a new option
        Option option = new Option("a");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().length);
        assertEquals(option, commandLine.getOptions()[0]);
    }
}