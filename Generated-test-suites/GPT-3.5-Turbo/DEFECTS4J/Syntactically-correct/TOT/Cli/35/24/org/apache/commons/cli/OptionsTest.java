import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionsTest {
    
    private Options options;
    
    @Before
    public void setUp() {
        options = new Options();
    }
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
        assertTrue(options.getOption("a") == option1);
        assertTrue(options.getOption("b") == option2);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(option1.isRequired());
        assertFalse(option2.isRequired());
    }
    
    @Test
    public void testAddOptionStringString() {
        options.addOption("a", "description");
        
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testAddOptionStringBooleanString() {
        options.addOption("a", true, "description");
        
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testAddOptionStringStringBooleanString() {
        options.addOption("a", "apple", true, "description");
        
        Option option = options.getOption("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("apple", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testAddOptionOption() {
        Option option = new Option("a", "description");
        options.addOption(option);
        
        Option resultOption = options.getOption("a");
        assertNotNull(resultOption);
        assertEquals(option, resultOption);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        Collection<Option> resultOptions = options.getOptions();
        
        assertEquals(2, resultOptions.size());
        assertTrue(resultOptions.contains(option1));
        assertTrue(resultOptions.contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "apple", false, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        
        List requiredOptions = options.getRequiredOptions();
        
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("a", "apple", false, "description");
        options.addOption(option);
        
        Option resultOption1 = options.getOption("a");
        Option resultOption2 = options.getOption("apple");
        Option resultOption3 = options.getOption("b");
        
        assertNotNull(resultOption1);
        assertNotNull(resultOption2);
        assertNull(resultOption3);
        assertEquals(option, resultOption1);
        assertEquals(option, resultOption2);
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("apple", false, "description1");
        Option option2 = new Option("banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        List<String> resultOptions1 = options.getMatchingOptions("a");
        List<String> resultOptions2 = options.getMatchingOptions("b");
        
        assertEquals(1, resultOptions1.size());
        assertEquals(1, resultOptions2.size());
        assertTrue(resultOptions1.contains("apple"));
        assertTrue(resultOptions2.contains("banana"));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("a", "apple", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertFalse(options.hasOption("b"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "apple", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasLongOption("a"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "apple", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("apple"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        OptionGroup resultGroup1 = options.getOptionGroup(option1);
        OptionGroup resultGroup2 = options.getOptionGroup(option2);
        
        assertNotNull(resultGroup1);
        assertNotNull(resultGroup2);
        assertEquals(group, resultGroup1);
        assertEquals(group, resultGroup2);
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        
        String result = options.toString();
        
        assertTrue(result.contains("Options:"));
        assertTrue(result.contains("short {a=Option(opt=a, longOpt=apple, hasArg=false, description=description1), b=Option(opt=b, longOpt=banana, hasArg=false, description=description2)}"));
        assertFalse(result.contains("long"));
    }
}