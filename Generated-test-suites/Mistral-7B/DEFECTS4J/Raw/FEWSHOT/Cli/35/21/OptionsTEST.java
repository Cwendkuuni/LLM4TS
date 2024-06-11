package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("testOptionGroup", "Test Option Group");
        Options options = new Options();

        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getOptionGroups().size());
        assertSame(optionGroup, options.getOptionGroups().iterator().next());
    }

    @Test
    public void testAddOptionWithShortNameOnly() {
        Options options = new Options();

        options.addOption('a', "optionA", false, "Short option A");

        assertEquals(1, options.getOptions().size());
        assertEquals("Short option A", options.getOptions().get(0).getDescription());
        assertEquals('a', options.getOptions().get(0).getKey().charAt(0));
        assertFalse(options.getOptions().get(0).hasArg());
    }

    @Test
    public void testAddOptionWithShortNameAndLongName() {
        Options options = new Options();

        options.addOption('a', "optionA", false, "Short option A");
        options.addOption("optionB", "optionB", false, "Long option B");

        assertEquals(2, options.getOptions().size());
        assertEquals("Short option A", options.getOptions().get(0).getDescription());
        assertEquals('a', options.getOptions().get(0).getKey().charAt(0));
        assertFalse(options.getOptions().get(0).hasArg());
        assertEquals("Long option B", options.getOptions().get(1).getDescription());
        assertEquals("optionB", options.getOptions().get(1).getKey());
        assertFalse(options.getOptions().get(1).hasArg());
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Options options = new Options();

        options.addOption('a', "optionA", true, "Short option A with value");

        assertEquals(1, options.getOptions().size());
        assertEquals("Short option A with value", options.getOptions().get(0).getDescription());
        assertEquals('a', options.getOptions().get(0).getKey().charAt(0));
        assertTrue(options.getOptions().get(0).hasArg());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", false, "Short option A");
        Option optionB = new Option("optionB", "optionB", false, "Long option B");

        options.addOption(optionA);
        options.addOption(optionB);

        Option actualOptionA = options.getOption("optionA");
        Option actualOptionB = options.getOption("optionB");

        assertEquals(optionA, actualOptionA);
        assertEquals(optionB, actualOptionB);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", false, "Short option A");
        Option optionB = new Option("optionB", "optionB", false, "Long option B");
        Option optionC = new Option("optionC", "optionC", false, "Long option C");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        List<String> matchingOptions = options.getMatchingOptions("opt");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("optionB"));
        assertTrue(matchingOptions.contains("optionC"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", false, "Short option A");
        Option optionB = new Option("optionB", "optionB", false, "Long option B");

        options.addOption(optionA);
        options.addOption(optionB);

        assertTrue(options.hasOption("optionA"));
        assertTrue(options.hasOption("optionB"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", false, "Short option A");
        Option optionB = new Option("optionB", "optionB", false, "Long option B");

        options.addOption(optionA);
        options.addOption(optionB);

        assertTrue(options.hasShortOption('a'));
        assertFalse(options.hasShortOption('b'));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", false, "Short option A");
        Option optionB = new Option("optionB", "optionB", false, "Long option B");

        options.addOption(optionA);
        options.addOption(optionB);

        assertFalse(options.hasLongOption("optionA"));
        assertTrue(options.hasLongOption("optionB"));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", true, "Required option A");

        options.addOption(optionA);

        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertEquals("Required option A", requiredOptions.get(0).getDescription());
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        Option optionA = new Option('a', "optionA", true, "Required option A");

        OptionGroup optionGroup = new OptionGroup("testOptionGroup", "Test Option Group")
                .addOption(optionA);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertEquals("Required option A", requiredOptions.get(0).getDescription());
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option optionA = new Option('a', "optionA", false, "Short option A");
        Option optionB = new Option("optionB", "optionB", false, "Long option B");

        options.addOption(optionA);
        options.addOption(optionB);

        String expectedOutput = "[ Options: [ short {" + optionA.getKey() + ", optionA}" +
                " } {" + optionB.getKey() + ", optionB}" +
                " } ] [ long {} ]]";

        assertEquals(expectedOutput, options.toString());
    }
}