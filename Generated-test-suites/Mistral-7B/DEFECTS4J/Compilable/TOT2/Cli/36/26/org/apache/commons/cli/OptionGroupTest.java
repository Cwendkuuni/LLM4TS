package org.apache.commons.cli;
import static org.apache.commons.cli.Option.*;
import static org.junit.Assert.*;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        OptionGroup addedOptionGroup = optionGroup.addOption(option);
        assertTrue(addedOptionGroup == optionGroup);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        OptionGroup addedOptionGroup = optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option1", "Option 1", false, "o1");
        OptionGroup addedOptionGroup = optionGroup.addOption(option1);
        addedOptionGroup = optionGroup.addOption(option2);
    }

    // Test other methods in OptionGroup here

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[[-o1 Option 1], [-o2 Option 2]]";
        assertEquals(expected, optionGroup.toString());
    }
}