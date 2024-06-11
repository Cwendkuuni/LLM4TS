package org.apache.commons.cli;
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
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertNotNull(group);
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "alpha", false, "Option A");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
        
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testAddOptionWithArg() {
        Option option = new Option("a", "alpha", true, "Option A with argument");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
        
        assertEquals(option, options.getOption("a"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        option1.setRequired(true);
        option2.setRequired(true);
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }
    
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }
    
    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }
    
    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("alpha"));
        assertTrue(options.hasLongOption("beta"));
        assertFalse(options.hasLongOption("delta"));
    }
    
    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }
    
    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "charlie", false, "Option C")));
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        
        options.addOption(option1);
        options.addOption(option2);
        
        String expectedString = "[ Options: [ short {a=Option a, b=Option b} ] [ long {alpha=Option a, beta=Option b} ]";
        
        assertEquals(expectedString, options.toString());
    }
}