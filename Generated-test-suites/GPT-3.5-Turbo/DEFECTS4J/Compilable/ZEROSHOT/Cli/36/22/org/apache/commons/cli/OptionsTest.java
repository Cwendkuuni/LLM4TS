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
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", true, "Description 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", true, "Description");
        options.addOption(option);
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOption_shortNameOnly() {
        String opt = "a";
        String description = "Description";
        options.addOption(opt, description);
        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testAddOption_shortNameWithArg() {
        String opt = "a";
        boolean hasArg = true;
        String description = "Description";
        options.addOption(opt, hasArg, description);
        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testAddOption_shortAndLongName() {
        String opt = "a";
        String longOpt = "option";
        boolean hasArg = true;
        String description = "Description";
        options.addOption(opt, longOpt, hasArg, description);
        assertTrue(options.hasOption(opt));
        assertTrue(options.hasOption(longOpt));
        assertEquals(options.getOption(opt).getLongOpt(), longOpt);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", true, "Description 2");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(options.getOption("a"), option1);
        assertEquals(options.getOption("option2"), option2);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("option2", "option2", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(options.getMatchingOptions("a").size(), 1);
        assertEquals(options.getMatchingOptions("option").size(), 2);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option", true, "Description");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option", true, "Description");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option", true, "Description");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "option", true, "Description");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(options.getOptionGroup(option), group);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", true, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {a=Option - a(option1): Description 1, b=Option - b(option2): Description 2 } ] [ long {option1=Option - a(option1): Description 1, option2=Option - b(option2): Description 2 } ]";
        assertEquals(options.toString(), expected);
    }
}