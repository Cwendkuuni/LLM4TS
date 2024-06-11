import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", true, "input file"));
        group.addOption(new Option("d", "directory", false, "input directory"));
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertNotNull(optionGroups);
        assertEquals(1, optionGroups.size());
        assertEquals(group, optionGroups.iterator().next());
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", false, "input directory");
        
        Collection<Option> optionCollection = options.getOptions();
        assertNotNull(optionCollection);
        assertEquals(2, optionCollection.size());
        
        List<Option> optionList = options.helpOptions();
        assertNotNull(optionList);
        assertEquals(2, optionList.size());
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", false, "input directory");
        
        assertEquals(0, options.getRequiredOptions().size());
        
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "file", true, "input file"));
        
        options.addOptionGroup(group);
        
        assertEquals(1, options.getRequiredOptions().size());
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", false, "input directory");
        
        Option option1 = options.getOption("f");
        assertNotNull(option1);
        assertEquals("f", option1.getOpt());
        assertEquals("file", option1.getLongOpt());
        
        Option option2 = options.getOption("file");
        assertNull(option2);
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", false, "input directory");
        options.addOption("format", true, "input format");
        
        List<String> matchingOptions = options.getMatchingOptions("f");
        assertNotNull(matchingOptions);
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("file"));
        assertTrue(matchingOptions.contains("format"));
        
        matchingOptions = options.getMatchingOptions("fo");
        assertNotNull(matchingOptions);
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("format"));
        
        matchingOptions = options.getMatchingOptions("x");
        assertNotNull(matchingOptions);
        assertEquals(0, matchingOptions.size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        options.addOption("d", "directory", false, "input directory");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertFalse(options.hasOption("x"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        
        assertTrue(options.hasLongOption("file"));
        assertFalse(options.hasLongOption("f"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        
        options.addOption("f", "file", true, "input file");
        
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("file"));
    }
    
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("f", "file", true, "input file"));
        
        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("d", "directory", true, "input directory"));
        
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        
        Option option1 = options.getOption("f");
        OptionGroup result1 = options.getOptionGroup(option1);
        assertEquals(group1, result1);
        
        Option option2 = options.getOption("d");
        OptionGroup result2 = options.getOptionGroup(option2);
        assertEquals(group2, result2);
        
        Option option3 = options.getOption("x");
        OptionGroup result3 = options.getOptionGroup(option3);
        assertNull(result3);
    }
}