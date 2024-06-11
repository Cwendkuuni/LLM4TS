package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest
{
    @Test
    public void testOptionGroupWithRequiredGroup()
    {
        OptionGroup optionGroup = new OptionGroup();
        OptionGroup requiredOptionGroup = new OptionGroup();
        requiredOptionGroup.setRequired(true);

        Option option1 = new Option("o1", "option1", true, "Description for option1");
        Option option2 = new Option("o2", "option2", true, "Description for option2");

        requiredOptionGroup.addOption(option1);
        requiredOptionGroup.addOption(option2);
        optionGroup.addOption(requiredOptionGroup);

        try
        {
            optionGroup.setSelected(option1);
            assertEquals("option1", optionGroup.getSelected());
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }

        optionGroup.setSelected(option2);
        assertEquals("option2", optionGroup.getSelected());

        assertFalse(optionGroup.isRequired());
        assertTrue(requiredOptionGroup.isRequired());

        try
        {
            optionGroup.setSelected(new Option("o3", "option3"));
            assertFalse(true); // should not be able to set selected option that is not in the group
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithOptionalGroup()
    {
        OptionGroup optionGroup = new OptionGroup();
        OptionGroup optionalOptionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", true, "Description for option1");
        Option option2 = new Option("o2", "option2", true, "Description for option2");

        optionalOptionGroup.addOption(option1);
        optionalOptionGroup.addOption(option2);
        optionGroup.addOption(optionalOptionGroup);

        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("option2", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
}