package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();
        options.addOption("a", "option-a", false, "Option A");

        Option optionA = options.getOption("a");
        assertEquals("Option A", optionA.getDescription());
        assertFalse(optionA.hasArg());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options options = new Options();
        options.addOption("a", "option-a", false, "Option A");
        options.addOption("--option-a", "option-a-long", false, "Option A long");

        Option optionA = options.getOption("a");
        assertEquals("Option A long", optionA.getLongOpt());
        assertEquals("Option A", optionA.getKey());

        Option optionALong = options.getOption("--option-a");
        assertEquals("Option A long", optionALong.getLongOpt());
        assertSame(optionA, optionALong);
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");

        Option optionA = options.getOption("a");
        assertTrue(optionA.hasArg());
    }

    @Test
    public void testAddOptionWithShortNameAndValueAndDescription() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A with value, description: %s");

        Option optionA = options.getOption("a");
        assertTrue(optionA.hasArg());
        assertEquals("Option A with value, description: %s", optionA.getDescription());
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        options.addOption("--option-b", "option-b", false, "Option B");

        Option optionB = options.getOption("--option-b");
        assertEquals("Option B", optionB.getDescription());
        assertFalse(optionB.hasArg());
    }

    @Test
    public void testAddOptionWithLongNameAndValue() {
        Options options = new Options();
        options.addOption("--option-b", "option-b", true, "Option B with value, description: %s");

        Option optionB = options.getOption("--option-b");
        assertTrue(optionB.hasArg());
        assertEquals("Option B with value, description: %s", optionB.getDescription());
    }

    @Test
    public void testAddOptionWithLongNameAndValueAndDescriptionAndShortName() {
        Options options = new Options();
        options.addOption("--option-b", "option-b", true, "Option B with value, description: %s");
        options.addOption("b", "option-b", true, "Option B with short name");

        Option optionB = options.getOption("--option-b");
        assertTrue(optionB.hasArg());
        assertEquals("Option B with value, description: %s", optionB.getDescription());
        assertEquals("Option B with short name", optionB.getKey());

        Option optionBShort = options.getOption("b");
        assertTrue(optionBShort.hasArg());
        assertEquals("Option B with short name", optionBShort.getKey());
        assertSame(optionB, optionBShort);
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("group", "Option group");
        optionGroup.addOption("--group-a", true, "Option group A");
        optionGroup.addOption("--group-b", true, "Option group B");

        options.addOptionGroup(optionGroup);

        Option optionGroupA = options.getOption("--group-a");
        assertEquals("Option group A", optionGroupA.getDescription());
        assertFalse(optionGroupA.hasArg());

        Option optionGroupB = options.getOption("--group-b");
        assertEquals("Option group B", optionGroupB.getDescription());
        assertFalse(optionGroupB.hasArg());

        List<Option> groupOptions = options.getOptions();
        assertEquals(3, groupOptions.size());
        assertTrue(groupOptions.contains(optionGroupA));
        assertTrue(groupOptions.contains(optionGroupB));
        assertTrue(groupOptions.contains(options.findOption("h")));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption("--option-a", true, "Required option A");
        options.addOption("--option-b", true, "Required option B");

        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(2, requiredOptions.size());
        assertTrue(requiredOptions.contains(options.findOption("--option-a")));
        assertTrue(requiredOptions.contains(options.findOption("--option-b")));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("--option-a", true, "Option A");

        assertTrue(options.hasOption("--option-a"));
        assertFalse(options.hasOption("--non-existent-option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("--option-a", true, "Option A");

        assertTrue(options.hasLongOption("--option-a"));
        assertFalse(options.hasLongOption("--non-existent-option"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("--option-a", true, "Option A");

        Option optionA = options.getOption("--option-a");
        assertEquals("Option A", optionA.getDescription());
        assertFalse(optionA.hasArg());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("--option-a", true, "Option A");
        options.addOption("--option-b", true, "Option B");
        options.addOption("--option-c", true, "Option C");

        List<String> matchingOptions = options.getMatchingOptions("option");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("--option-a"));
        assertTrue(matchingOptions.contains("--option-b"));
    }
}