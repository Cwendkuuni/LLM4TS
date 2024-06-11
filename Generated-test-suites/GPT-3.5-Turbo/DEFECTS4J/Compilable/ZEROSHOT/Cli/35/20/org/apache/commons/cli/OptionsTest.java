package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "Option 1");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "Option 1");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("o").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "Option 1");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "Option 1");
        options.addOption(option);

        assertTrue(options.hasLongOption("option1"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "Option 1");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "Option 1");
        optionGroup.addOption(option1);
        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("a", "option1", true, "Option 1");
        options.addOption(option);

        assertEquals("[ Options: [ short {a=Option -a, option1} ] [ long {} ]", options.toString());
    }

}