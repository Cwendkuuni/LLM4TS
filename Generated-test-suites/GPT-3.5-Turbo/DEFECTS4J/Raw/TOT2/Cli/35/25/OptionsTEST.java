import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OptionsTest {
    
    private Options options;
    
    @Before
    public void setUp() {
        options = new Options();
    }
    
    // Testing addOptionGroup method
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Description for option a");
        Option option2 = new Option("b", "ball", false, "Description for option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }
    
    // Testing addOption method
    @Test
    public void testAddOption() {
        Option option = new Option("c", "cat", true, "Description for option c");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("c"));
        assertTrue(options.getOption("c").equals(option));
    }
    
    // Testing addOption with only short name
    @Test
    public void testAddOptionWithShortNameOnly() {
        options.addOption("d", "Description for option d");
        
        assertTrue(options.hasOption("d"));
    }
    
    // Testing addOption with short name and hasArg
    @Test
    public void testAddOptionWithHasArg() {
        options.addOption("e", true, "Description for option e");
        
        assertTrue(options.hasOption("e"));
        assertTrue(options.getOption("e").hasArg());
    }
    
    // Testing addOption with short name, long name, and hasArg
    @Test
    public void testAddOptionWithLongName() {
        options.addOption("f", "fish", true, "Description for option f");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("fish"));
        assertTrue(options.getOption("f").getLongOpt().equals("fish"));
    }
    
    // Testing getMatchingOptions
    @Test
    public void testGetMatchingOptions() {
        options.addOption("g", "goose", true, "Description for option g");
        options.addOption("h", "horse", true, "Description for option h");
        
        List<String> matchingOptions = options.getMatchingOptions("h");
        
        assertTrue(matchingOptions.size() == 1);
        assertTrue(matchingOptions.contains("horse"));
    }
    
    // Testing required options
    @Test
    public void testRequiredOptions() {
        Option option = new Option("i", "index", true, "Description for option i");
        option.setRequired(true);
        
        options.addOption(option);
        
        assertTrue(options.getRequiredOptions().contains(option));
    }
}