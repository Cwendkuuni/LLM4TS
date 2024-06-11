import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * OptionGroup test cases.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionA", "Option A");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertNotNull(addedOption);
        Assert.assertEquals(option, addedOption);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.addOption(option);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionA", "Option A");
        Option option2 = new Option("optionB", "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set expectedNames = new HashSet(Arrays.asList("optionA", "optionB"));
        Assert.assertEquals(expectedNames, optionGroup.getNames());
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionA", "Option A");
        Option option2 = new Option("optionB", "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set expectedOptions = new HashSet(Arrays.asList(option1, option2));
        Assert.assertEquals(expectedOptions, optionGroup.getOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelectedValidOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionA", "Option A");

        optionGroup.setSelected(option);
        Assert.assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionA", "Option A");
        Option option2 = new Option("optionB", "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionA", "Option A");
        Option option2 = new Option("optionB", "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[--optionA Option A, --optionB Option B ]";
        Assert.assertEquals(expectedString, optionGroup.toString());
    }
}