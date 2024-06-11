package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest
{
    @Test
    public void testOptionGroupWithRequiredOptions()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("opt1", "Option 1", true, "Short description for option 1");
        Option option2 = new Option("opt2", "Option 2", false, "Short description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setRequired(true);

        assertThrows(IllegalArgumentException.class, () -> optionGroup.setSelected(null));
        optionGroup.setSelected(option1);
        assertNotNull(optionGroup.getSelected());
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        // test adding a third option which is not required
        Option option3 = new Option("opt3", "Option 3", false, "Short description for option 3");
        optionGroup.addOption(option3);
        assertFalse(optionGroup.isRequired());
        optionGroup.setSelected(option3);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithMutuallyExclusiveOptions()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("opt1", "Option 1", false, "Short description for option 1");
        Option option2 = new Option("opt2", "Option 2", false, "Short description for option 2");
        Option option3 = new Option("opt3", "Option 3", false, "Short description for option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setExclusive(true);

        try
        {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }

        optionGroup.setSelected(option1);
        assertNotNull(optionGroup.getSelected());
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option3);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithMixedOptions()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("opt1", "Option 1", true, "Short description for option 1");
        Option option2 = new Option("opt2", "Option 2", false, "Short description for option 2");
        Option option3 = new Option("opt3", "Option 3", false, "Short description for option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // test setting required group with mixed options
        optionGroup.setRequired(true);
        try
        {
            optionGroup.setSelected(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        optionGroup.setSelected(option1);
        assertNotNull(optionGroup.getSelected());
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        try
        {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }

        optionGroup.setSelected(option3);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupToString()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("opt1", "Option 1", false, "Short description for option 1");
        Option option2 = new Option("opt2", "Option 2", false, "Short description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[--opt1 Option 1, --opt2 Option 2]", optionGroup.toString());
    }
}