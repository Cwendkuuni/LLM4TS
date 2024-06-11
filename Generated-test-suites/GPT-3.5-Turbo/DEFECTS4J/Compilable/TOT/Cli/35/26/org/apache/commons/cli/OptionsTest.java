package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding option group with required options
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testAddOption() {
        // Test adding option without short or long name
        Options options = new Options();
        Option option = new Option(null, null, false, "Option without name");
        options.addOption(option);

        assertTrue(options.hasOption(null));
        assertTrue(options.getOption(null).equals(option));
    }

    @Test
    public void testGetRequiredOptions() {
        // Test getting required options
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(options.getRequiredOptions().size(), 1);
        assertTrue(options.getRequiredOptions().contains(option1));
    }

    @Test
    public void testGetOption() {
        // Test getting option by short name
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);

        assertTrue(options.getOption("a").equals(option));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting matching options
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        Option option3 = new Option("abc", null, false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(options.getMatchingOptions("o").size(), 3);
        assertTrue(options.getMatchingOptions("o").contains("option1"));
        assertTrue(options.getMatchingOptions("o").contains("option2"));
        assertTrue(options.getMatchingOptions("o").contains("abc"));
    }

    @Test
    public void testHasOption() {
        // Test checking if options exist
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
    }

    @Test
    public void testHasLongOption() {
        // Test checking if long option exists
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);

        assertTrue(options.hasLongOption("option1"));
    }

    @Test
    public void testHasShortOption() {
        // Test checking if short option exists
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test getting option group
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testToString() {
        // Test converting Options object to string
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);

        String expected = "[ Options: [ short {a=option1} ] [ long {option1=Option 1} ]]";
        assertEquals(options.toString(), expected);
    }
}