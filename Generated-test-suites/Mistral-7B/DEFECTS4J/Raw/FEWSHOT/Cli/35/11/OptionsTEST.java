package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options opts = new Options();
        Option opt = new Option("a", "a", false, "Option A");
        opts.addOption(opt);

        assertEquals(1, opts.getOptions().size());
        assertEquals(opt, opts.getOptions().get(0));
        assertNull(opts.getLongOpt("b"));
        assertNull(opts.getShortOpt("b"));
    }

    @Test
    public void testAddOptionWithLongName() {
        Options opts = new Options();
        Option opt = new Option("a", "option-a", false, "Option A with long name");
        opts.addOption(opt);

        assertEquals(1, opts.getOptions().size());
        assertEquals(opt, opts.getOptions().get(0));
        assertEquals("option-a", opts.getLongOpt("a").getLongOpt());
        assertNull(opts.getShortOpt("a"));
    }

    @Test
    public void testAddOptionWithValue() {
        Options opts = new Options();
        Option opt = new Option("a", "a", true, "Option A with value");
        opts.addOption(opt);

        assertEquals(1, opts.getOptions().size());
        assertEquals(opt, opts.getOptions().get(0));
        assertTrue(opt.hasArg());
        assertEquals("", opt.getArgName());
    }

    @Test
    public void testAddOptionWithArgName() {
        Options opts = new Options();
        Option opt = new Option("a", "a", true, "Option A with value");
        opt.setArgName("value");
        opts.addOption(opt);

        assertEquals(1, opts.getOptions().size());
        assertEquals(opt, opts.getOptions().get(0));
        assertTrue(opt.hasArg());
        assertEquals("value", opt.getArgName());
    }

    @Test
    public void testAddOptionWithDescription() {
        Options opts = new Options();
        Option opt = new Option("a", "a", false, "Option A with description");
        opt.setDescription("A short description for option A");
        opts.addOption(opt);

        assertEquals(1, opts.getOptions().size());
        assertEquals(opt, opts.getOptions().get(0));
        assertEquals("A short description for option A", opt.getDescription());
    }

    @Test
    public void testAddOptionGroup() {
        Options parentOpts = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");

        parentOpts.addOptionGroup(group);
        parentOpts.addOption(new Option("g", "g", false, "Option G", group));

        assertEquals(1, parentOpts.getOptions().size());
        assertEquals(group, parentOpts.getOptionGroups().iterator().next());
        assertEquals(1, group.getOptions().size());
        assertEquals("Option G", group.getOptions().get(0).getLongOpt());
    }

    @Test
    public void testAddOptionWithRequiresArg() {
        Options opts = new Options();
        Option opt = new Option("a", "a", true, "Option A with value");
        opts.addOption(opt);

        assertEquals(1, opts.getOptions().size());
        assertEquals(opt, opts.getOptions().get(0));
        assertTrue(opt.isRequired());
    }

    @Test
    public void testAddOptionGroupWithRequiresArg() {
        Options parentOpts = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");
        group.setRequired(true);

        parentOpts.addOptionGroup(group);

        assertEquals(1, parentOpts.getOptions().size());
        assertEquals(group, parentOpts.getOptionGroups().iterator().next());
        assertTrue(group.isRequired());
    }

    @Test
    public void testGetOption() {
        Options opts = new Options();
        Option opt = new Option("a", "a", false, "Option A");
        opts.addOption(opt);

        assertEquals(opt, opts.getOption("a"));
        assertEquals(opt, opts.getOption("a"));
        assertNull(opts.getOption("b"));
    }

    @Test
    public void testGetOptionWithLongName() {
        Options opts = new Options();
        Option opt = new Option("a", "option-a", false, "Option A with long name");
        opts.addOption(opt);

        assertEquals(opt, opts.getOption("option-a"));
        assertEquals(opt, opts.getOption("option-a"));
        assertNull(opts.getOption("b"));
    }

    @Test
    public void testGetLongOption() {
        Options opts = new Options();
        Option opt = new Option("a", "option-a", false, "Option A with long name");
        opts.addOption(opt);

        assertEquals(opt, opts.getOption("--option-a"));
        assertEquals(opt, opts.getOption("--option-a"));
        assertNull(opts.getOption("--b"));
    }

    @Test
    public void testGetShortOption() {
        Options opts = new Options();
        Option opt = new Option("a", "a", false, "Option A with short name");
        opts.addOption(opt);

        assertEquals(opt, opts.getOption("-a"));
        assertEquals(opt, opts.getOption("-a"));
        assertNull(opts.getOption("-b"));
    }

    @Test
    public void testGetOptionWithNoMatchingOption() {
        Options opts = new Options();
        Option opt1 = new Option("a", "a", false, "Option A with short name");
        Option opt2 = new Option("b", "b", false, "Option B with short name");
        opts.addOption(opt1);
        opts.addOption(opt2);

        assertNull(opts.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options opts = new Options();
        Option opt1 = new Option("opt", "opt", false, "Option with partial match 1");
        Option opt2 = new Option("optopt", "optopt", false, "Option with partial match 2");
        opts.addOption(opt1);
        opts.addOption(opt2);

        List<String> matchingOpts = opts.getMatchingOptions("opt");

        assertEquals(2, matchingOpts.size());
        assertTrue(matchingOpts.contains("opt"));
        assertTrue(matchingOpts.contains("optopt"));
    }

    @Test
    public void testHasOption() {
        Options opts = new Options();
        Option opt = new Option("a", "a", false, "Option A");
        opts.addOption(opt);

        assertTrue(opts.hasOption("a"));
        assertTrue(opts.hasOption("a"));
        assertFalse(opts.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options opts = new Options();
        Option opt = new Option("a", "option-a", false, "Option A with long name");
        opts.addOption(opt);

        assertTrue(opts.hasLongOption("option-a"));
        assertTrue(opts.hasLongOption("option-a"));
        assertFalse(opts.hasLongOption("option-b"));
    }

    @Test
    public void testHasShortOption() {
        Options opts = new Options();
        Option opt = new Option("a", "a", false, "Option A with short name");
        opts.addOption(opt);

        assertTrue(opts.hasShortOption("a"));
        assertTrue(opts.hasShortOption("a"));
        assertFalse(opts.hasShortOption("b"));
    }

    @Test
    public void testHasOptionGroup() {
        Options parentOpts = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");
        parentOpts.addOptionGroup(group);

        assertTrue(parentOpts.hasOptionGroup("group"));
        assertTrue(parentOpts.hasOptionGroup("group"));
        assertFalse(parentOpts.hasOptionGroup("unknown"));
    }

    @Test
    public void testToString() {
        Options opts = new Options();
        Option opt1 = new Option("a", "a", false, "Option A");
        Option opt2 = new Option("b", "b", false, "Option B");
        opts.addOption(opt1);
        opts.addOption(opt2);

        String expected = "[ Options: [ short {a=Option A}, {b=Option B}] [ long {} ]]";
        assertEquals(expected, opts.toString());
    }
}