import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest
{
    @Test
    public void testAddOption() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", true, "o1");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertSame(option, addedOption);
    }

    @Test
    public void testGetNames() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", true, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("option1"));
        Assert.assertTrue(names.contains("option2"));
    }

    @Test
    public void testGetOptions() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", true, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>((Collection<? extends Option>) optionGroup.getOptions());
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_SameOption() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", true, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_DifferentOption() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", true, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", true, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() throws Exception
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true, "o1");
        Option option2 = new Option("option2", "Option 2", true, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        String expected = "[-o1 Option 1\n, -o2 Option 2\n]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
}