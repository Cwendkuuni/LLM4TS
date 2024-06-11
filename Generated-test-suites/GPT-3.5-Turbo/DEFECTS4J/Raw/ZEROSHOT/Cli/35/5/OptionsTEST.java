import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "alpha", false, "Option A");
        group.addOption(option);
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "alpha", false, "Option A");
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("alpha"));
        assertTrue(options.hasShortOption("a"));
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        Option option = new Option("a", "alpha", true, "Option A with argument");
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("alpha"));
        assertTrue(options.hasShortOption("a"));
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", true, "Option B with argument");
        options.addOption(option1);
        options.addOption(option2);
        options.getRequiredOptions().add("a");
        
        assertTrue(options.getRequiredOptions().contains("a"));
        assertFalse(options.getRequiredOptions().contains("b"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "Option A with argument");
        Option option2 = new Option("b", "banana", true, "Option B with argument");
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getMatchingOptions("app").size());
        assertTrue(options.getMatchingOptions("app").contains("apple"));
        assertEquals(1, options.getMatchingOptions("ban").size());
        assertTrue(options.getMatchingOptions("ban").contains("banana"));
        assertEquals(0, options.getMatchingOptions("grape").size());
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        options.addOption(option1);
        options.addOption(option2);
        
        String expected = "[ Options: [ short {a=Option[ opt: a  long opt: alpha has args: false description: Option A ]} ] [ long {alpha=Option[ opt: a  long opt: alpha has args: false description: Option A ], beta=Option[ opt: b  long opt: beta has args: false description: Option B ]} ]";
        
        assertEquals(expected, options.toString());
    }
    
}