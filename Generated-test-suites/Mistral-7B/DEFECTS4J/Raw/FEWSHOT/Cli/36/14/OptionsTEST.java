package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "a", false, "Option A");

        options.addOption(option);
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("a"));
        assertFalse(options.hasOption("b"));

        Option optionWithArg = new Option("b", "b", true, "Option B with argument");
        options.addOption(optionWithArg);
        assertFalse(options.hasOption("b"));
        assertTrue(options.hasOption("b", true));
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options options = new Options();
        Option option = new Option('a', "optionA,optionB", false, "Option A");

        options.addOption(option);
        assertEquals(option, options.getOption("optionA"));
        assertEquals(option, options.getOption("optionB"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("a", true));
        assertEquals(option, options.getOption("optionB", true));

        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group", "Group");
        Options options = new Options();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);
        assertEquals(2, options.getOptions().size());
        assertEquals(option1, options.getOption("o1"));
        assertEquals(option2, options.getOption("o2"));
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOptionGroupWithOption() {
        OptionGroup group = new OptionGroup("group", "Group");
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);

        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals(option, options.getOption("o"));
        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", null, false, "Option 1");
        Option option2 = new Option("o2", null, false, "Option 2");
        Option option3 = new Option("o3", "o3long", false, "Option 3 long");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> optionsWithPrefix = options.getMatchingOptions("o");
        assertEquals(3, optionsWithPrefix.size());
        assertTrue(optionsWithPrefix.contains("o1"));
        assertTrue(optionsWithPrefix.contains("o2"));
        assertTrue(optionsWithPrefix.contains("o3"));
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        Options options = new Options();

        List<String> optionsWithPrefix = options.getMatchingOptions("x");
        assertTrue(optionsWithPrefix.isEmpty());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("o1", null, false, "Option 1");
        Option option2 = new Option("o2", null, false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("o1", true));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("o2", true));
        assertFalse(options.hasOption("x"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option('o', "o1", false, "Option 1");
        Option option2 = new Option("o2long", null, false, "Option 2 long");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("o1"));
        assertTrue(options.hasLongOption("o1long"));
        assertFalse(options.hasLongOption("x"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option('o', "o1", false, "Option 1");

        options.addOption(option1);

        assertTrue(options.hasShortOption("o"));
        assertTrue(options.hasShortOption("o1"));
        assertFalse(options.hasShortOption("x"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        Option option = new Option("o", null, false, "Option");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", null, false, "Option 1");
        Option option2 = new Option("o2", null, false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        options.addOption(new OptionGroup(null, "Group"));
        options.addOption(new OptionGroup(null, "Group2"));

        options.addOption(new Option("r1", null, true, "Required Option 1"));
        options.addOption(new Option("r2", null, true, "Required Option 2"));

        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(2, requiredOptions.size());
        assertEquals(requiredOptions.get(0), options.getOption("r1"));
        assertEquals(requiredOptions.get(1), options.getOption("r2"));
    }
}