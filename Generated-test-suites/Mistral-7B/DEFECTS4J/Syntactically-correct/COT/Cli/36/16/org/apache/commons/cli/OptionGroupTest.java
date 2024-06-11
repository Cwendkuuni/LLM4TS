import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test class for OptionGroup class.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "opt", false, "Test option");

        optionGroup.addOption(option);

        Option addedOption = optionGroup.getOptions().iterator().next();

        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Test option 1");
        Option option2 = new Option("o2", "opt2", false, "Test option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(Arrays.asList("o1", "o2"), names);
    }

    @Test
    public void testSetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Test option 1");
        Option option2 = new Option("o2", "opt2", false, "Test option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Test option 1");
        Option option2 = new Option("o2", "opt2", false, "Test option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testIsRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);

        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToStringEmpty()
    {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertEquals("[]", optionGroup.toString());
    }

    @Test
    public void testToStringOneOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "opt", false, "Test option");

        optionGroup.addOption(option);

        Assert.assertEquals("[--opt]", optionGroup.toString());
    }

    @Test
    public void testToStringMultipleOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Test option 1");
        Option option2 = new Option("o2", "opt2", false, "Test option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--opt1, --opt2]", optionGroup.toString());
    }
}