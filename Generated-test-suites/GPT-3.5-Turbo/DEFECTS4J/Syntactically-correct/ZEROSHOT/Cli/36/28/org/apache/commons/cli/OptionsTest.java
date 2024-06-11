import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {
    
    private Options options;
    
    @Before
    public void setUp() {
        options = new Options();
    }
    
    @After
    public void tearDown() {
        options = null;
    }
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", false, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testAddOptionWithLongOpt() {
        Option option = new Option("a", "optionA", false, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("optionA"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("optionA"));
    }
    
    @Test
    public void testAddOptionWithArg() {
        Option option = new Option("a", true, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(option.hasArg());
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testAddOptionWithLongOptAndArg() {
        Option option = new Option("a", "optionA", true, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(option.hasArg());
        assertTrue(options.hasLongOption("optionA"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("optionA"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");
        
        options.addOption(option1);
        options.addOption(option2);
        options.getOptionGroup(option1).setRequired(true);
        options.getOptionGroup(option2).setRequired(true);
        
        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option optionA = new Option("a", false, "Option a");
        Option optionAB1 = new Option("ab1", false, "Option ab1");
        Option optionAB2 = new Option("ab2", false, "Option ab2");
        
        options.addOption(optionA);
        options.addOption(optionAB1);
        options.addOption(optionAB2);
        
        assertEquals(1, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        
        assertEquals(2, options.getMatchingOptions("ab").size());
        assertTrue(options.getMatchingOptions("ab").contains("ab1"));
        assertTrue(options.getMatchingOptions("ab").contains("ab2"));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("a", false, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "optionA", false, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("optionA"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "optionA", false, "Option a");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasShortOption("a"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", false, "Option a");
        group.addOption(option);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("a", false, "Option a");
        
        options.addOption(option);
        
        String expected = "[ Options: [ short {a=Option a} ] [ long {} ]";
        assertEquals(expected, options.toString());
    }
}