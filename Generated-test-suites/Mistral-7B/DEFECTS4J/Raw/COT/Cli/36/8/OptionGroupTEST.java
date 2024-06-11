import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "opt description", false, "o");

        OptionGroup result = optionGroup.addOption(option);

        Assert.assertEquals(option, result.getOptions().iterator().next());
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt description 1", false, "o1");
        Option option2 = new Option("opt2", "opt description 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = Arrays.asList(option1.getKey(), option2.getKey());

        Assert.assertEquals(names, optionGroup.getNames());
    }

    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt description 1", false, "o1");
        Option option2 = new Option("opt2", "opt description 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = Arrays.asList(option1, option2);

        Assert.assertEquals(options, optionGroup.getOptions());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt description 1", false, "o1");
        Option option2 = new Option("opt2", "opt description 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt description 1", false, "o1");
        Option option2 = new Option("opt2", "opt description 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt description 1", false, "o1");
        Option option2 = new Option("opt2", "opt description 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--opt1 opt description 1, --opt2 opt description 2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }
}