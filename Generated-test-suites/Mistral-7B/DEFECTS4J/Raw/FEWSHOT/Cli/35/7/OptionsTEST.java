package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionShort() {
        Options options = new Options();
        options.addOption('a', "optionA");
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertFalse(option.hasArg());

        options = new Options();
        options.addOption('a', "optionA").required(true);
        optionsList = options.helpOptions();
        option = optionsList.get(0);
        assertTrue(option.isRequired());
    }

    @Test
    public void testAddOptionLong() {
        Options options = new Options();
        options.addOption("optionB", "optionB");
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);
        assertEquals("optionB", option.getLongOpt());
        assertEquals(null, option.getOpt());
        assertFalse(option.hasArg());

        options = new Options();
        options.addOption("optionB", "optionB").required(true).argName("name");
        optionsList = options.helpOptions();
        option = optionsList.get(0);
        assertTrue(option.isRequired());
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testAddOptionShortLong() {
        Options options = new Options();
        options.addOption("optionC", "optionC", true);
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);
        assertEquals("optionC", option.getLongOpt());
        assertEquals("c", option.getOpt());
        assertTrue(option.hasArg());

        options = new Options();
        options.addOption("optionC", "optionC", true, "name");
        optionsList = options.helpOptions();
        option = optionsList.get(0);
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group", "group");
        group.addOption("optionGroupA", true);
        group.addOption("optionGroupB", false);

        Options options = new Options();
        options.addOptionGroup(group);
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(optionsList.size() - 1);
        assertEquals("group", option.getLongOpt());
        assertEquals("group", option.getOpt());
        assertTrue(option.isAllowedCombinationAllowed(null));

        Option groupOption = options.getOption("group");
        assertEquals(group, groupOption);
    }

    @Test
    public void testAddRequiredOptionGroup() {
        OptionGroup group = new OptionGroup("group", "group");
        group.addOption("optionGroupA", true);
        group.addOption("optionGroupB", false);

        Options options = new Options();
        options.addOptionGroup(group).required(true);
        List<Option> optionsList = options.helpOptions();
        assertTrue(options.getRequiredOptions().containsAll(Arrays.asList("group")));
        assertSame(group.getOptions().get(0), optionsList.get(optionsList.size() - 1));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("optionD", "optionD");

        assertTrue(options.hasOption("optionD"));
        assertTrue(options.hasOption("optionD", true));
        assertFalse(options.hasOption("optionE"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("optionF", "optionF");

        assertTrue(options.hasLongOption("optionF"));
        assertFalse(options.hasLongOption("optionG"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("optionH", "optionH", true);

        Option option = options.getOption("optionH");
        assertEquals("optionH", option.getLongOpt());
        assertEquals("h", option.getOpt());
        assertTrue(option.hasArg());
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNull() {
        Options options = new Options();
        options.getOption(null);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("optionI", "optionI");
        options.addOption("optionJ", "optionJ");
        options.addOption("optionK", "optionK");

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(1, matchingOptions.size());
        assertEquals("optionI", matchingOptions.get(0));
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        Options options = new Options();

        List<String> matchingOptions = options.getMatchingOptions("XX");
        assertTrue(matchingOptions.isEmpty());
    }
}