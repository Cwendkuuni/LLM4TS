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
        options.addOption("a", "alpha", true, "Alpha option");
        options.addOption("b", "beta", false, "Beta option");
        options.addOption("c", false, "Gamma option");
    }

    @Test
    public void testAddOption() {
        Option option = new Option("d", "delta", true, "Delta option");
        options.addOption(option);

        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("delta"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("e", "epsilon", false, "Epsilon option"));
        optionGroup.addOption(new Option("f", "phi", true, "Phi option"));
        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("e"));
        assertTrue(options.hasOption("f"));
    }

    @Test
    public void testGetOption() {
        Option option = options.getOption("a");

        assertEquals("a", option.getOpt());
        assertEquals("alpha", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Alpha option", option.getDescription());
    }

    @Test
    public void testHasOption() {
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("d"));
    }

    @Test
    public void testHasLongOption() {
        assertTrue(options.hasLongOption("alpha"));
        assertFalse(options.hasLongOption("delta"));
    }

    @Test
    public void testHasShortOption() {
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("d"));
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("x", "xi", false, "Xi option");
        options.addOption("y", "omega", false, "Omega option");

        assertEquals(2, options.getMatchingOptions("x").size());
        assertEquals(1, options.getMatchingOptions("om").size());
        assertTrue(options.getMatchingOptions("de").isEmpty());
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("g", "gamma", false, "Gamma option"));
        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(options.getOption("g")));
    }

    @Test
    public void testToString() {
        String expected = "[ Options: [ short {a:alpha, b:beta, c:gamma} ] [ long {alpha=Option[ opt: a, longOpt: alpha, hasArg: true, description: Alpha option]}, beta=Option[ opt: b, longOpt: beta, hasArg: false, description: Beta option], gamma=Option[ opt: , longOpt: null, hasArg: false, description: Gamma option]} ]";
        assertEquals(expected, options.toString());
    }
}