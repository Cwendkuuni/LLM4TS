import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    /**
     * Test method for {@link OptionGroup#addOption(Option)} method.
     */
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("o1", option1);
        expectedOptionMap.put("o2", option2);

        Assert.assertEquals(expectedOptionMap, optionGroup.optionMap);
    }

    /**
     * Test method for {@link OptionGroup#getNames()} method.
     */
    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");

        List<String> actualNames = new ArrayList<>(optionGroup.getNames());

        Assert.assertEquals(expectedNames, actualNames);
    }

    /**
     * Test method for {@link OptionGroup#getOptions()} method.
     */
    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        List<Option> actualOptions = new ArrayList<>(optionGroup.getOptions());

        Assert.assertArrayEquals(expectedOptions.toArray(), actualOptions.toArray());
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} method with null option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} method with already selected option.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} method with correct option.
     */
    @Test
    public void testSetSelectedCorrectOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals("o1", optionGroup.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)} method.
     */
    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        Assert.assertFalse(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()} method.
     */
    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 option1, --o2 option2]";

        String actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);
    }
}