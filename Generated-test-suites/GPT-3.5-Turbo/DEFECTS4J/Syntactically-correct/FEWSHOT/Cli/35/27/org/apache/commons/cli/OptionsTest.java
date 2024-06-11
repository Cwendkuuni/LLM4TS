package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionStringStringBooleanString() {
        options.addOption("a", true, "Option a");
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionStringStringBooleanString2() {
        options.addOption("a", false, "Option a");
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionStringStringBooleanString3() {
        options.addOption("a", "aaa", true, "Option a");
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionStringStringBooleanString4() {
        options.addOption("a", "aaa", false, "Option a");
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("b", "bbb", true, "Option b");
        group.addOption(option);
        options.addOptionGroup(group);
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testGetOption() {
        options.addOption("c", "ccc", true, "Option c");
        Option option = options.getOption("c");
        assertEquals("c", option.getOpt());
        assertEquals("ccc", option.getLongOpt());
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("d", true, "Option d");
        options.addOption("dd", true, "Option dd");
        options.addOption("ddd", true, "Option ddd");

        List<String> matchingOptions = options.getMatchingOptions("d");
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("d"));
        assertTrue(matchingOptions.contains("dd"));
        assertTrue(matchingOptions.contains("ddd"));
    }

    @Test
    public void testHasOption() {
        assertFalse(options.hasOption("e"));
        options.addOption("e", true, "Option e");
        assertTrue(options.hasOption("e"));
    }

    @Test
    public void testHasLongOption() {
        assertFalse(options.hasLongOption("f"));
        options.addOption("f", "fff", true, "Option f");
        assertTrue(options.hasLongOption("fff"));
    }

    @Test
    public void testHasShortOption() {
        assertFalse(options.hasShortOption("g"));
        options.addOption("g", true, "Option g");
        assertTrue(options.hasShortOption("g"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("h", "hhh", true, "Option h");
        group.addOption(option);
        options.addOptionGroup(group);

        OptionGroup retrievedGroup = options.getOptionGroup(option);
        assertEquals(group, retrievedGroup);
    }
}