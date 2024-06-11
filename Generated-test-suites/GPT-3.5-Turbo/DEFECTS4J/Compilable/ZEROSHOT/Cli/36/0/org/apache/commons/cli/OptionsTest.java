package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "alpha", false, "Alpha option"));
        group.addOption(new Option("b", "beta", false, "Beta option"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        
        options.addOption(option);
        
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        
        options.addOption(option);
        
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        
        options.addOption(option);

        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("unknown"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("unknown"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("unknown"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Test option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        
        options.addOption(option);

        assertEquals("[ Options: [ short {o=Option - o} ] [ long {option=Option - option} ]", options.toString());
    }

}