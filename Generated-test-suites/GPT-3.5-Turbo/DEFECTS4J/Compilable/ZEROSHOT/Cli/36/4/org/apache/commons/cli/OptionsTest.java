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
        OptionGroup group = new OptionGroup()
            .addOption(new Option("a", "add", false, "add option"))
            .addOption(new Option("r", "remove", false, "remove option"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        options.addOption(new Option("a", "add", false, "add option"));

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "option", true, "option with argument");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o", "option1", true, "option1 with argument");
        Option option2 = new Option("r", "required", true, "required option");
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "option with argument");
        options.addOption(option);

        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "option1", true, "option1 with argument");
        Option option2 = new Option("option2", true, "option2 with argument");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("op").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "option with argument");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", true, "option with argument");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", true, "option with argument");
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup()
            .addOption(new Option("a", "add", false, "add option"))
            .addOption(new Option("r", "remove", false, "remove option"));

        options.addOptionGroup(group);

        Option option = new Option("a", "add", false, "add option");

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "option1", true, "option1 with argument");
        Option option2 = new Option("r", "required", true, "required option");
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {o=option1, r=required} ] [ long {option1=option1, required=required} ]", options.toString());
    }
}