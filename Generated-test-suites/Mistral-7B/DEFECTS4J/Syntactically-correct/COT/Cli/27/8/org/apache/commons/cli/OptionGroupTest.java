import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OptionGroupTest class file for testing OptionGroup class methods.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "This is option 1.");

        optionGroup.addOption(option);

        Collection options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.iterator().next());
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "This is option 1.");
        Option option2 = new Option("option2", "This is option 2.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();
        List expectedNames = new ArrayList();
        expectedNames.add(option1.getKey());
        expectedNames.add(option2.getKey());

        Assert.assertEquals(expectedNames, names);
    }

    @Test
    public void testSetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", "This is option.");

        try
        {
            optionGroup.setSelected(option);
        }
        catch (AlreadySelectedException e)
        {
            Assert.fail("Expected no exception when setting selected option to null.");
        }

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedSameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "This is option 1.");
        Option option2 = new Option("option1", "This is option 1.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "This is option 1.");
        Option option2 = new Option("option2", "This is option 2.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "This is option 1.");
        Option option2 = new Option("option2", "This is option 2.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[-option1 This is option 1., --option2 This is option 2.]";
        Assert.assertEquals(expectedToString, optionGroup.toString());
    }
}