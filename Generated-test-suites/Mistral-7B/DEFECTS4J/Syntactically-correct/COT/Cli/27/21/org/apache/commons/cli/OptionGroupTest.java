import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Description for option1");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();
        Assert.assertEquals(names.size(), 2);
        Assert.assertTrue(names.contains("option1"));
        Assert.assertTrue(names.contains("option2"));
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();
        Assert.assertEquals(options.size(), 2);
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try
        {
            optionGroup.setSelected(option1);
        }
        catch (AlreadySelectedException e)
        {
            // expected since no option is selected initially
        }
        optionGroup.setSelected(option2);
        Assert.assertEquals(option2.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--option1 Description for option1, --option2 Description for option2]", optionGroup.toString());
    }
}