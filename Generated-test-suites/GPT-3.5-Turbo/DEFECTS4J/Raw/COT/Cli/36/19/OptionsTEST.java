
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "apple", false, "Apple option"));
        group.addOption(new Option("b", "banana", false, "Banana option"));

        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        
        options.addOption("f", false, "File option");
        options.addOption("d", true, "Directory option");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));
    }
    
    @Test
    public void testAddOptionWithLongOption() {
        Options options = new Options();
        
        options.addOption("f", "file", false, "File option");
        options.addOption("d", "dir", true, "Directory option");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasLongOption("file"));
        assertTrue(options.hasLongOption("dir"));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        
        options.addOption("f", false, "File option");
        
        Option option = options.getOption("f");
        
        assertNotNull(option);
        assertEquals("f", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("File option", option.getDescription());
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        options.addOption("f", false, "File option");
        options.addOption("f1", false, "First file option");
        options.addOption("fa", false, "Another file option");
        
        List<String> matchingOptions = options.getMatchingOptions("f");
        
        assertNotNull(matchingOptions);
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("f"));
        assertTrue(matchingOptions.contains("f1"));
        assertTrue(matchingOptions.contains("fa"));
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        options.addOption("f", false, "File option");
        
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("d"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        
        options.addOption("f", "file", false, "File option");
        
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasLongOption("dir"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        
        options.addOption("f", "file", false, "File option");
        
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("d"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "apple", false, "Apple option"));
        
        options.addOptionGroup(group);
        
        Option option = new Option("a", "apple", false, "Apple option");
        OptionGroup retrievedGroup = options.getOptionGroup(option);
        
        assertEquals(group, retrievedGroup);
    }
}
