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
 * Test cases for OptionGroup class
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A", false, "a");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertSame(option, addedOption);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", false, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");

        List<String> actualNames = new ArrayList<>(optionGroup.getNames());

        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", false, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        List<Option> actualOptions = new ArrayList<>(optionGroup.getOptions());

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", false, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "Option", false, "o");
        optionGroup.addOption(option);
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedValidOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", false, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        Assert.assertEquals("o1", optionGroup.getSelected());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "Option", false, "o");
        optionGroup.addOption(option);
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1", false, "o1");
        Option option2 = new Option("opt2", "Option 2", false, "o2");
        Option option3 = new Option("opt3", "Option 3", false, "o3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expectedString = "[-o1 Option 1\n, -o2 Option 2\n, --opt3 Option 3\n]";
        String actualString = optionGroup.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        String expectedString = "[ ]";
        String actualString = optionGroup.toString();

        Assert.assertEquals(expectedString, actualString);
    }
}