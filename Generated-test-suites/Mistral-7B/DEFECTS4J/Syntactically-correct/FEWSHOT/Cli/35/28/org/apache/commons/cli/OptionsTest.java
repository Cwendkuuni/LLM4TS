package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionGroupWithNullOptionGroup() {
        Options options = new Options();
        assertNull(options.addOptionGroup(null));
    }

    @Test
    public void testAddOptionGroupWithEmptyOptionGroup() {
        Options options = new Options();
        OptionGroup emptyOptionGroup = new OptionGroup();
        Options result = options.addOptionGroup(emptyOptionGroup);
        assertSame(options, result);
    }

    @Test
    public void testAddOptionGroupWithOptionGroupContainingOptions() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt2", "description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Options result = options.addOptionGroup(optionGroup);
        assertEquals(Collections.singletonList(optionGroup), result.getOptionGroups());
        assertEquals(2, result.getOptions().size());
        assertEquals(option1, result.getOption("opt1"));
        assertEquals(option2, result.getOption("opt2"));
    }

    @Test
    public void testAddOptionWithNullDescription() {
        Options options = new Options();
        String opt = "opt";
        assertIllegalArgumentException("description cannot be null", () -> options.addOption(opt));
    }

    @Test
    public void testAddOptionWithEmptyDescription() {
        Options options = new Options();
        String opt = "opt";
        assertIllegalArgumentException("description cannot be empty", () -> options.addOption(opt, ""));
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOpt() {
        Options options = new Options();
        String opt = "opt";
        String description = "description";
        Options result = options.addOption(opt, description);
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, false, null)), result.getShortOpts());
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOptRequiringArgument() {
        Options options = new Options();
        String opt = "opt";
        String description = "description";
        Options result = options.addOption(opt, description, true, null);
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, true, null)), result.getShortOpts());
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOptAndLongOpt() {
        Options options = new Options();
        String shortOpt = "s";
        String longOpt = "long";
        String description = "description";
        Options result = options.addOption(shortOpt, longOpt, false, description);
        assertEquals(Collections.singletonMap(shortOpt, new Option(shortOpt, longOpt, false, description)), result.getShortOpts());
        assertEquals(Collections.singletonMap(longOpt, new Option(longOpt, description, false, null)), result.getLongOpts());
    }

    @Test
    public void testAddOptionWithSingleCharacterShortOptRequiringArgumentAndLongOpt() {
        Options options = new Options();
        String shortOpt = "s";
        String longOpt = "long";
        String description = "description";
        Options result = options.addOption(shortOpt, longOpt, true, description);
        assertEquals(Collections.singletonMap(shortOpt, new Option(shortOpt, longOpt, true, description)), result.getShortOpts());
        assertEquals(Collections.singletonMap(longOpt, new Option(longOpt, description, true, null)), result.getLongOpts());
    }

    @Test
    public void testAddOptionWithMultipleCharacterShortOpt() {
        Options options = new Options();
        String opt = "--opt";
        String description = "description";
        Options result = options.addOption(opt, description);
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, false, null)), result.getShortOpts());
        assertEquals(Collections.emptyMap(), result.getLongOpts());
    }

    @Test
    public void testAddOptionWithMultipleCharacterShortOptRequiringArgument() {
        Options options = new Options();
        String opt = "--opt";
        String description = "description";
        Options result = options.addOption(opt, description, true, null);
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, true, null)), result.getShortOpts());
        assertEquals(Collections.emptyMap(), result.getLongOpts());
    }

    @Test
    public void testAddOptionWithShortOptWithValue() {
        Options options = new Options();
        String opt = "opt";
        String value = "value";
        String description = "description";
        Options result = options.addOption(opt, description, value, null);
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, false, value)), result.getShortOpts());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        String opt = "long-opt";
        String description = "description";
        Options result = options.addOption(opt, description);
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, false, null)), result.getLongOpts());
        assertEquals(Collections.emptyMap(), result.getShortOpts());
    }

    @Test
    public void testAddOptionWithLongOptRequiringArgument() {
        Options options = new Options();
        String opt = "long-opt";
        String description = "description";
        Options result = options.addOption(opt, description, true, null);
        assertEquals(Collections.emptyMap(), result.getShortOpts());
        assertEquals(Collections.singletonMap(opt, new Option(opt, description, true, null)), result.getLongOpts());
    }

    @Test
    public void testAddOptionWithLongOptAndShortOptWithSameName() {
        Options options = new Options();
        String shortOpt = "s";
        String longOpt = "long";
        String description = "description";
        Options result = options.addOption(shortOpt, description).addOption(longOpt, description);
        assertEquals(Collections.singletonMap(shortOpt, new Option(shortOpt, description, false, null)), result.getShortOpts());
        assertEquals(Collections.singletonMap(longOpt, new Option(longOpt, description, false, null)), result.getLongOpts());
    }

    @Test
    public void testAddOptionWithRequiredOption() {
        Options options = new Options();
        String opt = "opt";
        Options result = options.addOption(opt);
        List<Object> requiredOpts = options.getRequiredOptions();
        assertTrue(requiredOpts.contains(opt));
    }

    @Test
    public void testAddOptionGroupWithRequiredOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Option opt = new Option("opt", "description");
        optionGroup.addOption(opt);
        Options result = options.addOptionGroup(optionGroup);
        List<Object> requiredOpts = result.getRequiredOptions();
        assertTrue(requiredOpts.contains(opt));
    }

    @Test
    public void testAddOptionWithOptionAlreadyPresent() {
        Options options = new Options();
        String opt = "opt";
        Options result = options.addOption(opt);
        assertIllegalStateException("Option [" + opt + "] is already present", () -> options.addOption(opt));
    }

    @Test
    public void testGetOptionWithShortOpt() {
        Options options = new Options();
        String opt = "opt";
        Option option = new Option(opt, null, false, null);
        options.addOption(option);
        Option result = options.getOption(opt);
        assertEquals(option, result);
    }

    @Test
    public void testGetOptionWithLongOpt() {
        Options options = new Options();
        String longOpt = "long-opt";
        Option option = new Option(null, longOpt, false, null);
        options.addOption(option);
        Option result = options.getOption(longOpt);
        assertEquals(option, result);
    }

    @Test
    public void testGetOptionWithShortOptNotPresent() {
        Options options = new Options();
        String opt = "opt";
        Option result = options.getOption(opt);
        assertNull(result);
    }

    @Test
    public void testGetOptionWithLongOptNotPresent() {
        Options options = new Options();
        String longOpt = "long-opt";
        Option result = options.getOption(longOpt);
        assertNull(result);
    }

    @Test
    public void testGetMatchingOptionsWithPartialName() {
        Options options = new Options();
        String opt1 = "opt1";
        String opt2 = "opt2";
        options.addOption(new Option(opt1, null, false, "Description for opt1"));
        options.addOption(new Option(opt2, "long-opt2", false, "Description for opt2"));
        List<String> result = options.getMatchingOptions("opt");
        assertEquals(Collections.singletonList(opt1), result);

        result = options.getMatchingOptions("long-opt");
        assertEquals(Collections.singletonList(opt2), result);

        result = options.getMatchingOptions("opt2-");
        assertEquals(Collections.singletonList(opt2), result);

        result = options.getMatchingOptions("opt2--long");
        assertEquals(Collections.singletonList(opt2), result);

        result = options.getMatchingOptions("");
        assertEquals(Collections.emptyList(), result);

        result = options.getMatchingOptions("opt3");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testHasOptionWithShortOpt() {
        Options options = new Options();
        String opt = "opt";
        Option option = new Option(opt, null, false, null);
        options.addOption(option);
        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testHasOptionWithLongOpt() {
        Options options = new Options();
        String longOpt = "long-opt";
        Option option = new Option(null, longOpt, false, null);
        options.addOption(option);
        assertTrue(options.hasLongOption(longOpt));
    }

    @Test
    public void testHasOptionWithShortOptNotPresent() {
        Options options = new Options();
        String opt = "opt";
        assertFalse(options.hasOption(opt));
    }

    @Test
    public void testHasOptionWithLongOptNotPresent() {
        Options options = new Options();
        String longOpt = "long-opt";
        assertFalse(options.hasLongOption(longOpt));
    }

    @Test
    public void testHasOptionGroupWithOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        options.addOptionGroup(optionGroup);
        assertTrue(options.getOptionGroup(optionGroup) != null);
    }

    @Test
    public void testHasOptionGroupWithNullOptionGroup() {
        Options options = new Options();
        assertFalse(options.getOptionGroup(null) != null);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        String shortOpt = "s";
        String longOpt = "long-opt";
        String optDescription = "description";
        Option option = new Option(shortOpt, longOpt, false, optDescription);
        options.addOption(option);
        String expected = "[ Options: [ short {s=s} ] [ long {}=long-opt ] ]";
        assertEquals(expected, options.toString());
    }
}