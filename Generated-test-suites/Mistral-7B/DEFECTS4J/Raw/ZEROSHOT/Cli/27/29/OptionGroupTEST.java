import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test for OptionGroup class
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", true, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set<String> expectedOptionNames = new HashSet<>();
        expectedOptionNames.add("opt1");
        expectedOptionNames.add("opt2");

        Set<String> actualOptionNames = new HashSet<>(optionGroup.getNames());

        Assert.assertEquals(expectedOptionNames, actualOptionNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;

        optionGroup.addOption(option);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", true, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set<Option> expectedOptions = new HashSet<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Set<Option> actualOptions = new HashSet<>(optionGroup.getOptions());

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", true, "o1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", true, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", true, "o1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(false);

        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredTrue() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", true, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[--opt1 Option 1, --opt2 Option 2 ]";

        String actualToString = optionGroup.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }
}