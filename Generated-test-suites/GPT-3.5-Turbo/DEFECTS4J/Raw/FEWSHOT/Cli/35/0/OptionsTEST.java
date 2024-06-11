package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        options.addOption(option1);
        assertTrue(options.hasOption("a"));
        assertEquals(option1, options.getOption("a"));

        Option option2 = new Option("b", "option B");
        options.addOption(option2);
        assertTrue(options.hasOption("b"));
        assertEquals(option2, options.getOption("b"));

        Option option3 = new Option("c", "option C");
        options.addOption(option3);
        assertTrue(options.hasOption("c"));
        assertEquals(option3, options.getOption("c"));
    }

    @Test
    public void testAddOption_withLongOpt() {
        Options options = new Options();
        Option option1 = new Option("a", "optionA");
        options.addOption(option1);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("optionA"));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("optionA"));

        Option option2 = new Option("b", "optionB");
        options.addOption(option2);
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("optionB"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("optionB"));

        Option option3 = new Option("c", "optionC");
        options.addOption(option3);
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasLongOption("optionC"));
        assertEquals(option3, options.getOption("c"));
        assertEquals(option3, options.getOption("optionC"));
    }

    @Test
    public void testAddOption_withHasArg() {
        Options options = new Options();
        Option option1 = new Option("a", "option A", true);
        options.addOption(option1);
        assertTrue(options.hasOption("a"));
        assertEquals(option1, options.getOption("a"));

        Option option2 = new Option("b", "option B", true);
        options.addOption(option2);
        assertTrue(options.hasOption("b"));
        assertEquals(option2, options.getOption("b"));

        Option option3 = new Option("c", "option C", true);
        options.addOption(option3);
        assertTrue(options.hasOption("c"));
        assertEquals(option3, options.getOption("c"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        OptionGroup group1 = new OptionGroup().addOption(option1).addOption(option2);
        options.addOptionGroup(group1);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group1, options.getOptionGroup(option2));

        Option option3 = new Option("c", "option C");
        OptionGroup group2 = new OptionGroup().addOption(option3);
        options.addOptionGroup(group2);
        assertTrue(options.hasOption("c"));
        assertEquals(group2, options.getOptionGroup(option3));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option option1 = new Option("a", "apple");
        Option option2 = new Option("b", "banana");
        Option option3 = new Option("c", "carrot");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("b").size());
        assertEquals(1, options.getMatchingOptions("c").size());
        assertEquals(0, options.getMatchingOptions("d").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option A"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("option B"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=option A, b=option B} ] [ long {option A=option A, option B=option B} ]";
        assertEquals(expected, options.toString());
    }
}