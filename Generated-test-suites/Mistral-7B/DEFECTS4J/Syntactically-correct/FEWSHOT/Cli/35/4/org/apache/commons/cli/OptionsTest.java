package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();
        Options options = new Options();

        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        assertEquals(2, options.getOptionGroups().size());
        assertTrue(options.getOptionGroups().contains(optionGroup1));
        assertTrue(options.getOptionGroups().contains(optionGroup2));
    }

    @Test
    public void testAddOptionGroupWithDuplicate() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();
        Options options = new Options();

        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup1);

        assertEquals(1, options.getOptionGroups().size());
        assertTrue(options.getOptionGroups().contains(optionGroup1));
    }

    @Test
    public void testAddOptionGroupWithDuplicateKey() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();
        Option opt1 = new Option("o", "opt1", false, "Option 1");
        Option opt2 = new Option("o", "opt2", false, "Option 2");
        optionGroup1.addOption(opt1);
        optionGroup2.addOption(opt2);
        Options options = new Options();

        options.addOptionGroup(optionGroup1);
        options.addOptionGroup(optionGroup2);

        assertEquals(1, options.getOptions().size());
        Option option = options.getOption("o");
        assertSame(opt1, option);
        assertTrue(option.getOptionGroup() != null);
        assertTrue(option.getOptionGroup() == optionGroup1.getOptions().get(0));
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option opt = new Option("o", "opt", true, "Required Option");
        optionGroup.addOption(opt);
        Options options = new Options();

        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().get(0) instanceof Option);
        assertSame(opt, options.getRequiredOptions().get(0));
    }

    @Test
    public void testAddOptionGroupWithRequiredOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option opt = new Option("o", "opt", true, "Required Option");
        optionGroup.addOption(opt);
        Options options = new Options();

        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().get(0) instanceof OptionGroup);
        assertSame(optionGroup, options.getRequiredOptions().get(0));
    }

    @Test
    public void testGetOptions() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Option opt4 = new Option("o4", "opt4", false, "Option 4");
        Option opt5 = new Option("o5", "opt5", false, "Option 5");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        options.addOption(opt4);
        options.addOption(opt5);

        List<Option> optionsList = options.getOptions();
        List<Option> expectedList = new ArrayList<>(Arrays.asList(opt1, opt2, opt3, opt4, opt5));

        assertEquals(5, optionsList.size());
        assertSame(opt1, optionsList.get(0));
        assertSame(opt2, optionsList.get(1));
        assertSame(opt3, optionsList.get(2));
        assertSame(opt4, optionsList.get(3));
        assertSame(opt5, optionsList.get(4));
        assertEquals(expectedList, optionsList);
    }

    @Test
    public void testGetRequiredOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option opt1 = new Option("o1", "opt1", true, "Required Option 1");
        Option opt2 = new Option("o2", "opt2", true, "Required Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Optional Option 3");
        Option opt4 = new Option("o4", "opt4", false, "Optional Option 4");
        optionGroup.addOption(opt1);
        optionGroup.addOption(opt2);
        Options options = new Options();

        options.addOptionGroup(optionGroup);

        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(2, requiredOptions.size());
        assertTrue(requiredOptions.get(0) instanceof Option);
        assertSame(opt1, requiredOptions.get(0));
        assertTrue(requiredOptions.get(1) instanceof Option);
        assertSame(opt2, requiredOptions.get(1));
    }

    @Test
    public void testGetOption() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        Option option = options.getOption("o1");

        assertSame(opt1, option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionWithNullShortOptionName() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Options options = new Options();

        options.addOption(opt1);

        options.getOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionWithNullLongOptionName() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Options options = new Options();

        options.addOption(opt1);

        options.getOption(null);
    }

    @Test
    public void testGetMatchingOptions() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Option opt4 = new Option("o4", "opt4", false, "Option 4");
        Option opt5 = new Option("o5", "opt5", false, "Option 5");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);
        options.addOption(opt4);
        options.addOption(opt5);

        List<String> matchingOptions = options.getMatchingOptions("opt");

        List<String> expectedList = new ArrayList<>();
        expectedList.add("opt1");
        expectedList.add("opt2");
        expectedList.add("opt3");
        expectedList.add("opt4");
        expectedList.add("opt5");

        assertEquals(5, matchingOptions.size());
        assertEquals(expectedList, matchingOptions);
    }

    @Test
    public void testHasOption() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("o3"));
        assertFalse(options.hasOption("nonexistent"));
    }

    @Test
    public void testHasShortOption() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        assertTrue(options.hasShortOption("o1"));
        assertTrue(options.hasShortOption("o2"));
        assertTrue(options.hasShortOption("o3"));
        assertFalse(options.hasShortOption("nonexistent"));
    }

    @Test
    public void testHasLongOption() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        assertTrue(options.hasLongOption("opt1"));
        assertTrue(options.hasLongOption("opt2"));
        assertTrue(options.hasLongOption("opt3"));
        assertFalse(options.hasLongOption("nonexistent"));
    }

    @Test
    public void testToString() {
        Option opt1 = new Option("o1", "opt1", false, "Option 1");
        Option opt2 = new Option("o2", "opt2", false, "Option 2");
        Option opt3 = new Option("o3", "opt3", false, "Option 3");
        Options options = new Options();

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        String expected = "[ Options: [ short {o=Option[name=o1, longOpt=, optional=false, values=[], description=Option 1]},"
                + " {o=Option[name=o2, longOpt=, optional=false, values=[], description=Option 2]},"
                + " {o=Option[name=o3, longOpt=, optional=false, values=[], description=Option 3]}] "
                + "[ long {opt1=Option[name=opt1, longOpt=opt1, optional=false, values=[], description=Option 1]},"
                + " {opt2=Option[name=opt2, longOpt=opt2, optional=false, values=[], description=Option 2]},"
                + " {opt3=Option[name=opt3, longOpt=opt3, optional=false, values=[], description=Option 3]}]]";

        String result = options.toString();

        assertEquals(expected, result);
    }
}