import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {
    
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup()
                            .addOption(new Option("a", "apple", false, "apple description"))
                            .addOption(new Option("b", "banana", false, "banana description"));
                            
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }
    
    @Test
    public void testAddOption() {
        options.addOption(new Option("c", "car", false, "car description"));
        
        assertTrue(options.hasOption("c"));
    }
    
    @Test
    public void testGetOptions() {
        options.addOption(new Option("d", "dog", false, "dog description"));
        options.addOption(new Option("e", "elephant", false, "elephant description"));
        
        assertEquals(2, options.getOptions().size());
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("f", "fish", false, "fish description");
        option1.setRequired(true);
        Option option2 = new Option("g", "goat", false, "goat description");
        option2.setRequired(true);
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getRequiredOptions().size());
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("h", "horse", false, "horse description");
        Option option2 = new Option("i", "iguana", false, "iguana description");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("h"));
        assertEquals(option2, options.getOption("i"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("j", "jaguar", false, "jaguar description");
        Option option2 = new Option("k", "kangaroo", false, "kangaroo description");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getMatchingOptions("kang").size());
        assertEquals("kangaroo", options.getMatchingOptions("kang").get(0));
    }
    
    @Test
    public void testHasOption() {
        options.addOption(new Option("l", "lion", false, "lion description"));
        assertTrue(options.hasOption("l"));
    }
    
    @Test
    public void testHasLongOption() {
        options.addOption(new Option("m", "monkey", false, "monkey description"));
        assertTrue(options.hasLongOption("monkey"));
    }
    
    @Test
    public void testHasShortOption() {
        options.addOption(new Option("n", "newt", false, "newt description"));
        assertTrue(options.hasShortOption("n"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup()
                            .addOption(new Option("o", "ostrich", false, "ostrich description"));
                            
        options.addOptionGroup(group);
        
        Option option = new Option("o", "ostrich", false, "ostrich description");
        
        assertEquals(group, options.getOptionGroup(option));
    }
    
    @Test
    public void testToString() {
        options.addOption(new Option("p", "peacock", false, "peacock description"));
        
        String expected = "[ Options: [ short {p=Option: { longOpt: 'peacock', description: 'peacock description'}} ] [ long {peacock=Option: { longOpt: 'peacock', description: 'peacock description'}} ]";
        
        assertEquals(expected, options.toString());
    }
}