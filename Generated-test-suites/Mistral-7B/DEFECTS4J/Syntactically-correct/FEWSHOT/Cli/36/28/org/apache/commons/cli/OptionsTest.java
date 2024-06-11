package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroupAndGetOptionGroups() {
        OptionGroup optionGroup = new OptionGroup("TestGroup");
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(optionGroup));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test", false, "Test option");
        Options options = new Options();
        options.addOption(option);
        Option testOption = options.getOption("t");
        assertSame(option, testOption);
    }

    @Test
    public void testGetOptionWithLongName() {
        Option option = new Option("test-long", "Test long option", false, "Test long option");
        Options options = new Options();
        options.addOption(option);
        Option testLongOption = options.getOption("test-long");
        assertSame(option, testLongOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("opt1", "Option 1", false, "Description 1");
        Option option2 = new Option("opt2", "Option 2", false, "Description 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertTrue(matchingOptions.containsAll(Arrays.asList("opt1", "opt2")));
    }

    @Test
    public void testHasOptionWithShortName() {
        Option option = new Option("t", "test", false, "Test option");
        Options options = new Options();
        options.addOption(option);
        assertTrue(options.hasShortOption("t"));
    }

    @Test
    public void testHasOptionWithLongName() {
        Option option = new Option("test-long", "Test long option", false, "Test long option");
        Options options = new Options();
        options.addOption(option);
        assertTrue(options.hasLongOption("test-long"));
    }

    @Test
    public void testHasNoOption() {
        Options options = new Options();
        assertFalse(options.hasOption(""));
    }

    @Test
    public void testHasOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("TestGroup");
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        assertTrue(options.hasOptionGroup("TestGroup"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("t1", "Option 1", false, "Description 1");
        Option option2 = new Option("t2", "Option 2", false, "Description 2");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {t1=Option{key='t1', longOpt='', hasArg=false, values=[], description='Description 1'}} ] [ long {} ] ]";
        String actual = options.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("t", "test", true, "Test option");
        Options options = new Options();
        options.addOption(option);

        List<Option> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.contains(option));
    }
}