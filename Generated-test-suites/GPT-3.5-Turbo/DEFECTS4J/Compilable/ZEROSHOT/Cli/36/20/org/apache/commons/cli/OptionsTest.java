package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(new Option("o", "option", false, "Option description"));
        Options options = new Options();
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        option.setRequired(true);
        options.addOption(option);
        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        assertEquals(option, options.getOption("o"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1 description");
        Option option2 = new Option("o2", "option2", false, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.getMatchingOptions("o").contains("o1"));
        assertTrue(options.getMatchingOptions("o").contains("o2"));
        assertFalse(options.getMatchingOptions("o").contains("o3"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("o"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("option"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option description");
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);
        assertEquals("[ Options: [ short {o=Option(key: o, longOpt: option, hasArg: false, description: Option description)} ] [ long {option=Option(key: o, longOpt: option, hasArg: false, description: Option description)} ]", options.toString());
    }
}