import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "apple", false, "Apple description");
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "apple", false, "Apple description");
        Option option2 = new Option("b", "banana", false, "Banana description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        options.addOptionGroup(optionGroup);
        
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "apple", false, "Apple description");
        options.addOption(option);
        
        Option retrievedOption = options.getOption("a");
        
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "apple", false, "Apple description");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "Apple description");
        options.addOption(option1);
        
        Option option2 = new Option("aa", "apricot", false, "Apricot description");
        options.addOption(option2);
        
        String partialName = "ap";
        List<String> matchingOptions = options.getMatchingOptions(partialName);
        
        assertTrue(matchingOptions.contains("apple"));
        assertTrue(matchingOptions.contains("apricot"));
    }

    @Test
    public void testAddOptionExceptionHandling() {
        try {
            options.addOption(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testRequiredOptions() {
        Option option = new Option("a", "apple", false, "Apple description");
        option.setRequired(true);
        
        options.addOption(option);
        
        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testOptionGroupRequired() {
        Option option1 = new Option("a", "apple", false, "Apple description");
        Option option2 = new Option("b", "banana", false, "Banana description");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        options.addOptionGroup(optionGroup);
        
        assertTrue(options.getRequiredOptions().contains(optionGroup));
    }

}