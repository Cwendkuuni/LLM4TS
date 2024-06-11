package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", true, "test option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertEquals(option, options.getOption("o"));
        assertEquals(option, options.getOption("--option"));
    }

    @Test
    public void testAddOptionWithShortNameOnly() {
        options.addOption("s", "short", false, "short option");

        assertEquals("s", options.getOption("s").getOpt());
        assertEquals("short", options.getOption("s").getLongOpt());
        assertFalse(options.getOption("s").hasArg());
        assertEquals("short option", options.getOption("s").getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndArg() {
        options.addOption("a", true, "argument option");

        assertEquals("a", options.getOption("a").getOpt());
        assertNull(options.getOption("a").getLongOpt());
        assertTrue(options.getOption("a").hasArg());
        assertEquals("argument option", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        options.addOption("o", "option", true, "test option");

        assertEquals("o", options.getOption("o").getOpt());
        assertEquals("option", options.getOption("o").getLongOpt());
        assertTrue(options.getOption("o").hasArg());
        assertEquals("test option", options.getOption("o").getDescription());
    }

    @Test
    public void testAddOptionInstance() {
        Option option = new Option("t", "test", false, "test option");
        options.addOption(option);

        assertEquals(option, options.getOption("t"));
        assertEquals(option, options.getOption("--test"));
    }

    @Test
    public void testGetOptions() {
        options.addOption("a", "arg", true, "argument option");
        options.addOption("o", "option", false, "test option");

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(options.getOption("a")));
        assertTrue(options.getOptions().contains(options.getOption("o")));
    }

    @Test
    public void testGetRequiredOptions() {
        options.addOption("r", true, "required option");
        options.addOption("o", "optional", false, "optional option");

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(options.getOption("r")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test", true, "test option");
        options.addOption(option);

        assertEquals(option, options.getOption("t"));
        assertEquals(option, options.getOption("--test"));
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("o", false, "option");
        options.addOption("op", false, "option");
        options.addOption("opt", false, "option");

        assertEquals(1, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("o"));
        assertEquals(2, options.getMatchingOptions("op").size());
        assertTrue(options.getMatchingOptions("op").contains("op"));
        assertTrue(options.getMatchingOptions("op").contains("opt"));
        assertEquals(1, options.getMatchingOptions("opt").size());
        assertTrue(options.getMatchingOptions("opt").contains("opt"));
    }

    @Test
    public void testHasOption() {
        options.addOption("o", false, "option");
        options.addOption("opt", true, "option");

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("no"));
        assertTrue(options.hasOption("--option"));
        assertTrue(options.hasOption("opt"));
        assertTrue(options.hasOption("--opt"));
        assertFalse(options.hasOption("opt1"));
    }

    @Test
    public void testHasLongOption() {
        options.addOption("o", false, "option");
        options.addOption("opt", true, "option");

        assertFalse(options.hasLongOption("o"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasLongOption("opt"));
        assertFalse(options.hasLongOption("no"));
    }

    @Test
    public void testHasShortOption() {
        options.addOption("o", false, "option");
        options.addOption("opt", true, "option");

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("option"));
        assertFalse(options.hasShortOption("opt"));
        assertFalse(options.hasShortOption("no"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", true, "test option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        options.addOption("o", false, "option");
        options.addOption("opt", true, "option");

        String expected = "[ Options: [ short {o=option, opt=option} ] [ long {option=option} ]";
        assertEquals(expected, options.toString());
    }
}