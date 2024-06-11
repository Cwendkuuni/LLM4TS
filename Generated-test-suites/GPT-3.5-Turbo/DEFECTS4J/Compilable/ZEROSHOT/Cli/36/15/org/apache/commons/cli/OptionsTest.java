package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertEquals(option, options.getOption("o"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("o").size());
        assertEquals("option1", options.getMatchingOptions("o").get(0));

        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("option1"));
        assertTrue(options.getMatchingOptions("option").contains("option1"));
        assertTrue(options.getMatchingOptions("option").contains("option2"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("p"));
        assertFalse(options.hasOption("other"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("o"));
        assertFalse(options.hasLongOption("p"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("option"));
        assertFalse(options.hasShortOption("p"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();

        assertEquals("[ Options: [ short {} ] [ long {} ]", options.toString());
    }
}