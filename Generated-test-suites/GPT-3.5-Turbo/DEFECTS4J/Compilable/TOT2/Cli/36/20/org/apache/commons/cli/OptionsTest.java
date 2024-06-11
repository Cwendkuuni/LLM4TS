package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aa", false, "description");
        Option option2 = new Option("b", "bb", true, "description");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "cc", false, "description");
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "dd", true, "description");
        options.addOption(option);
        
        assertEquals(option, options.getOption("d"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("e", "ee", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasOption("e"));
        assertFalse(options.hasOption("z"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "ff", false, "description");
        Option option2 = new Option("g", "gg", true, "description");
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
    }
}