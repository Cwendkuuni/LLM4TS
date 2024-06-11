package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        OptionGroup group2 = new OptionGroup();
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("b", "bbb", true, "option 2");
        group1.addOption(option1);
        group2.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group2, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("b", "bbb", true, "option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("aaa"));
        assertTrue(options.hasLongOption("bbb"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("b", "bbb", true, "option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionList = options.getOptions();
        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("a", "aaa", true, "option 1");
        option.setRequired(true);
        Options options = new Options();
        options.addOption(option);
        List requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("b", "bbb", true, "option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("aa", "aaaa", true, "option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("aaa"));
        assertTrue(matchingOptions.contains("aaaa"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("b", "bbb", true, "option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("aaa"));
        assertTrue(options.hasLongOption("bbb"));
    }

    @Test
    public void testHasOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "aaa", true, "option 1");
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroup(option) != null);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "aaa", true, "option 1");
        Option option2 = new Option("b", "bbb", true, "option 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        assertEquals("[ Options: [ short {a=option 1, b=option 2} ] [ long {aaa=option 1, bbb=option 2} ]", options.toString());
    }
}