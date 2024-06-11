package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testOptionGroup()
    {
        // Create option group with options
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", false, "o1-desc");
        Option option2 = new Option("o2", "option 2", false, "o2-desc");
        Option option3 = new Option("o3", "option 3", false, "o3-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test getters
        assertEquals("The number of options in the group should be equal to three.", 3, optionGroup.getOptions().size());
        assertEquals("The selected option should be null.", null, optionGroup.getSelected());
        assertFalse("The group should not be required.", optionGroup.isRequired());

        // Test setSelected method with valid option
        optionGroup.setSelected(option1);
        assertEquals("The selected option should be 'o1'.", "o1", optionGroup.getSelected());

        // Test setSelected method with invalid option
        assertThrows("Setting a selected option that is already selected should throw an AlreadySelectedException.", AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        // Test setSelected method with null option
        optionGroup.setSelected(null);
        assertNull("The selected option should be null after resetting it.", optionGroup.getSelected());

        // Test toString method
        assertEquals("The string representation of the group should match the expected format.", "[--o1-desc, --o2-desc, --o3-desc]", optionGroup.toString());
    }

    @Test
    public void testOptionGroupWithRequired()
    {
        // Create option group with options and set it required
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1", false, "o1-desc");
        Option option2 = new Option("o2", "option 2", false, "o2-desc");
        Option option3 = new Option("o3", "option 3", false, "o3-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setRequired(true);

        // Test setSelected method with valid option
        optionGroup.setSelected(option1);
        assertEquals("The selected option should be 'o1'.", "o1", optionGroup.getSelected());

        // Test setSelected method with null option
        assertThrows("Setting a null selected option on a required group should throw an IllegalArgumentException.", IllegalArgumentException.class, () -> optionGroup.setSelected(null));

        // Test setSelected method with an invalid option
        assertThrows("Selecting an invalid option on a required group should throw an IllegalStateException.", IllegalStateException.class, () -> optionGroup.setSelected(new Option("invalidOption", "Invalid Option", false, null)));
    }

    @Test
    public void testOptionGroupWithInvalidOptions()
    {
        // Create option group with invalid options
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option 1", false, "o1-desc");
        Option option2 = new Option("o2", "option 2", false, "o2-desc");
        Option invalidOption = new Option("iOption", "Invalid Option", false, null);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(invalidOption);

        // Test setSelected method with valid option
        optionGroup.setSelected(option1);
        assertEquals("The selected option should be 'o1'.", "o1", optionGroup.getSelected());

        // Test setSelected method with invalid option
        assertThrows("Selecting an invalid option should throw an IllegalStateException.", IllegalStateException.class, () -> optionGroup.setSelected(invalidOption));
    }

    @Test
    public void testOptionGroupWithEmptyCollection()
    {
        // Create option group with empty collection
        OptionGroup optionGroup = new OptionGroup();

        // Test getNames method
        List<String> expectedNames = new ArrayList<>();
        assertEquals("The names of the options in the group should be an empty collection.", Collections.emptyList(), optionGroup.getNames());

        // Test getOptions method
        List<Option> expectedOptions = new ArrayList<>();
        assertEquals("The options in the group should be an empty collection.", Collections.emptyList(), optionGroup.getOptions());
    }
}