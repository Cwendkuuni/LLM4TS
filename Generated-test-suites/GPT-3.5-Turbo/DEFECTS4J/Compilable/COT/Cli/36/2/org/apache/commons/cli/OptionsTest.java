package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);

        Options options = new Options();
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));

        Option option2 = options.getOption("o");
        assertEquals(option2, option);
    }

    @Test
    public void testAddOptionWithArg() {
        Option option = new Option("o", "option", true, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));

        Option option2 = options.getOption("o");
        assertEquals(option2, option);
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));

        Option option2 = options.getOption("option");
        assertEquals(option2, option);
    }

    @Test
    public void testAddOptionWithLongOptAndArg() {
        Option option = new Option("o", "option", true, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));

        Option option2 = options.getOption("option");
        assertEquals(option2, option);
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(true);

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        Option option2 = options.getOption("option");
        assertEquals(option2, option);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "option", false, "Option");
        Option option2 = new Option("t", "test", false, "Test");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(matchingOptions, Arrays.asList("option"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("abc"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("abc"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("x"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);

        Options options = new Options();
        options.addOptionGroup(group);

        OptionGroup optionGroup = options.getOptionGroup(option);
        assertEquals(optionGroup, group);
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", false, "Option");

        Options options = new Options();
        options.addOption(option);

        assertEquals(options.toString(), "[ Options: [ short {o=org.apache.commons.cli.Option [ opt=o, longOpt=option, hasArg=false, description=Option, required=false ]} ] [ long {option=org.apache.commons.cli.Option [ opt=o, longOpt=option, hasArg=false, description=Option, required=false ]} ]");
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}