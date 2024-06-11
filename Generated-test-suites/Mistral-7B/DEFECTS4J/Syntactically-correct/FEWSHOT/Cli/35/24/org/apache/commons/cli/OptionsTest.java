package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test group");

        Options options = new Options();
        options = options.addOptionGroup(optionGroup);

        assertEquals("testGroup", options.getOptionGroups().iterator().next().getName());
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test group");
        Option option = new Option("opt", "opt", true, "Optional test option");
        optionGroup.addOption(option);

        Options options = new Options();
        options = options.addOptionGroup(optionGroup);

        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup optionGroup1 = new OptionGroup("testGroup1", "Test group 1");
        OptionGroup optionGroup2 = new OptionGroup("testGroup2", "Test group 2");

        Options options = new Options();
        options = options.addOptionGroup(optionGroup1);
        options = options.addOptionGroup(optionGroup2);

        List<OptionGroup> optionGroups = options.getOptionGroups();

        assertEquals(2, optionGroups.size());
        assertEquals("testGroup1", optionGroups.get(0).getName());
        assertEquals("testGroup2", optionGroups.get(1).getName());
    }

    @Test
    public void testAddOptionWithShortAndLongOpt() {
        Option option = new Option("opt", "o", true, "Test option with short and long opt");

        Options options = new Options();
        options = options.addOption(option);

        Option optionParsed = options.getOption("opt");

        assertEquals(option, optionParsed);
        assertEquals("o", optionParsed.getOpt());
        assertEquals("opt", optionParsed.getLongOpt());
        assertTrue(optionParsed.isRequired());
    }

    @Test
    public void testAddOptionWithShortOptOnly() {
        Option option = new Option("opt", "Test option with short opt only");

        Options options = new Options();
        options = options.addOption(option);

        Option optionParsed = options.getOption("opt");

        assertEquals(option, optionParsed);
        assertEquals("opt", optionParsed.getOpt());
        assertNull(optionParsed.getLongOpt());
        assertFalse(optionParsed.isRequired());
    }

    @Test
    public void testAddOptionWithLongOptOnly() {
        Option option = new Option("longOpt", "Test option with long opt only", true, "Test long opt only option");

        Options options = new Options();
        options = options.addOption(option);

        Option optionParsed = options.getOption("--longOpt");

        assertEquals(option, optionParsed);
        assertEquals(null, optionParsed.getOpt());
        assertEquals("longOpt", optionParsed.getLongOpt());
        assertTrue(optionParsed.isRequired());
    }

    @Test
    public void testAddOptionWithSameShortOptInMultipleOptions() {
        Option option1 = new Option("opt", "Test option 1");
        Option option2 = new Option("opt", "Test option 2");

        Options options = new Options();
        options = options.addOption(option1);
        options = options.addOption(option2);

        Option option1Parsed = options.getOption("opt");
        Option option2Parsed = options.getOption("opt");

        assertEquals(option1, option1Parsed);
        assertEquals(option2, option2Parsed);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("opt", "Test option", true);

        Options options = new Options();
        options = options.addOption(option);

        List<Option> optionsParsed = options.getOptions();

        assertEquals(1, optionsParsed.size());
        assertEquals(option, optionsParsed.get(0));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("opt", "Test option", true);

        Options options = new Options();
        options = options.addOption(option);

        List<Option> requiredOptionsParsed = options.getRequiredOptions();

        assertEquals(1, requiredOptionsParsed.size());
        assertEquals(option, requiredOptionsParsed.get(0));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("opt", "Test option", true);

        Options options = new Options();
        options = options.addOption(option);

        Option optionParsed = options.getOption("opt");

        assertEquals(option, optionParsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullOption() {
        Options options = new Options();
        options.addOption(null);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("opt1", "Test option 1");
        Option option2 = new Option("opt2", "Test option 2");
        Option option3 = new Option("opt", "Test option");

        Options options = new Options();
        options = options.addOption(option1);
        options = options.addOption(option2);
        options = options.addOption(option3);

        List<String> matchingOptionsParsed = options.getMatchingOptions("opt");

        assertEquals(1, matchingOptionsParsed.size());
        assertEquals("opt", matchingOptionsParsed.get(0));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt", "Test option", true);

        Options options = new Options();
        options = options.addOption(option);

        assertTrue(options.hasOption("opt"));
        assertTrue(options.hasLongOption("opt"));
        assertTrue(options.hasShortOption("opt"));
    }

    @Test
    public void testHasOptionWithNullOpt() {
        Options options = new Options();

        assertFalse(options.hasOption(null));
        assertFalse(options.hasLongOption(null));
        assertFalse(options.hasShortOption(null));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("opt1", "Test option 1");
        Option option2 = new Option("opt2", "Test option 2");
        Option option3 = new Option("opt3", "Test option 3");

        Options options = new Options();
        options = options.addOption(option1);
        options = options.addOption(option2);
        options = options.addOption(option3);

        String optionsString = options.toString();

        StringBuilder expected = new StringBuilder();
        expected.append("[ Options: [ short ");
        expected.append(options.shortOpts.toString());
        expected.append(" ] [ long ");
        expected.append(options.longOpts);
        expected.append(" ]]");

        assertEquals(expected.toString(), optionsString);
    }
}