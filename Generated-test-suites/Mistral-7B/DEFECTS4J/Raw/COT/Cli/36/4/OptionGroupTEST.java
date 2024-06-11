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
 * Test class for OptionGroup
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A");
        optionGroup.addOption(option);
        Option optionFromMap = optionGroup.optionMap.get("optA");
        Assert.assertNotNull(optionFromMap);
        Assert.assertEquals(option, optionFromMap);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        Option option3 = new Option("opt3", "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(Arrays.asList("opt1", "opt2", "opt3"), names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        Option option3 = new Option("opt3", "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(options.size(), 3);
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A");
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
        Assert.assertEquals("optA", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
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
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        Option option3 = new Option("opt3", "Option 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]";
        String result = optionGroup.toString();
        Assert.assertEquals(expected, result);
    }
}