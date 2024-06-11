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
    public void testOptionGroup_addOptions()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", true, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");
        Option option3 = new Option("o3", "option3", false, "Description for option3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection optionNames = optionGroup.getNames();
        Collection optionValues = optionGroup.getOptions();

        assertEquals(3, optionNames.size());
        assertEquals(option1, optionValues.iterator().next());
        assertEquals(option2, optionValues.iterator().next());
        assertEquals(option3, optionValues.iterator().next());
    }

    @Test
    public void testOptionGroup_setSelected() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Description for option1");
        Option option2 = new Option("o2", "option2", true, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try
        {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        }
        catch (Exception e)
        {
            assertTrue(e instanceof AlreadySelectedException);
        }

        optionGroup.setSelected(option1);

        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testOptionGroup_isRequired()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }
}