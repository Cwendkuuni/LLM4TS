import org.apache.commons.cli.Option;
import org.apache.commons.cli. OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for OptionGroup class.
 */
public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        // Creating an OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Testing getNames() method
        List names = Arrays.asList("option1", "option2", "option3");
        for (String name : names) {
            Option option = new Option(name, false, false, "");
            optionGroup.addOption(option);
        }
        List actualNames = Collections.list(optionGroup.getNames());
        Assert.assertEquals(names, actualNames);

        // Testing getOptions() method
        List options = Arrays.asList(new Option("option1", false, false, ""),
                new Option("option2", false, false, ""),
                new Option("option3", false, false, ""));
        List actualOptions = Collections.list(optionGroup.getOptions());
        Assert.assertEquals(options, actualOptions);

        // Testing setSelected() method
        Option option1 = optionGroup.getOptions().iterator().next();
        optionGroup.setSelected(option1);
        String expectedSelected = option1.getOpt();
        String actualSelected = optionGroup.getSelected();
        Assert.assertEquals(expectedSelected, actualSelected);

        // Testing isRequired() method
        optionGroup.setRequired(true);
        boolean expectedRequired = true;
        boolean actualRequired = optionGroup.isRequired();
        Assert.assertEquals(expectedRequired, actualRequired);

        // Testing toString() method
        String expectedToString = "[-option1 Option1\n, -option2 Option2\n, -option3 Option3\n]";
        String actualToString = optionGroup.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", false, false, "");
        Option option2 = new Option("option1", false, false, "");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);
    }
}