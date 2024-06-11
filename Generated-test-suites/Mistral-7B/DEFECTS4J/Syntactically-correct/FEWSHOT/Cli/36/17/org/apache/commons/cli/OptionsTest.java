package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionShort() {
        Options options = new Options();
        options.addOption("a", "option A");
        Collection<Option> optionsList = options.getOptions();
        Option option = optionsList.iterator().next();
        assertEquals("a", option.getKey());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionShortWithArg() {
        Options options = new Options();
        options.addOption("a", "option A", true, "arg");
        Collection<Option> optionsList = options.getOptions();
        Option option = optionsList.iterator().next();
        assertEquals("a", option.getKey());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOptionLong() {
        Options options = new Options();
        options.addOption("optionA", "Option A");
        Collection<Option> optionsList = options.getOptions();
        Option option = optionsList.iterator().next();
        assertEquals("optionA", option.getLongOpt());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionLongWithArg() {
        Options options = new Options();
        options.addOption("optionA", true, "arg");
        options.addOption("optionA", "Option A");
        Collection<Option> optionsList = options.getOptions();
        Option option = optionsList.iterator().next();
        assertEquals("optionA", option.getLongOpt());
        assertEquals("Option A", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOptionShortAndLong() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("optionA", "Option A");
        Collection<Option> optionsList = options.getOptions();
        Option option1 = optionsList.iterator().next();
        assertEquals("a", option1.getKey());
        Option option2 = optionsList.iterator().next();
        assertEquals("optionA", option2.getLongOpt());
        assertEquals("Option A", option2.getDescription());
        assertEquals(option1, option2);
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("groupName", "group description");
        options.addOptionGroup(optionGroup);
        assertEquals(optionGroup, options.getOptionGroup(optionGroup));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("optA", "Option A");
        options.addOption("optB", "Option B");
        options.addOption("optC-abc", "Option C with arg ab");
        options.addOption("optC-def", "Option C with arg def");

        List<String> matchingOptions = options.getMatchingOptions("optC");
        List<String> expectedOptions = Arrays.asList("optC-abc", "optC-def");
        assertEquals(expectedOptions, matchingOptions);
    }

    @Test
    public void testGetMatchingOptionsEmpty() {
        Options options = new Options();
        List<String> matchingOptions = options.getMatchingOptions("nonexistent");
        List<String> expectedOptions = Collections.emptyList();
        assertEquals(expectedOptions, matchingOptions);
    }

    @Test
    public void testHasOptionShort() {
        Options options = new Options();
        options.addOption("a", "Option A");
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasOptionLong() {
        Options options = new Options();
        options.addOption("optionA", "Option A");
        assertTrue(options.hasLongOption("optionA"));
    }

    @Test
    public void testHasOptionShortWithArg() {
        Options options = new Options();
        options.addOption("a", "Option A", true, "arg");
        assertTrue(options.hasOption("a", "arg"));
    }

    @Test
    public void testHasOptionLongWithArg() {
        Options options = new Options();
        options.addOption("optionA", true, "arg");
        options.addOption("optionA", "Option A");
        assertTrue(options.hasOption("--arg"));
    }

    @Test
    public void testHasOptionShortMissing() {
        Options options = new Options();
        assertFalse(options.hasOption("missing"));
    }

    @Test
    public void testHasOptionLongMissing() {
        Options options = new Options();
        assertFalse(options.hasLongOption("missing"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", "Option A");
        options.addOption("optionA", "Option A");
        String expectedString = "[ Options: [ short {a=OPTION_A} ] [ long {OptionA=OPTION_A}] ]";
        assertEquals(expectedString, options.toString());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOptionGroup(new OptionGroup("requiredOptions", "Required Options"));
        options.addOptionGroup(new OptionGroup("optionalOptions", "Optional Options"));
        options.addOptionGroup(new OptionGroup("anotherGroup", "Another Group"));
        List<Object> requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals("requiredOptions", requiredOptions.get(0).toString());
    }
}