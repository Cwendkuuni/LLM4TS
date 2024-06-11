import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    /*
     * Test cases for hasOption(String opt)
     */

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption_ValidOption_ReturnsTrue() {
        commandLine.addOption(new Option("f", "file", false, "input file"));
        assertTrue(commandLine.hasOption("f"));
    }

    @Test
    public void testHasOption_InvalidOption_ReturnsFalse() {
        assertFalse(commandLine.hasOption("invalid"));
    }

    /*
     * Test cases for hasOption(char opt)
     */

    @Test
    public void testHasOption_ValidCharOption_ReturnsTrue() {
        commandLine.addOption(new Option("f", "file", false, "input file"));
        assertTrue(commandLine.hasOption('f'));
    }

    @Test
    public void testHasOption_InvalidCharOption_ReturnsFalse() {
        assertFalse(commandLine.hasOption('i'));
    }

    /*
     * Test cases for getOptionObject(String opt)
     */

    @Test
    public void testGetOptionObject_ValidOption_ReturnsObject() {
        Option option = new Option("f", "file", true, "input file");
        option.setType(Integer.class);
        commandLine.addOption(option);
        commandLine.addArg("10");
        assertEquals(10, commandLine.getOptionObject("f"));
    }

    @Test
    public void testGetOptionObject_InvalidOption_ReturnsNull() {
        assertNull(commandLine.getOptionObject("invalid"));
    }

    /*
     * Test cases for getOptionObject(char opt)
     */

    @Test
    public void testGetOptionObject_ValidCharOption_ReturnsObject() {
        Option option = new Option("f", "file", true, "input file");
        option.setType(Double.class);
        commandLine.addOption(option);
        commandLine.addArg("3.14");
        assertEquals(3.14, commandLine.getOptionObject('f'));
    }

    @Test
    public void testGetOptionObject_InvalidCharOption_ReturnsNull() {
        assertNull(commandLine.getOptionObject('i'));
    }

    /*
     * Test cases for getOptionValue(String opt)
     */

    @Test
    public void testGetOptionValue_ValidOption_ReturnsValue() {
        commandLine.addOption(new Option("n", "name", true, "name"));
        commandLine.addArg("John");
        assertEquals("John", commandLine.getOptionValue("n"));
    }

    @Test
    public void testGetOptionValue_InvalidOption_ReturnsNull() {
        assertNull(commandLine.getOptionValue("invalid"));
    }

    /*
     * Test cases for getOptionValue(char opt)
     */

    @Test
    public void testGetOptionValue_ValidCharOption_ReturnsValue() {
        commandLine.addOption(new Option("n", "name", true, "name"));
        commandLine.addArg("John");
        assertEquals("John", commandLine.getOptionValue('n'));
    }

    @Test
    public void testGetOptionValue_InvalidCharOption_ReturnsNull() {
        assertNull(commandLine.getOptionValue('i'));
    }

    /*
     * Test cases for getOptionValues(String opt)
     */

    @Test
    public void testGetOptionValues_ValidOption_ReturnsValues() {
        commandLine.addOption(new Option("f", "file", true, "input file"));
        commandLine.addArg("file1.txt");
        commandLine.addArg("file2.txt");
        String[] expectedValues = {"file1.txt", "file2.txt"};
        assertArrayEquals(expectedValues, commandLine.getOptionValues("f"));
    }

    @Test
    public void testGetOptionValues_InvalidOption_ReturnsNull() {
        assertNull(commandLine.getOptionValues("invalid"));
    }

    /*
     * Test cases for getOptionValues(char opt)
     */

    @Test
    public void testGetOptionValues_ValidCharOption_ReturnsValues() {
        commandLine.addOption(new Option("f", "file", true, "input file"));
        commandLine.addArg("file1.txt");
        commandLine.addArg("file2.txt");
        String[] expectedValues = {"file1.txt", "file2.txt"};
        assertArrayEquals(expectedValues, commandLine.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValues_InvalidCharOption_ReturnsNull() {
        assertNull(commandLine.getOptionValues('i'));
    }

    /*
     * Test cases for getOptionValue(String opt, String defaultValue)
     */

    @Test
    public void testGetOptionValue_ValidOption_ReturnsValue() {
        commandLine.addOption(new Option("n", "name", true, "name"));
        commandLine.addArg("John");
        assertEquals("John", commandLine.getOptionValue("n", "default"));
    }

    @Test
    public void testGetOptionValue_InvalidOption_ReturnsDefault() {
        assertEquals("default", commandLine.getOptionValue("invalid", "default"));
    }

    /*
     * Test cases for getOptionValue(char opt, String defaultValue)
     */

    @Test
    public void testGetOptionValue_ValidCharOption_ReturnsValue() {
        commandLine.addOption(new Option("n", "name", true, "name"));
        commandLine.addArg("John");
        assertEquals("John", commandLine.getOptionValue('n', "default"));
    }

    @Test
    public void testGetOptionValue_InvalidCharOption_ReturnsDefault() {
        assertEquals("default", commandLine.getOptionValue('i', "default"));
    }

    /*
     * Test cases for getArgs()
     */

    @Test
    public void testGetArgs_NoArgs_ReturnsEmptyArray() {
        String[] expectedArgs = {};
        assertArrayEquals(expectedArgs, commandLine.getArgs());
    }

    @Test
    public void testGetArgs_WithArgs_ReturnsArray() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] expectedArgs = {"arg1", "arg2"};
        assertArrayEquals(expectedArgs, commandLine.getArgs());
    }

    /*
     * Test cases for getArgList()
     */

    @Test
    public void testGetArgList_NoArgs_ReturnsEmptyList() {
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testGetArgList_WithArgs_ReturnsList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List<String> expectedArgs = new ArrayList<>();
        expectedArgs.add("arg1");
        expectedArgs.add("arg2");
        assertEquals(expectedArgs, commandLine.getArgList());
    }

    /*
     * Test cases for addArg(String arg)
     */

    @Test
    public void testAddArg_ValidArg_ArgAddedToList() {
        commandLine.addArg("arg1");
        assertEquals("arg1", commandLine.getArgs()[0]);
    }

    /*
     * Test cases for addOption(Option opt)
     */

    @Test
    public void testAddOption_ValidOption_OptionAdded() {
        Option option = new Option("f", "file", false, "input file");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions()[0]);
    }

    /*
     * Test cases for iterator()
     */

    @Test
    public void testIterator_NoOptions_ReturnsEmptyIterator() {
        assertFalse(commandLine.iterator().hasNext());
    }

    @Test
    public void testIterator_WithOptions_ReturnsIterator() {
        Option option1 = new Option("f", "file", false, "input file");
        Option option2 = new Option("n", "name", true, "name");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /*
     * Test cases for getOptions()
     */

    @Test
    public void testGetOptions_NoOptions_ReturnsEmptyArray() {
        Option[] expectedOptions = {};
        assertArrayEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptions_WithOptions_ReturnsArray() {
        Option option1 = new Option("f", "file", false, "input file");
        Option option2 = new Option("n", "name", true, "name");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Option[] expectedOptions = {option1, option2};
        assertArrayEquals(expectedOptions, commandLine.getOptions());
    }

}