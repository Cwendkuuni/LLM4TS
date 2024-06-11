package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test group options");
        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals(1, options.getOptionGroups().size());
        assertEquals(group, options.getOptionGroups().iterator().next());
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        OptionGroup group = new OptionGroup("testGroup", "Test group options");
        Option opt = new Option("o", "option", true, "Option to test");
        group.addOption(opt);

        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals(1, options.getOptionGroups().size());
        assertEquals(group, options.getOptionGroups().iterator().next());
        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testAddOptionShort() {
        Options options = new Options();
        options.addOption('o', "option", false, "Option to test");

        assertEquals(1, options.getOptions().size());
        Option opt = options.getOptions().get(0);
        assertEquals('o', opt.getKey());
        assertEquals("option", opt.getLongOpt());
        assertEquals(false, opt.hasArg());
        assertEquals("Option to test", opt.getDescription());
        assertFalse(opt.isRequired());
        assertNull(opt.getValues());
    }

    @Test
    public void testAddOptionShortWithArg() {
        Options options = new Options();
        options.addOption('o', "option", true, "Option to test");

        assertEquals(1, options.getOptions().size());
        Option opt = options.getOptions().get(0);
        assertEquals('o', opt.getKey());
        assertEquals("option", opt.getLongOpt());
        assertEquals(true, opt.hasArg());
        assertEquals("Option to test", opt.getDescription());
        assertFalse(opt.isRequired());
        assertNull(opt.getValues());
    }

    @Test
    public void testAddOptionShortWithArgAndValue() {
        Options options = new Options();
        options.addOption('o', "option", true, "Option to test")
                .setValueSeparator('=')
                .setValue("value");

        assertEquals(1, options.getOptions().size());
        Option opt = options.getOptions().get(0);
        assertEquals('o', opt.getKey());
        assertEquals("option", opt.getLongOpt());
        assertEquals(true, opt.hasArg());
        assertEquals("Option to test", opt.getDescription());
        assertTrue(opt.isRequired());
        assertEquals("value", opt.getValues()[0]);
    }

    @Test
    public void testAddOptionLong() {
        Options options = new Options();
        options.addOption("option", null, false, "Option to test");

        assertEquals(1, options.getOptions().size());
        Option opt = options.getOptions().get(0);
        assertEquals(null, opt.getKey());
        assertEquals("option", opt.getLongOpt());
        assertEquals(false, opt.hasArg());
        assertEquals("Option to test", opt.getDescription());
        assertFalse(opt.isRequired());
        assertNull(opt.getValues());
    }

    @Test
    public void testAddOptionLongWithArg() {
        Options options = new Options();
        options.addOption("option", null, true, "Option to test");

        assertEquals(1, options.getOptions().size());
        Option opt = options.getOptions().get(0);
        assertEquals(null, opt.getKey());
        assertEquals("option", opt.getLongOpt());
        assertEquals(true, opt.hasArg());
        assertEquals("Option to test", opt.getDescription());
        assertFalse(opt.isRequired());
        assertNull(opt.getValues());
    }

    @Test
    public void testAddOptionLongWithArgAndValue() {
        Options options = new Options();
        options.addOption("option", "valOption", true, "Option to test")
                .setValueSeparator('=')
                .setValue("value");

        assertEquals(1, options.getOptions().size());
        Option opt = options.getOptions().get(0);
        assertEquals("valOption", opt.getKey());
        assertEquals("option", opt.getLongOpt());
        assertEquals(true, opt.hasArg());
        assertEquals("Option to test", opt.getDescription());
        assertTrue(opt.isRequired());
        assertEquals("value", opt.getValues()[0]);
    }

    @Test
    public void testAddOptionWithValue() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option to test");
        opt.setValueSeparator('=');
        opt.setValue("value");
        options.addOption(opt);

        assertEquals(1, options.getOptions().size());
        Option testOpt = options.getOptions().get(0);
        assertEquals('o', testOpt.getKey());
        assertEquals("option", testOpt.getLongOpt());
        assertEquals(true, testOpt.hasArg());
        assertEquals("Option to test", testOpt.getDescription());
        assertTrue(testOpt.isRequired());
        assertEquals("value", testOpt.getValues()[0]);
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option to test");
        opt.setValueSeparator('=');
        opt.setValue("value");
        options.addOption(opt);

        Option testOpt = options.getOption("o");
        assertEquals(opt, testOpt);

        testOpt = options.getOption("option");
        assertEquals(opt, testOpt);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option opt1 = new Option("opt1", null, false, "Test option 1");
        Option opt2 = new Option("opt1a", null, false, "Test option 1a");
        Option opt3 = new Option("opt2", null, false, "Test option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        List<String> matchingOptions = options.getMatchingOptions("opt1");
        assertEquals(2, matchingOptions.size());
        assertSame(opt1, options.getOption(matchingOptions.get(0)));
        assertSame(opt2, options.getOption(matchingOptions.get(1)));

        matchingOptions = options.getMatchingOptions("optX");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option to test");
        opt.setValueSeparator('=');
        opt.setValue("value");
        options.addOption(opt);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("x"));
        assertFalse(options.hasLongOption("x"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option to test");
        opt.setValueSeparator('=');
        opt.setValue("value");
        options.addOption(opt);

        String expected = "[ Options: [ short {o=value} ] [ long {}option=value]]";
        assertEquals(expected, options.toString());
    }
}