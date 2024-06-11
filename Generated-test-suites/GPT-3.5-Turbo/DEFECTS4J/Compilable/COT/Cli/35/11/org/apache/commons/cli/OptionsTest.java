package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroup(option1) == group);
        assertTrue(options.getOptionGroup(option2) == group);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));
    }

    @Test
    public void testAddOptionWithShortName() {
        String opt = "a";
        String description = "Option A";

        options.addOption(opt, description);

        assertTrue(options.hasOption(opt));

        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndArg() {
        String opt = "a";
        boolean hasArg = true;
        String description = "Option A";

        options.addOption(opt, hasArg, description);

        assertTrue(options.hasOption(opt));

        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        String opt = "a";
        String longOpt = "optionA";
        boolean hasArg = true;
        String description = "Option A";

        options.addOption(opt, longOpt, hasArg, description);

        assertTrue(options.hasOption(opt));
        assertTrue(options.hasOption(longOpt));

        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertEquals(longOpt, option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(description, option.getDescription());

        Option optionLong = options.getOption(longOpt);
        assertNotNull(optionLong);
        assertEquals(opt, optionLong.getOpt());
        assertEquals(longOpt, optionLong.getLongOpt());
        assertTrue(optionLong.hasArg());
        assertEquals(description, optionLong.getDescription());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "optionA", true, "Option A");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));

        Option addedOption = options.getOption("a");
        assertNotNull(addedOption);
        assertEquals("a", addedOption.getOpt());
        assertEquals("optionA", addedOption.getLongOpt());
        assertTrue(addedOption.hasArg());
        assertEquals("Option A", addedOption.getDescription());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        Collection<Option> optionCollection = options.getOptions();
        assertTrue(optionCollection.contains(option1));
        assertTrue(optionCollection.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        options.addOptionGroup(new OptionGroup().addOption(option1));

        List requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.contains(option1));
        assertFalse(requiredOptions.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        options.addOption(option1);

        Option option = options.getOption("a");
        assertNotNull(option);
        assertSame(option1, option);

        option = options.getOption("option1");
        assertNotNull(option);
        assertSame(option1, option);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("apple", false, "Apple option");
        Option option2 = new Option("banana", false, "Banana option");
        Option option3 = new Option("orange", false, "Orange option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("or");
        assertTrue(matchingOptions.contains("orange"));
        assertEquals(1, matchingOptions.size());

        matchingOptions = options.getMatchingOptions("a");
        assertTrue(matchingOptions.contains("apple"));
        assertTrue(matchingOptions.contains("banana"));
        assertEquals(2, matchingOptions.size());

        matchingOptions = options.getMatchingOptions("pe");
        assertTrue(matchingOptions.isEmpty());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("optionA"));
        assertTrue(options.hasLongOption("optionB"));
        assertFalse(options.hasLongOption("optionC"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        OptionGroup retrievedGroup1 = options.getOptionGroup(option1);
        assertNotNull(retrievedGroup1);
        assertSame(group, retrievedGroup1);

        OptionGroup retrievedGroup2 = options.getOptionGroup(option2);
        assertNotNull(retrievedGroup2);
        assertSame(group, retrievedGroup2);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option 1, b=Option 2} ] [ long {option1=null, option2=null} ]";
        assertEquals(expected, options.toString());
    }
}