import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        // Test addOption() method
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");
        Option option3 = new Option("o3", "option3", false, "Description for option3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test getNames() method
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("o1");
        expectedNames.add("o2");
        expectedNames.add("o3");

        List<String> names = new ArrayList<>(optionGroup.getNames());
        Collections.sort(expectedNames);
        Collections.sort(names);

        Assert.assertEquals(expectedNames, names);

        // Test getOptions() method
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(option3);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        Assert.assertEquals(expectedOptions, options);

        // Test setSelected() method
        optionGroup.setSelected(option1);
        String selectedOption = optionGroup.getSelected();

        Assert.assertEquals(option1.getOpt(), selectedOption);

        // Test setSelected() method with already selected option
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
        } catch (org.apache.commons.cli.AlreadySelectedException e) {
            // Expected exception
        }

        // Test isRequired() method
        optionGroup.setRequired(true);
        boolean isRequired = optionGroup.isRequired();

        Assert.assertTrue(isRequired);

        optionGroup.setRequired(false);
        isRequired = optionGroup.isRequired();

        Assert.assertFalse(isRequired);

        // Test toString() method
        String expectedToString = "[-o1 Description for option1, -o2 Description for option2, --option3 Description for option3]";
        String toString = optionGroup.toString();

        Assert.assertEquals(expectedToString, toString);
    }
}