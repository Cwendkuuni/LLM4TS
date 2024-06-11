package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }
    
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertTrue(options.getOption("a").equals(option));
    }
    
    @Test
    public void testAddOptionWithType() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertTrue(options.getOption("a").equals(option));
        assertTrue(options.getOption("a").hasArg());
        assertTrue(options.getOption("option").equals(option));
        assertTrue(options.getOption("option").hasArg());
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", false, "Option 2");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getRequiredOptions().contains(option2));
        assertFalse(options.getRequiredOptions().contains(option1));
    }
    
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("b", "option", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getOption("a").equals(option1));
        assertTrue(options.getOption("option").equals(option2));
        assertNull(options.getOption("c"));
    }
    
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("help", false, "Help option");
        Option option2 = new Option("host", false, "Host option");
        Option option3 = new Option("hide", false, "Hide option");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        assertTrue(options.getMatchingOptions("h").contains("help"));
        assertTrue(options.getMatchingOptions("h").contains("hide"));
        assertFalse(options.getMatchingOptions("h").contains("host"));
        assertTrue(options.getMatchingOptions("help").contains("help"));
        assertEquals(0, options.getMatchingOptions("abc").size());
    }
    
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("a"));
    }
    
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("option"));
    }
    
    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("option", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {a=Option 1} ] [ long {option=Option 2} ]";
        assertEquals(expected, options.toString());
    }
    
    
    // Generated test cases for edge cases and exception handling scenarios

    @Test(expected=IllegalArgumentException.class)
    public void testAddOptionGroupDuplicateOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("a", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
    }

    @Test
    public void testAddOptionWithDuplicateKey() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option 1");
        Option option2 = new Option("a", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getOption("a").equals(option2));
    }
    
    @Test
    public void testGetMatchingOptionsPartialName() {
        Options options = new Options();
        Option option1 = new Option("help", false, "Help option");
        Option option2 = new Option("host", false, "Host option");
        Option option3 = new Option("hide", false, "Hide option");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        assertTrue(options.getMatchingOptions("h").contains("help"));
        assertTrue(options.getMatchingOptions("h").contains("hide"));
        assertFalse(options.getMatchingOptions("h").contains("host"));
        assertTrue(options.getMatchingOptions("help").contains("help"));
        assertEquals(2, options.getMatchingOptions("h").size());
        assertEquals(0, options.getMatchingOptions("abc").size());
    }
    
    
}