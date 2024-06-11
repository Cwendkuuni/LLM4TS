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
    public void testHasOption_WithExistingOption_ReturnsTrue() {
        commandLine.addOption(new Option("a", "add", false, "Add option"));
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_WithNonExistingOption_ReturnsFalse() {
        assertFalse(commandLine.hasOption("x"));
    }

    @Test
    public void testHasOption_WithOptionSetUsingChar_ReturnsTrue() {
        commandLine.addOption(new Option("a", "add", false, "Add option"));
        assertTrue(commandLine.hasOption('a'));
    }

    @Test
    public void testHasOption_WithNonExistingOptionChar_ReturnsFalse() {
        assertFalse(commandLine.hasOption('x'));
    }

    @Test
    public void testGetOptionObject_WithExistingOption_ReturnsOptionObject() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        assertEquals(opt, commandLine.getOptionObject("f"));
    }

    @Test
    public void testGetOptionObject_WithNonExistingOption_ReturnsNull() {
        assertNull(commandLine.getOptionObject("x"));
    }

    @Test
    public void testGetOptionObject_WithExistingOptionChar_ReturnsOptionObject() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        assertEquals(opt, commandLine.getOptionObject('f'));
    }

    @Test
    public void testGetOptionObject_WithNonExistingOptionChar_ReturnsNull() {
        assertNull(commandLine.getOptionObject('x'));
    }

    @Test
    public void testGetOptionValue_WithExistingOption_ReturnsOptionValue() {
        commandLine.addOption(new Option("f", "file", true, "File option"));
        commandLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", commandLine.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValue_WithNonExistingOption_ReturnsNull() {
        assertNull(commandLine.getOptionValue("x"));
    }

    @Test
    public void testGetOptionValue_WithExistingOptionChar_ReturnsOptionValue() {
        commandLine.addOption(new Option("f", "file", true, "File option"));
        commandLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", commandLine.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValue_WithNonExistingOptionChar_ReturnsNull() {
        assertNull(commandLine.getOptionValue('x'));
    }

    @Test
    public void testGetOptionValues_WithExistingOption_ReturnsOptionValues() {
        commandLine.addOption(new Option("f", "file", true, "File option"));
        commandLine.addOptionValue("f", "test1.txt");
        commandLine.addOptionValue("f", "test2.txt");
        assertArrayEquals(new String[]{"test1.txt", "test2.txt"}, commandLine.getOptionValues("f"));
    }

    @Test
    public void testGetOptionValues_WithNonExistingOption_ReturnsNull() {
        assertNull(commandLine.getOptionValues("x"));
    }

    @Test
    public void testGetOptionValues_WithExistingOptionChar_ReturnsOptionValues() {
        commandLine.addOption(new Option("f", "file", true, "File option"));
        commandLine.addOptionValue("f", "test1.txt");
        commandLine.addOptionValue("f", "test2.txt");
        assertArrayEquals(new String[]{"test1.txt", "test2.txt"}, commandLine.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValues_WithNonExistingOptionChar_ReturnsNull() {
        assertNull(commandLine.getOptionValues('x'));
    }

    @Test
    public void testGetOptionValue_WithExistingOptionAndDefaultValue_ReturnsOptionValue() {
        commandLine.addOption(new Option("f", "file", true, "File option"));
        commandLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", commandLine.getOptionValue("f", "default.txt"));
    }

    @Test
    public void testGetOptionValue_WithNonExistingOptionAndDefaultValue_ReturnsDefaultValue() {
        assertEquals("default.txt", commandLine.getOptionValue("x", "default.txt"));
    }

    @Test
    public void testGetOptionValue_WithExistingOptionCharAndDefaultValue_ReturnsOptionValue() {
        commandLine.addOption(new Option("f", "file", true, "File option"));
        commandLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", commandLine.getOptionValue('f', "default.txt"));
    }

    @Test
    public void testGetOptionValue_WithNonExistingOptionCharAndDefaultValue_ReturnsDefaultValue() {
        assertEquals("default.txt", commandLine.getOptionValue('x', "default.txt"));
    }

    @Test
    public void testGetArgs_WithNoArgs_ReturnsEmptyArray() {
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgs_WithArgs_ReturnsArgsArray() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList_WithNoArgs_ReturnsEmptyList() {
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testGetArgList_WithArgs_ReturnsArgsList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
        assertEquals("arg2", commandLine.getArgList().get(1));
    }

    @Test
    public void testIterator_WithNoOptions_ReturnsEmptyIterator() {
        assertFalse(commandLine.iterator().hasNext());
    }

    @Test
    public void testIterator_WithOption_ReturnsIteratorWithOption() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        assertTrue(commandLine.iterator().hasNext());
        assertEquals(opt, commandLine.iterator().next());
    }

    @Test
    public void testGetOptions_WithNoOptions_ReturnsEmptyArray() {
        assertArrayEquals(new Option[]{}, commandLine.getOptions());
    }

    @Test
    public void testGetOptions_WithOptions_ReturnsOptionsArray() {
        Option opt1 = new Option("f", "file", true, "File option");
        Option opt2 = new Option("d", "directory", false, "Directory option");
        commandLine.addOption(opt1);
        commandLine.addOption(opt2);
        assertArrayEquals(new Option[]{opt1, opt2}, commandLine.getOptions());
    }

}