package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertIterableEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testEmptyOptions() {
        Options options = new Options();
        assertEquals(Collections.emptyMap(), options.getShortOpts());
        assertEquals(Collections.emptyMap(), options.getLongOpts());
        assertEquals(Collections.emptyList(), options.getRequiredOpts());
        assertEquals(Collections.emptyMap(), options.getOptionGroups());
    }

    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        Option opt1 = new Option("o", "opt1", false, "short option 1");
        Option opt2 = new Option("l", "opt2", true, "long option 2");
        Option opt3 = new Option("g", "opt-group", false, "option group");

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOptionGroup(new OptionGroup("g", "group option", true, "group options"));

        assertEquals(1, options.getShortOpts().size());
        assertEquals(1, options.getLongOpts().size());
        assertEquals(1, options.getRequiredOpts().size());
        assertEquals(1, options.getOptionGroups().size());

        assertEquals(opt1, options.getShortOpts().get(opt1.getKey()));
        assertEquals(opt2, options.getLongOpts().get(opt2.getLongOpt()));
        assertEquals(opt3, options.getOptionGroups().get(opt3.getKey()));

        assertEquals(opt1.getKey(), options.getShortOpt(opt1.getKey()).getKey());
        assertEquals(opt2.getLongOpt(), options.getLongOpt(opt2.getLongOpt()).getLongOpt());
        assertEquals(opt3, options.getOptionGroup(opt3));

        assertFalse(options.hasOption("nonexistent"));
        assertFalse(options.hasLongOption("nonexistent"));
        assertFalse(options.hasShortOption("nonexistent"));

        assertNull(options.getShortOpt("nonexistent"));
        assertNull(options.getLongOpt("nonexistent"));
        assertNull(options.getOptionGroup("nonexistent"));

        assertSame(opt1, options.getShortOpt(opt1.getKey()));
        assertSame(opt2, options.getLongOpt(opt2.getLongOpt()));
        assertSame(opt3, options.getOptionGroup(opt3.getKey()));

        assertEquals("short option 1", opt1.getDescription());
        assertEquals("long option 2", opt2.getDescription());
        assertEquals("group options", opt3.getDescription());

        assertEquals(opt1, options.getOption("o"));
        assertEquals(opt1, options.getOption("o", true));
        assertEquals(opt1, options.getOption("--opt1"));
        assertEquals(opt1, options.getOption("--opt1="));

        assertEquals(opt2, options.getOption("l"));
        assertEquals(opt2, options.getOption("--opt2"));
        assertEquals(opt2, options.getOption("--opt2="));

        assertEquals(opt3, options.getOptionGroup("g"));

        assertEquals(1, options.getMatchingOptions("opt1").size());
        assertEquals(1, options.getMatchingOptions("opt2").size());
        assertEquals(1, options.getMatchingOptions("opt-group").size());
        assertEquals(0, options.getMatchingOptions("nonexistent").size());

        assertEquals(1, options.getOptions().size());
        assertEquals(opt1, options.getOptions().get(0));

        assertTrue(options.toString().contains("short"));
        assertTrue(options.toString().contains("long"));
    }

    @Test
    public void testComplexOptions() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "group 1", false, "group 1 options");
        Option opt1 = new Option("o", "opt1", false, "short option 1");
        Option opt2 = new Option("l", "opt2", true, "long option 2");
        Option opt3 = new Option("g", "opt-group", false, "option group");
        Option opt4 = new Option("a", null, true, "option a");
        Option opt5 = new Option("b", null, true, "option b");
        Option opt6 = new Option("c", null, false, "option c");

        group1.addOption(opt1);
        group1.addOption(opt2);
        group1.addOption(opt3);
        group1.addOption(opt4);
        group1.addOption(opt5);
        group1.addOption(opt6);

        options.addOptionGroup(group1);
        options.addOption(opt6);

        assertEquals(1, options.getShortOpts().size());
        assertEquals(1, options.getLongOpts().size());
        assertEquals(2, options.getRequiredOpts().size());
        assertEquals(1, options.getOptionGroups().size());

        assertEquals(opt6, options.getShortOpts().get(opt6.getKey()));
        assertEquals(opt1, options.getLongOpts().get("opt1"));
        assertEquals(group1, options.getOptionGroups().get(opt3.getKey()));

        assertEquals(opt6, options.getShortOpt(opt6.getKey()));
        assertEquals(opt1, options.getLongOpt(opt1.getLongOpt()));
        assertEquals(group1, options.getOptionGroup(opt3));

        assertFalse(options.hasOption("nonexistent"));
        assertFalse(options.hasLongOption("nonexistent"));
        assertFalse(options.hasShortOption("nonexistent"));

        assertNull(options.getShortOpt("nonexistent"));
        assertNull(options.getLongOpt("nonexistent"));
        assertNull(options.getOptionGroup("nonexistent"));

        assertSame(opt6, options.getShortOpt(opt6.getKey()));
        assertSame(opt1, options.getLongOpt(opt1.getLongOpt()));
        assertSame(group1, options.getOptionGroup(opt3));

        assertEquals("short option 1", opt1.getDescription());
        assertEquals("long option 2", opt2.getDescription());
        assertEquals("option group", opt3.getDescription());

        assertEquals(opt1, options.getOption("opt1"));
        assertEquals(opt1, options.getOption("opt1="));
        assertEquals(opt1, options.getOption("--opt1"));
        assertEquals(opt1, options.getOption("--opt1="));

        assertEquals(opt2, options.getOption("opt2"));
        assertEquals(opt2, options.getOption("opt2="));
        assertEquals(opt2, options.getOption("--opt2"));
        assertEquals(opt2, options.getOption("--opt2="));

        assertEquals(opt3, options.getOption("group"));
        assertEquals(opt3, options.getOption("--group"));

        assertEquals(opt4, group1.getOption("a"));
        assertEquals(opt4, group1.getOption("a="));
        assertEquals(opt4, group1.getOption("--a"));
        assertEquals(opt4, group1.getOption("--a="));

        assertEquals(opt5, group1.getOption("b"));
        assertEquals(opt5, group1.getOption("b="));
        assertEquals(opt5, group1.getOption("--b"));
        assertEquals(opt5, group1.getOption("--b="));

        assertEquals(opt6, group1.getOption("c"));
        assertEquals(opt6, group1.getOption("c="));
        assertEquals(opt6, group1.getOption("--c"));
        assertEquals(opt6, group1.getOption("--c="));

        assertEquals("option group options", group1.getDescription());

        assertEquals(1, options.getMatchingOptions("opt1").size());
        assertEquals(1, options.getMatchingOptions("opt2").size());
        assertEquals(1, options.getMatchingOptions("opt-group").size());
        assertEquals(5, options.getMatchingOptions("group").size());
        assertEquals(1, options.getMatchingOptions("opt-group2").size());
        assertEquals(0, options.getMatchingOptions("nonexistent").size());

        List<Option> optionsList = Arrays.asList(opt1, opt2, opt3, opt4, opt5, opt6);
        assertEquals(optionsList, options.getOptions());

        List<Option> groupOptionsList = Arrays.asList(opt1, opt2, opt3, opt4, opt5);
        assertEquals(groupOptionsList, group1.getOptions());

        assertTrue(options.toString().contains("short"));
        assertTrue(options.toString().contains("long"));
        assertTrue(options.toString().contains("option group options"));
        assertTrue(options.toString().contains("group 1 options"));
    }
}