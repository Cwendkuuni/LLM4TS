package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group Options");
        Options opts = new Options();

        opts = opts.addOptionGroup(group);

        assertEquals(1, opts.getOptionGroups().size());
        assertSame(group, opts.getOptionGroups().iterator().next());
    }

    @Test
    public void testAddOptionGroupWithRequiredOptions() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group Options");
        group.addOption(new Option("a", "a", true, "Required option A"));
        Options opts = new Options();

        opts = opts.addOptionGroup(group);

        List<Object> requiredOptions = opts.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.get(0) instanceof OptionGroup);
        assertSame(group, requiredOptions.get(0));
    }

    @Test
    public void testAddOption() {
        Options opts = new Options();

        opts = opts.addOption("a", "Option A");

        assertEquals(1, opts.getOptions().size());
        assertEquals("a", opts.getOptions().get(0).getKey());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options opts = new Options();

        opts = opts.addOption("a", "Option A", true, "Option with short and long name");

        assertEquals(1, opts.getOptions().size());
        assertEquals("a", opts.getOptions().get(0).getKey());
        assertTrue(opts.getOptions().get(0).hasLongOpt());
    }

    @Test
    public void testAddOptionWithArgument() {
        Options opts = new Options();

        opts = opts.addOption("a", true, "Option with argument");

        assertEquals(1, opts.getOptions().size());
        assertTrue(opts.getOptions().get(0).hasArg());
    }

    @Test
    public void testAddOptionWithNoArgument() {
        Options opts = new Options();

        opts = opts.addOption("a", "Option with no argument");

        assertEquals(1, opts.getOptions().size());
        assertFalse(opts.getOptions().get(0).hasArg());
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "Option A");
        Options opts = new Options().addOption(option);

        Option retrievedOption = opts.getOption("a");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetOptionWithLongName() {
        Option option = new Option("longOption", "Long Option A");
        Options opts = new Options().addOption(option);

        Option retrievedOption = opts.getOption("--longOption");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetOptionsWithPrefix() {
        Option option1 = new Option("a", "Option A");
        Option option2 = new Option("b", "Option B");
        Options opts = new Options()
                .addOption(option1)
                .addOption(option2);

        List<Option> matchingOptions = opts.getMatchingOptions("a");

        assertEquals(1, matchingOptions.size());
        assertEquals(option1, matchingOptions.get(0));
    }

    @Test
    public void testGetOptionsWithNoMatch() {
        Option option1 = new Option("a", "Option A");
        Option option2 = new Option("b", "Option B");
        Options opts = new Options()
                .addOption(option1)
                .addOption(option2);

        List<Option> matchingOptions = opts.getMatchingOptions("c");

        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "Option A");
        Options opts = new Options().addOption(option);

        assertTrue(opts.hasOption("a"));
        assertTrue(opts.hasOption("--a"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "Option A", true, "Long Option A");
        Options opts = new Options().addOption(option);

        assertTrue(opts.hasLongOption("--a"));
    }

    @Test
    public void testHasShortOption() {   
        Option option = new Option("a", "Option A");
        Options opts = new Options().addOption(option);

        assertTrue(opts.hasShortOption("a"));
    }

    @Test
    public void testHasNoOption() {
        Option option = new Option("a", "Option A");
        Options opts = new Options();

        assertFalse(opts.hasOption("a"));
        assertFalse(opts.hasOption("--a"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group Options");
        Option option = new Option("a", "Option A", true, "Option A", group);
        Options opts = new Options().addOptionGroup(group).addOption(option);

        OptionGroup retrievedGroup = opts.getOptionGroup(option);

        assertEquals(group, retrievedGroup);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "Option A");
        Option option2 = new Option("b", "Option B");
        Options opts = new Options()
                .addOption(option1)
                .addOption(option2);

        String toString = opts.toString();

        String expectedToString = "[ Options: [ short {a=} ] [ long {}--a={}, {}--b={}] ]";
        assertEquals(expectedToString, toString);
    }
}