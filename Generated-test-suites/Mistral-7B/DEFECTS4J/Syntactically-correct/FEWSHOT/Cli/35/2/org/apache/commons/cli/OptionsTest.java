package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group", true, "test");
        Options options = new Options();
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetOptionGroupsEmpty() {
        Options options = new Options();
        assertTrue(options.getOptionGroups().isEmpty());
    }

    @Test
    public void testGetOptionGroupsWithOptions() {
        OptionGroup group = new OptionGroup("testGroup", "Test Group", true, "test");
        Options options = new Options();
        options.addOptionGroup(group);
        Option opt = new Option("t", "test", false, "Test option");
        options.addOption(opt);
        assertTrue(options.getOptionGroups().contains(group));
        assertEquals(1, options.getOptionGroups().size());
    }

    @Test
    public void testHasOptionEmpty() {
        Options options = new Options();
        assertFalse(options.hasOption(""));
    }

    @Test
    public void testHasOptionShort() {
        Options options = new Options();
        Option opt = new Option("t", "test", false, "Test option");
        options.addOption(opt);
        assertTrue(options.hasOption("t"));
    }

    @Test
    public void testHasOptionLong() {
        Options options = new Options();
        Option opt = new Option("test", "longTest", false, "Test long option");
        options.addOption(opt);
        assertTrue(options.hasLongOption("longTest"));
    }

    @Test
    public void testHasOptionMissing() {
        Options options = new Options();
        assertFalse(options.hasOption("missing"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option opt = new Option("t", "test", false, "Test option");
        options.addOption(opt);
        Option returnedOpt = options.getOption("t");
        assertEquals(opt, returnedOpt);
    }

    @Test
    public void testGetOptionMissing() {
        Options options = new Options();
        assertNull(options.getOption("missing"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option opt1 = new Option("t", "test1", false, "Test option 1");
        Option opt2 = new Option("test", "test2", false, "Test option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        List<String> optionsWithTest = options.getMatchingOptions("test");
        assertEquals(2, optionsWithTest.size());
        assertTrue(optionsWithTest.contains("test1"));
        assertTrue(optionsWithTest.contains("test2"));
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        Options options = new Options();
        List<String> optionsWithTest = options.getMatchingOptions("test");
        assertTrue(optionsWithTest.isEmpty());
    }
}