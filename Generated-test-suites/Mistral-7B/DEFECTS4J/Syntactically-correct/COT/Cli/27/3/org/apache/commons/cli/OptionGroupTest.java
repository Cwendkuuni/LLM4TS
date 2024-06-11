import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Description for option1");

        optionGroup.addOption(option);

        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("option1", option);

        Map<String, Option> actualOptionMap = (Map<String, Option>) optionGroup.getOptionMap();

        Assert.assertEquals(expectedOptionMap, actualOptionMap);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("option1");
        expectedNames.add("option2");

        Assert.assertEquals(expectedNames, names);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Description for option1");

        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelectedOptionAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            // Expected
        }

        try {
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedNewOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option2);

        Assert.assertEquals("option2", optionGroup.getSelected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullGroup() {
        Option option = new Option("option1", "Description for option1");

        try {
            OptionGroup optionGroup = null;
            optionGroup.setSelected(option);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            // Expected
        }

        try {
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            // Expected
        }
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

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }
}