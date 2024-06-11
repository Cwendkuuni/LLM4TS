package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        Option opt1 = new Option("f", false, "Option 1");
        Option opt2 = new Option("g", false, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        options.addOption("a", "Option A", false, "Description A");
        options.addOption("b", true, "Description B");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testGetOptions() {
        options.addOption("c", "Option C", false, "Description C");
        options.addOption("d", true, "Description D");

        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        Option opt1 = new Option("e", false, "Option E");
        Option opt2 = new Option("f", true, "Option F");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);

        options.addOptionGroup(group);

        assertTrue(options.getRequiredOptions().contains(opt1));
        assertFalse(options.getRequiredOptions().contains(opt2));
    }

    @Test
    public void testGetOption() {
        Option opt1 = new Option("g", false, "Option G");
        Option opt2 = new Option("h", true, "Option H");
        options.addOption(opt1);
        options.addOption(opt2);

        assertEquals(opt1, options.getOption("g"));
        assertEquals(opt2, options.getOption("h"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option opt1 = new Option("i", "long1", false, "Option I");
        Option opt2 = new Option("j", "long2", false, "Option J");
        options.addOption(opt1);
        options.addOption(opt2);

        assertEquals(1, options.getMatchingOptions("long1").size());
        assertEquals(1, options.getMatchingOptions("long2").size());
        assertEquals(0, options.getMatchingOptions("long").size());
    }

    @Test
    public void testHasOption() {
        options.addOption("k", "Option K", false, "Description K");
        options.addOption("l", true, "Description L");

        assertTrue(options.hasOption("k"));
        assertTrue(options.hasOption("l"));
    }

    @Test
    public void testHasLongOption() {
        options.addOption("m", "Option M", false, "Description M");

        assertTrue(options.hasLongOption("Option M"));
        assertFalse(options.hasLongOption("Option N"));
    }

    @Test
    public void testHasShortOption() {
        options.addOption("n", "Option N", false, "Description N");

        assertTrue(options.hasShortOption("n"));
        assertFalse(options.hasShortOption("o"));
    }

    @Test
    public void testGetOptionGroup() {
        Option opt1 = new Option("p", false, "Option P");
        Option opt2 = new Option("q", false, "Option Q");
        OptionGroup group = new OptionGroup();
        group.addOption(opt1);
        group.addOption(opt2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(opt1));
        assertEquals(group, options.getOptionGroup(opt2));
    }

    @Test
    public void testToString() {
        Option opt1 = new Option("r", false, "Option R");
        Option opt2 = new Option("s", false, "Option S");
        options.addOption(opt1);
        options.addOption(opt2);

        String expected = "[ Options: [ short {r=Option R, s=Option S} ] [ long {} ]";
        assertEquals(expected, options.toString());
    }

}