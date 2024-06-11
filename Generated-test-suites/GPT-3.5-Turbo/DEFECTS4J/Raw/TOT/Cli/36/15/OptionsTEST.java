import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        
        // Test adding option group with required option
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("a", "opt1", true, "Option 1");
        group1.addOption(option1);
        options.addOptionGroup(group1);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("opt1"));
        assertTrue(options.getRequiredOptions().contains("a"));
        assertEquals(group1, options.getOptionGroup(option1));
        
        // Test adding option group with non-required option
        OptionGroup group2 = new OptionGroup();
        Option option2 = new Option("b", "opt2", false, "Option 2");
        group2.addOption(option2);
        options.addOptionGroup(group2);
        
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("opt2"));
        assertFalse(options.getRequiredOptions().contains("b"));
        assertEquals(group2, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        
        // Test adding option without argument
        options.addOption("a", "Option a");
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("Option a"));
        assertFalse(options.hasShortOption("Option a"));
        
        // Test adding option with argument
        options.addOption("b", true, "Option b");
        
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("Option b"));
        assertTrue(options.hasShortOption("Option b"));
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        
        // Test retrieving options
        options.addOption("a", "Option a");
        options.addOption("b", true, "Option b");
        
        assertEquals(2, options.getOptions().size());
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        
        // Test retrieving option by short name
        options.addOption("a", "Option a");
        options.addOption("b", true, "Option b");
        
        assertNotNull(options.getOption("a"));
        assertNotNull(options.getOption("b"));
        
        // Test retrieving option by long name
        assertNotNull(options.getOption("Option a"));
        assertNotNull(options.getOption("Option b"));
        
        // Test retrieving non-existent option
        assertNull(options.getOption("c"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        // Test retrieving matching options
        options.addOption("a", "Option a");
        options.addOption("b", true, "Option b");
        
        assertEquals(1, options.getMatchingOptions("Op").size());
        assertEquals(2, options.getMatchingOptions("O").size());
        assertFalse(options.getMatchingOptions("Op").contains("b"));
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        // Test checking if option exists by short name
        options.addOption("a", "Option a");
        options.addOption("b", true, "Option b");
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        
        // Test checking if option exists by long name
        assertTrue(options.hasOption("Option a"));
        assertTrue(options.hasOption("Option b"));
        
        // Test checking if non-existent option exists
        assertFalse(options.hasOption("c"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        
        // Test checking if long option exists
        options.addOption("a", "Option a");
        options.addOption("b", true, "Option b");
        
        assertTrue(options.hasLongOption("Option a"));
        assertTrue(options.hasLongOption("Option b"));
        
        // Test checking if non-existent long option exists
        assertFalse(options.hasLongOption("Option c"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        
        // Test checking if short option exists
        options.addOption("a", "Option a");
        options.addOption("b", true, "Option b");
        
        assertTrue(options.hasShortOption("Option a"));
        assertFalse(options.hasShortOption("Option b"));
        
        // Test checking if non-existent short option exists
        assertFalse(options.hasShortOption("Option c"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        // Test getting option group
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("a", "opt1", true, "Option 1");
        group1.addOption(option1);
        options.addOptionGroup(group1);
        
        assertEquals(group1, options.getOptionGroup(option1));
        
        // Test getting non-existent option group
        Option option2 = new Option("b", "opt2", false, "Option 2");
        assertNull(options.getOptionGroup(option2));
    }
    
}