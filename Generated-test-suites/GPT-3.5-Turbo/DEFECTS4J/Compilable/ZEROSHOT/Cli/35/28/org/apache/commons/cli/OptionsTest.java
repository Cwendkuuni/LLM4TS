package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "option a"));
        group.addOption(new Option("b", "option b"));
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);

        assertNotNull(options.getOption("a"));
    }

    @Test
    public void testAddOption_shortAndLongName() {
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);

        assertNotNull(options.getOption("a"));
        assertNotNull(options.getOption("option a"));
    }

    @Test
    public void testAddOption_shortNameWithArgument() {
        Options options = new Options();
        Option option = new Option("a", true, "option a");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option a"));
    }

    @Test
    public void testAddOption_longNameWithArgument() {
        Options options = new Options();
        Option option = new Option(null, "option", true, "option a");
        options.addOption(option);

        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        options.addOption(optionA);
        options.addOption(optionB);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(optionA));
        assertTrue(options.getOptions().contains(optionB));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        optionA.setRequired(true);
        options.addOption(optionA);
        options.addOption(optionB);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("a"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        options.addOption(optionA);

        assertEquals(optionA, options.getOption("a"));
        assertEquals(optionA, options.getOption("option a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        options.addOption(optionA);
        options.addOption(optionB);

        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("option a"));
        assertTrue(options.getMatchingOptions("o").contains("option b"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option a"));
        assertFalse(options.hasOption("b"));
        assertFalse(options.hasOption("option b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option(null, "option", true, "option a");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("option a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "option a"));
        group.addOption(new Option("b", "option b"));
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(options.getOption("a")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        options.addOption(optionA);
        options.addOption(optionB);

        String expected = "[ Options: [ short {a=Option{opt=a, longOpt=null, hasArg=false, description='option a'}}, {b=Option{opt=b, longOpt=null, hasArg=false, description='option b'}} ] [ long {}]";

        assertEquals(expected, options.toString());
    }
}