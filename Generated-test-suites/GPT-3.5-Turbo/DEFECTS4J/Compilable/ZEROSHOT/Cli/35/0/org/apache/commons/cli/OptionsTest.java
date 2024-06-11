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
        group.addOption(new Option("a", "option a"));
        group.addOption(new Option("b", "option b"));
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("c", "option c");
        options.addOption(option);
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOptionWithHasArg() {
        Option option = new Option("d", "option d", true, "has arg");
        options.addOption(option);
        assertTrue(options.hasOption("d"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("e", "option e");
        options.addOption(option);
        assertEquals(option, options.getOption("e"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "option f");
        options.addOption(option1);
        Option option2 = new Option("foo", "option foo");
        options.addOption(option2);
        assertEquals(1, options.getMatchingOptions("f").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("g", "option g");
        options.addOption(option);
        assertTrue(options.hasOption("g"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("h", "option h");
        options.addOption(option);
        assertTrue(options.hasLongOption("option h"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("i", "option i");
        options.addOption(option);
        assertTrue(options.hasShortOption("i"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("j", "option j");
        group.addOption(option);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option));
    }

}