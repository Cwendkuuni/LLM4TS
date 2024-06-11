import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        
        group.addOption(new Option("a", "aaa", false, "Option AAA"));
        group.addOption(new Option("b", "bbb", true, "Option BBB"));
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }
    
    @Test
    public void testAddOption_shortNameOnly() {
        Options options = new Options();
        
        options.addOption("a", "Option A");
        
        assertNotNull(options.getOption("a"));
    }
    
    @Test
    public void testAddOption_shortNameHasArg() {
        Options options = new Options();
        
        options.addOption("a", true, "Option A");
        
        assertTrue(options.getOption("a").hasArg());
    }
    
    @Test
    public void testAddOption_shortAndLongNames() {
        Options options = new Options();
        
        options.addOption("a", "aaa", true, "Option A with long name AAA");
        
        assertNotNull(options.getOption("a"));
        assertNotNull(options.getOption("aaa"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        options.addOption("apple", "Option Apple");
        options.addOption("banana", "Option Banana");
        options.addOption("cherry", "Option Cherry");
        
        assertEquals(2, options.getMatchingOptions("b").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        options.addOption("a", "Option A");
        
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        
        options.addOption("a", "Option A");
        options.addOption("b", "Option B");
        
        assertEquals("[ Options: [ short {a=Option A, b=Option B} ] [ long {} ]", options.toString());
    }
}