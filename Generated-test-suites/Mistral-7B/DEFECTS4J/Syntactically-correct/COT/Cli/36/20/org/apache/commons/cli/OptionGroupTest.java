import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Before;
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
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        optionGroup = new OptionGroup();
    }

    /**
     * Test addOption method.
     */
    @Test
    public void testAddOption()
    {
        Option option = new Option("option1", "option1-description", true, "o");
        optionGroup.addOption(option);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.iterator().next());
    }

    /**
     * Test getNames method.
     */
    @Test
    public void testGetNames()
    {
        Option option1 = new Option("option1", "option1-description", true, "o");
        Option option2 = new Option("option2", "option2-description", false, "d");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("option1");
        expectedNames.add("option2");

        Assert.assertEquals(expectedNames, names);
    }

    /**
     * Test getOptions method.
     */
    @Test
    public void testGetOptions()
    {
        Option option1 = new Option("option1", "option1-description", true, "o");
        Option option2 = new Option("option2", "option2-description", false, "d");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, options);
    }

    /**
     * Test setSelected method with null option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        optionGroup.setSelected(null);
    }

    /**
     * Test setSelected method with already selected option.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException
    {
        Option option1 = new Option("option1", "option1-description", true, "o");
        Option option2 = new Option("option2", "option2-description", false, "d");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    /**
     * Test setSelected method with new option.
     */
    @Test
    public void testSetSelectedNewOption() throws AlreadySelectedException
    {
        Option option1 = new Option("option1", "option1-description", true, "o");
        Option option2 = new Option("option2", "option2-description", false, "d");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option2);

        Assert.assertEquals("d", optionGroup.getSelected());
    }

    /**
     * Test isRequired method with false required flag.
     */
    @Test
    public void testIsRequiredFalse()
    {
        optionGroup.setRequired(false);

        Assert.assertFalse(optionGroup.isRequired());
    }

    /**
     * Test isRequired method with true required flag.
     */
    @Test
    public void testIsRequiredTrue()
    {
        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString()
    {
        Option option1 = new Option("option1", "option1-description", true, "o");
        Option option2 = new Option("option2", "option2-description", false, "d");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[--option1 option1-description, --option2 option2-description]";
        Assert.assertEquals(expectedToString, optionGroup.toString());
    }
}