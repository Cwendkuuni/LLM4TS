import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_withExistingOption_returnsTrue() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "optionDescription"));
        assertTrue(cmdLine.hasOption("opt"));
    }
    
    @Test
    public void testHasOption_withNonExistingOption_returnsFalse() {
        CommandLine cmdLine = new CommandLine();
        assertFalse(cmdLine.hasOption("opt"));
    }
    
    @Test
    public void testHasOption_withExistingOption_returnsTrue() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "optionDescription"));
        assertTrue(cmdLine.hasOption('o'));
    }
    
    @Test
    public void testHasOption_withNonExistingOption_returnsFalse() {
        CommandLine cmdLine = new CommandLine();
        assertFalse(cmdLine.hasOption('o'));
    }
    
    @Test
    public void testGetOptionValue_withExistingOptionAndValue_returnsValue() {
        CommandLine cmdLine = new CommandLine();
        Option option = new Option("opt", "optionDescription");
        option.addValue("value");
        cmdLine.addOption(option);
        assertEquals("value", cmdLine.getOptionValue("opt"));
    }
    
    @Test
    public void testGetOptionValue_withExistingOptionAndNoValue_returnsNull() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "optionDescription"));
        assertNull(cmdLine.getOptionValue("opt"));
    }
    
    @Test
    public void testGetOptionValue_withNonExistingOption_returnsNull() {
        CommandLine cmdLine = new CommandLine();
        assertNull(cmdLine.getOptionValue("opt"));
    }
    
    @Test
    public void testGetOptionObject_withExistingOption_returnsObject() {
        CommandLine cmdLine = new CommandLine();
        Option option = new Option("opt", "optionDescription");
        option.setType(Integer.class);
        option.addValue("100");
        cmdLine.addOption(option);
        assertEquals(Integer.valueOf(100), cmdLine.getOptionObject("opt"));
    }
    
    @Test
    public void testGetOptionObject_withNonExistingOption_returnsNull() {
        CommandLine cmdLine = new CommandLine();
        assertNull(cmdLine.getOptionObject("opt"));
    }
    
    @Test
    public void testGetOptionValue_withNonExistingOptionAndDefaultValue_returnsDefaultValue() {
        CommandLine cmdLine = new CommandLine();
        assertEquals("defaultValue", cmdLine.getOptionValue("opt", "defaultValue"));
    }
    
    @Test
    public void testGetOptionValue_withExistingOptionAndDefaultValue_returnsValue() {
        CommandLine cmdLine = new CommandLine();
        Option option = new Option("opt", "optionDescription");
        option.addValue("value");
        cmdLine.addOption(option);
        assertEquals("value", cmdLine.getOptionValue("opt", "defaultValue"));
    }
    
    @Test
    public void testGetOptionValue_withNonExistingOptionAndDefaultValue_returnsDefaultValue() {
        CommandLine cmdLine = new CommandLine();
        assertNull(cmdLine.getOptionValue("opt", null));
    }
    
    @Test
    public void testGetArgs_withAddedArgs_returnsArgs() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmdLine.getArgs());
    }
    
    @Test
    public void testGetArgList_withAddedArgs_returnsArgList() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(expected, cmdLine.getArgList());
    }
    
    @Test
    public void testGetOptionValues_withExistingOptionAndValues_returnsValues() {
        CommandLine cmdLine = new CommandLine();
        Option option = new Option("opt", "optionDescription");
        option.addValue("value1");
        option.addValue("value2");
        cmdLine.addOption(option);
        String[] expected = {"value1", "value2"};
        assertArrayEquals(expected, cmdLine.getOptionValues("opt"));
    }
    
    @Test
    public void testGetOptionValues_withExistingOptionAndNoValues_returnsNull() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "optionDescription"));
        assertNull(cmdLine.getOptionValues("opt"));
    }
    
    @Test
    public void testGetOptionValues_withNonExistingOption_returnsNull() {
        CommandLine cmdLine = new CommandLine();
        assertNull(cmdLine.getOptionValues("opt"));
    }
    
    @Test
    public void testGetOptions_withAddedOptions_returnsOptions() {
        CommandLine cmdLine = new CommandLine();
        Option option1 = new Option("opt1", "option1Description");
        Option option2 = new Option("opt2", "option2Description");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        Option[] expected = {option1, option2};
        assertArrayEquals(expected, cmdLine.getOptions());
    }
    
}