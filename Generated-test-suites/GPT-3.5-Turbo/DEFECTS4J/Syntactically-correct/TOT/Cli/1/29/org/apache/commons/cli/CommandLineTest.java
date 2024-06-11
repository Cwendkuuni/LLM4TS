import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    // Test cases for hasOption(String opt) method

    @Test
    public void testHasOption_WithExistingOption_ReturnsTrue(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", false, "This is option A");
        cmd.addOption(option);

        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testHasOption_WithNonExistingOption_ReturnsFalse(){
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption("b"));
    }

    // Test cases for hasOption(char opt) method

    @Test
    public void testHasOption_Char_WithExistingOption_ReturnsTrue(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", false, "This is option A");
        cmd.addOption(option);

        assertTrue(cmd.hasOption('a'));
    }

    @Test
    public void testHasOption_Char_WithNonExistingOption_ReturnsFalse(){
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption('b'));
    }

    // Test cases for getOptionValue(String opt) method

    @Test
    public void testGetOptionValue_WithExistingOption_ReturnsValue(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", true, "This is option A");
        option.addValue("valueA");
        cmd.addOption(option);

        assertEquals("valueA", cmd.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValue_WithNonExistingOption_ReturnsNull(){
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValue("b"));
    }

    // Test cases for getOptionValue(char opt) method

    @Test
    public void testGetOptionValue_Char_WithExistingOption_ReturnsValue(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", true, "This is option A");
        option.addValue("valueA");
        cmd.addOption(option);

        assertEquals("valueA", cmd.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValue_Char_WithNonExistingOption_ReturnsNull(){
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValue('b'));
    }

    // Test cases for getOptionValues(String opt) method

    @Test
    public void testGetOptionValues_WithExistingOption_ReturnsValues(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", true, "This is option A");
        option.addValue("valueA");
        option.addValue("valueB");
        cmd.addOption(option);

        String[] expectedValues = {"valueA", "valueB"};
        assertArrayEquals(expectedValues, cmd.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValues_WithNonExistingOption_ReturnsNull(){
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValues("b"));
    }

    // Test cases for getOptionValues(char opt) method

    @Test
    public void testGetOptionValues_Char_WithExistingOption_ReturnsValues(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", true, "This is option A");
        option.addValue("valueA");
        option.addValue("valueB");
        cmd.addOption(option);

        String[] expectedValues = {"valueA", "valueB"};
        assertArrayEquals(expectedValues, cmd.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValues_Char_WithNonExistingOption_ReturnsNull(){
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValues('b'));
    }

    // Test cases for getOptionObject(String opt) method

    @Test
    public void testGetOptionObject_WithExistingOption_ReturnsParsedObject(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", true, "This is option A");
        option.addValue("10");
        cmd.addOption(option);

        Object expectedObject = 10;
        assertEquals(expectedObject, cmd.getOptionObject("a"));
    }

    @Test
    public void testGetOptionObject_WithNonExistingOption_ReturnsNull(){
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject("b"));
    }

    // Test cases for getOptionObject(char opt) method

    @Test
    public void testGetOptionObject_Char_WithExistingOption_ReturnsParsedObject(){
        CommandLine cmd = new CommandLine();

        // Add an option to the command line
        Option option = new Option("a", "optionA", true, "This is option A");
        option.addValue("10");
        cmd.addOption(option);

        Object expectedObject = 10;
        assertEquals(expectedObject, cmd.getOptionObject('a'));
    }

    @Test
    public void testGetOptionObject_Char_WithNonExistingOption_ReturnsNull(){
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject('b'));
    }

    // Test cases for getArgs() method

    @Test
    public void testGetArgs_WithExistingArguments_ReturnsArray(){
        CommandLine cmd = new CommandLine();

        // Add arguments to the command line
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] expectedArgs = {"arg1", "arg2"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
    }

    @Test
    public void testGetArgs_WithNoArguments_ReturnsEmptyArray(){
        CommandLine cmd = new CommandLine();

        String[] expectedArgs = {};
        assertArrayEquals(expectedArgs, cmd.getArgs());
    }

    // Test cases for getArgList() method

    @Test
    public void testGetArgList_WithExistingArguments_ReturnsList(){
        CommandLine cmd = new CommandLine();

        // Add arguments to the command line
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> expectedList = new ArrayList<>();
        expectedList.add("arg1");
        expectedList.add("arg2");
        assertEquals(expectedList, cmd.getArgList());
    }

    @Test
    public void testGetArgList_WithNoArguments_ReturnsEmptyList(){
        CommandLine cmd = new CommandLine();

        List<String> expectedList = new ArrayList<>();
        assertEquals(expectedList, cmd.getArgList());
    }

    // Test cases for addArg() method

    @Test
    public void testAddArg_WithArgument_AddsArgumentToList(){
        CommandLine cmd = new CommandLine();

        cmd.addArg("arg1");

        List<String> expectedList = new ArrayList<>();
        expectedList.add("arg1");
        assertEquals(expectedList, cmd.getArgList());
    }

    // Test cases for addOption() method

    @Test
    public void testAddOption_WithOption_AddsOptionToMap(){
        CommandLine cmd = new CommandLine();

        Option option = new Option("a", "optionA", false, "This is option A");
        cmd.addOption(option);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    // Test cases for iterator() method

    @Test
    public void testIterator_WithExistingOptions_ReturnsIterator(){
        CommandLine cmd = new CommandLine();

        Option option1 = new Option("a", "optionA", false, "This is option A");
        cmd.addOption(option1);

        Option option2 = new Option("b", "optionB", true, "This is option B");
        cmd.addOption(option2);

        Iterator<Option> iterator = cmd.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test cases for getOptions() method

    @Test
    public void testGetOptions_WithExistingOptions_ReturnsArray(){
        CommandLine cmd = new CommandLine();

        Option option1 = new Option("a", "optionA", false, "This is option A");
        cmd.addOption(option1);

        Option option2 = new Option("b", "optionB", true, "This is option B");
        cmd.addOption(option2);

        Option[] expectedOptions = {option1, option2};
        assertArrayEquals(expectedOptions, cmd.getOptions());
    }

    @Test
    public void testGetOptions_WithNoOptions_ReturnsEmptyArray(){
        CommandLine cmd = new CommandLine();

        Option[] expectedOptions = {};
        assertArrayEquals(expectedOptions, cmd.getOptions());
    }

}