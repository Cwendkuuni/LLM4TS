package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test Group");
        Option option = new Option("t", "test", true, "Test Option");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getOptions().size());
        assertEquals(1, options.getRequiredOptions().size());
        assertEquals(1, options.getOptionGroups().size());
        assertTrue(options.hasOption("t"));
        assertTrue(options.hasLongOption("test"));
        assertTrue(options.hasShortOption("t"));
        assertTrue(options.hasOption("test"));

        Option testOption = options.getOption("test");
        assertEquals("Test Option", testOption.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, testOption.getArgParser().getMaxOccurrences());
        assertTrue(testOption.isRequired());
        assertFalse(testOption.hasArg());

        OptionGroup testGroup = options.getOptionGroup("TestGroup");
        assertEquals("Test Group", testGroup.getLongOpt());
        assertEquals(1, testGroup.getOptions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupWithDuplicateLongOpt() {
        OptionGroup optionGroup1 = new OptionGroup("TestGroup1", "Test Group 1");
        Option option1 = new Option("t", "test1", true, "Test Option 1");
        optionGroup1.addOption(option1);
        options.addOptionGroup(optionGroup1);

        OptionGroup optionGroup2 = new OptionGroup("TestGroup2", "Test Group 2");
        Option option2 = new Option("t", "test2", true, "Test Option 2");
        optionGroup2.addOption(option2);
        options.addOptionGroup(optionGroup2);
    }

    @Test
    public void testAddOption() {
        options.addOption("t", "test", true);

        assertEquals(1, options.getOptions().size());
        assertEquals(1, options.getRequiredOptions().size());
        assertNull(options.getOptionGroup("TestGroup"));

        Option testOption = options.getOption("test");
        assertEquals("", testOption.getLongOpt());
        assertEquals(Option.NO_ARGUMENTS, testOption.getArgParser().getArgumentType());
        assertTrue(testOption.isRequired());
        assertFalse(testOption.hasArg());
    }

    @Test
    public void testAddOptionWithArg() {
        options.addOption("t", "test", true, "Test Option with Argument");

        assertEquals(1, options.getOptions().size());
        assertEquals(1, options.getRequiredOptions().size());
        assertNull(options.getOptionGroup("TestGroup"));

        Option testOption = options.getOption("test");
        assertEquals("Test Option with Argument", testOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, testOption.getArgParser().getArgumentType());
        assertTrue(testOption.isRequired());
        assertTrue(testOption.hasArg());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        options.addOption("t", "test-long", true, "Test Option with Long Argument");

        assertEquals(1, options.getOptions().size());
        assertEquals(1, options.getRequiredOptions().size());
        assertNull(options.getOptionGroup("TestGroup"));

        Option testOption = options.getOption("test-long");
        assertEquals("Test Option with Long Argument", testOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, testOption.getArgParser().getArgumentType());
        assertTrue(testOption.isRequired());
        assertTrue(testOption.hasArg());
    }

    @Test
    public void testAddOptionWithShortAndLongOpt() {
        options.addOption("t", "test-short", false, "Test Option with Short Argument");
        options.addOption("l", "test-long", false, "Test Option with Long Argument");

        assertEquals(2, options.getOptions().size());
        assertEquals(2, options.getRequiredOptions().size());
        assertNull(options.getOptionGroup("TestGroup"));

        Option testShortOption = options.getOption("test-short");
        assertEquals("Test Option with Short Argument", testShortOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, testShortOption.getArgParser().getArgumentType());
        assertFalse(testShortOption.isRequired());
        assertFalse(testShortOption.hasArg());

        Option testLongOption = options.getOption("test-long");
        assertEquals("Test Option with Long Argument", testLongOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, testLongOption.getArgParser().getArgumentType());
        assertFalse(testLongOption.isRequired());
        assertFalse(testLongOption.hasArg());
    }

    @Test
    public void testAddOptions() {
        options.addOptions(Arrays.asList(
                new Option("t", "test-short", true, "Test Option with Short Argument"),
                new Option("l", "test-long", true, "Test Option with Long Argument"),
                new Option("o", "option", false, "Test Option with no argument")
        ));

        assertEquals(3, options.getOptions().size());
        assertEquals(3, options.getRequiredOptions().size());
        assertNull(options.getOptionGroup("TestGroup"));

        Option testShortOption = options.getOption("test-short");
        assertEquals("Test Option with Short Argument", testShortOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, testShortOption.getArgParser().getArgumentType());
        assertTrue(testShortOption.isRequired());
        assertFalse(testShortOption.hasArg());

        Option testLongOption = options.getOption("test-long");
        assertEquals("Test Option with Long Argument", testLongOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, testLongOption.getArgParser().getArgumentType());
        assertTrue(testLongOption.isRequired());
        assertFalse(testLongOption.hasArg());

        Option optionOption = options.getOption("option");
        assertEquals("Test Option with no argument", optionOption.getDescription());
        assertEquals(Option.NO_ARGUMENTS, optionOption.getArgParser().getArgumentType());
        assertFalse(optionOption.isRequired());
        assertFalse(optionOption.hasArg());
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("t1", "test1", true, "Test Option 1");
        Option option2 = new Option("t2", "test2", true, "Test Option 2");
        Option option3 = new Option("t3", "test3", true, "Test Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("t");
        assertEquals(2, matchingOptions.size());
        assertIterableEquals(Arrays.asList("test1", "test2"), matchingOptions);
    }

    @Test
    public void testGetMatchingOptionsWithLongOpt() {
        Option option1 = new Option("t1l", "test1long", true, "Test Option 1 with long name");
        Option option2 = new Option("t2l", "test2long", true, "Test Option 2 with long name");
        Option option3 = new Option("t3l", "test3long", true, "Test Option 3 with long name");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("t2");
        assertEquals(1, matchingOptions.size());
        assertEquals("test2long", matchingOptions.get(0));
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        List<String> matchingOptions = options.getMatchingOptions("x");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("t1", "test1", true, "Test Option 1");
        Option option2 = new Option("t2", "test2", true, "Test Option 2");
        Option option3 = new Option("t3", "test3", true, "Test Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals("[ Options: [ short {t=test1, t1=test1}, {} ], [ long {}, {} ], [] ]", options.toString());
    }
}