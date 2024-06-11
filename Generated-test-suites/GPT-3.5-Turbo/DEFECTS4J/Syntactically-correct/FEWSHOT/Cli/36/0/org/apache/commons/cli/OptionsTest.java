import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    
    private Options options;
    
    @Before
    public void setUp() {
        options = new Options();
    }
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        
        Option option1 = new Option("a", "aa", false, "Option 1");
        Option option2 = new Option("b", "bb", false, "Option 2");
        
        group1.addOption(option1);
        group1.addOption(option2);
        
        options.addOptionGroup(group1);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.getOptionGroup(option1).equals(group1));
        assertTrue(options.getOptionGroup(option2).equals(group1));
    }
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(options.getOptions().size(), 2);
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        options.getOptionGroup(option1).setRequired(true);
        
        assertEquals(options.getRequiredOptions().size(), 1);
        assertTrue(options.getRequiredOptions().contains(option1));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(options.getOption("a"), option1);
        assertEquals(options.getOption("b"), option2);
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("abc", false, "Option 2");
        Option option3 = new Option("bcd", false, "Option 3");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("ab").contains("abc"));
        assertTrue(options.getMatchingOptions("bcd").contains("bcd"));
        assertFalse(options.getMatchingOptions("c").contains("abcd"));
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "aa", false, "Option 1");
        Option option2 = new Option("b", "bb", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("aa"));
        assertTrue(options.hasLongOption("bb"));
        assertFalse(options.hasLongOption("cc"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "aa", false, "Option 1");
        Option option2 = new Option("b", "bb", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }
}