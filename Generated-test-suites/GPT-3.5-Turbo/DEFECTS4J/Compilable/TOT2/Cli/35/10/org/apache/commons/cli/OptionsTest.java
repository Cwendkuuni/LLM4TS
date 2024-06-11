package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "apple", false, "description");
        options.addOption(option);
        
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "description 1");
        Option option2 = new Option("b", "banana", false, "description 2");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOptionWithDescription() {
        Options options = new Options();
        options.addOption("o", "description");
        
        assertTrue(options.hasOption("o"));
        assertEquals("description", options.getOption("o").getDescription());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("apple", "apple option", false, "description 1");
        options.addOption("banana", "banana option", false, "description 2");
        
        assertEquals(2, options.getMatchingOptions("app").size());
        assertEquals(1, options.getMatchingOptions("ban").size());
        assertEquals(0, options.getMatchingOptions("pear").size());
    }

    @Test
    public void testRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "description 1");
        Option option2 = new Option("b", "banana", false, "description 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertFalse(options.getRequiredOptions().contains("a"));
        assertFalse(options.getRequiredOptions().contains("b"));
        
        option1.setRequired(true);
        
        assertTrue(options.getRequiredOptions().contains("a"));
        assertFalse(options.getRequiredOptions().contains("b"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("o", "option description");

        assertTrue(options.toString().contains("Options"));
        assertTrue(options.toString().contains("o"));
    }
}