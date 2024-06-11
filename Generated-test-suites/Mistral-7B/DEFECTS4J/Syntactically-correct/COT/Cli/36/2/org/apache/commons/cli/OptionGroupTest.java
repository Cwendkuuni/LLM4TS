import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test file for OptionGroup class.
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Description for option1");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(Arrays.asList("option1", "option2"), names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedAndGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequiredTrue() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--option1 Description for option1, --option2 Description for option2]", optionGroup.toString());
    }
}