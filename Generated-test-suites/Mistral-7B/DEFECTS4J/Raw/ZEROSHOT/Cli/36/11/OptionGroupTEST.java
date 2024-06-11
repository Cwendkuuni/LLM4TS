package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    /**
     * Test method for the constructor.
     */
    @Test
    public void testOptionGroupConstruction()
    {
        OptionGroup optionGroup = new OptionGroup();
        assertNull(optionGroup.getSelected());
        assertFalse(optionGroup.isRequired());

        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        OptionGroup optionGroupInstance = new OptionGroup();
        optionGroupInstance.addOption(option1);
        optionGroupInstance.addOption(option2);

        assertEquals("Size of the option map should be 2", 2, optionGroupInstance.getOptions().size());
        assertEquals("Size of the option names should be 2", 2, optionGroupInstance.getNames().size());
        assertSame("The options should be the same instances", option1, optionGroupInstance.getOption("o1"));
        assertSame("The options should be the same instances", option2, optionGroupInstance.getOption("o2"));
    }

    /**
     * Test method for setSelected.
     */
    @Test
    public void testSetSelected()
    {
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try
        {
            optionGroup.setSelected(null);
            fail("Expected AlreadySelectedException when setting selected to null");
        } catch (AlreadySelectedException e)
        {
            // Expected
        }

        try
        {
            optionGroup.setSelected(option1);
            assertEquals("Selected option should be 'o1'", option1.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e)
        {
            // Expected
        }

        try
        {
            optionGroup.setSelected(option2);
            assertEquals("Selected option should be 'o2'", option2.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e)
        {
            // Expected
        }

        try
        {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException when setting selected to an already selected option");
        } catch (AlreadySelectedException e)
        {
            // Expected
        }
    }

    /**
     * Test method for isRequired.
     */
    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse("OptionGroup should not be required by default", optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue("OptionGroup should be required", optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse("OptionGroup should not be required anymore", optionGroup.isRequired());
    }
}