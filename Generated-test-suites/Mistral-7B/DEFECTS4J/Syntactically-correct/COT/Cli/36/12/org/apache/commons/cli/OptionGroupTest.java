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
 * Test class for OptionGroup
 */
public class OptionGroupTest
{
    /**
     * Test method for {@link OptionGroup#addOption(Option)}
     */
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "optional description", true);
        optionGroup.addOption(option);

        Map<String, Option> expectedMap = new HashMap<>();
        expectedMap.put("option1", option);

        Assert.assertEquals(expectedMap, optionGroup.optionMap);
    }

    /**
     * Test method for {@link OptionGroup#getNames()}
     */
    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optional description", true);
        Option option2 = new Option("option2", "optional description", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> expectedList = new ArrayList<>();
        expectedList.add("option1");
        expectedList.add("option2");

        Assert.assertEquals(expectedList, optionGroup.getNames());
    }

    /**
     * Test method for {@link OptionGroup#getOptions()}
     */
    @Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optional description", true);
        Option option2 = new Option("option2", "optional description", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> expectedList = new ArrayList<>();
        expectedList.add(option1);
        expectedList.add(option2);

        Assert.assertEquals(expectedList, optionGroup.getOptions());
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedException()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optional description", true);
        Option option2 = new Option("option2", "optional description", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     */
    @Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optional description", true);
        Option option2 = new Option("option2", "optional description", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals("option1", optionGroup.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#isRequired()}
     */
    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()}
     */
    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optional description", true);
        Option option2 = new Option("option2", "optional description", true);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--option1 optional description, --option2 optional description]", optionGroup.toString());
    }
}