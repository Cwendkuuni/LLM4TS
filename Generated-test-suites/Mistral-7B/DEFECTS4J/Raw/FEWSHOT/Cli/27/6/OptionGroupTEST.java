package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupEmpty() {
        OptionGroup optionGroup = new OptionGroup();

        assertEquals(Collections.emptyList(), optionGroup.getNames());
        assertEquals(Collections.emptyList(), optionGroup.getOptions());
        assertNull(optionGroup.getSelected());
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testOptionGroupSingleOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "force", true, "Force action.");
        optionGroup.addOption(option);

        assertEquals(Collections.singletonList("f"), optionGroup.getNames());
        assertEquals(Collections.singletonList(option), optionGroup.getOptions());
        assertNull(optionGroup.getSelected());
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testOptionGroupMultipleOptions() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("f", "force", true, "Force action.");
        Option option2 = new Option("s", "size", true, "Size of the output.");
        Option option3 = new Option("o", "output", true, "Output file name.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> expectedNames = Arrays.asList("f", "s", "o");
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(option3);

        assertEquals(expectedNames, optionGroup.getNames());
        assertEquals(expectedOptions, optionGroup.getOptions());
        assertNull(optionGroup.getSelected());
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testOptionGroupSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("f", "force", true, "Force action.");
        Option option2 = new Option("s", "size", true, "Size of the output.");
        Option selectedOption = new Option("o", "output", true, "Output file name.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(selectedOption);

        optionGroup.setSelected(selectedOption);

        List<String> expectedNames = Arrays.asList("f", "s", "o");
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(selectedOption);

        assertEquals(expectedNames, optionGroup.getNames());
        assertEquals(expectedOptions, optionGroup.getOptions());
        assertEquals(selectedOption.getOpt(), optionGroup.getSelected());
        assertFalse(optionGroup.isRequired());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupSelectedOptionAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("f", "force", true, "Force action.");
        Option selectedOption = new Option("f", "force", true, "Force action.");

        optionGroup.addOption(option1);
        optionGroup.addOption(selectedOption);
        optionGroup.setSelected(selectedOption);
    }

    @Test
    public void testOptionGroupRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("f", "force", true, "Force action.");
        Option option2 = new Option("s", "size", true, "Size of the output.");
        Option option3 = new Option("o", "output", true, "Output file name.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setRequired(true);

        List<String> expectedNames = Arrays.asList("f", "s", "o");
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(option3);

        assertEquals(expectedNames, optionGroup.getNames());
        assertEquals(expectedOptions, optionGroup.getOptions());
        assertNull(optionGroup.getSelected());
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("f", "force", true, "Force action.");
        Option option2 = new Option("s", "size", true, "Size of the output.");
        Option option3 = new Option("o", "output", true, "Output file name.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expectedString = "[--force Force action., --size Size of the output., --output Output file name.]";

        assertEquals(expectedString, optionGroup.toString());
    }
}