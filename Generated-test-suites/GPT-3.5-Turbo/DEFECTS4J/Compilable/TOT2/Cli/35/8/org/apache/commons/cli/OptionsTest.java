package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "alpha", false, "Alpha option");
        Option option2 = new Option("b", "beta", false, "Beta option");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("c", "charlie", false, "Charlie option");
        
        options.addOption(option1);
        
        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "delta", false, "Delta option");
        
        options.addOption(option);
        
        assertEquals(option, options.getOption("d"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("e", "echo", false, "Echo option");
        Option option2 = new Option("f", "foxtrot", false, "Foxtrot option");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(1, options.getMatchingOptions("f").size());
        assertEquals(0, options.getMatchingOptions("g").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("g", "golf", false, "Golf option");
        
        options.addOption(option);
        
        assertTrue(options.hasOption("g"));
        assertFalse(options.hasOption("h"));
    }
}