// OptionGroupTest.java - Expert 1
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        // Test adding an option
        OptionGroup result = optionGroup.addOption(option);
        assert (result == optionGroup); // Check if the method returns the same OptionGroup instance
        assert (optionMap.containsKey(option.getKey())); // Check if the option was added to the map

        // Test adding the same option twice
        OptionGroup testResult = optionGroup.addOption(option);
        assert (result == optionGroup); // Check if the method returns the same OptionGroup instance
        assert (optionMap.size() == 1); // Check if the option was only added once

        // Test adding an option with the same key as an existing one
        OptionGroup wrongOption = new OptionGroup();
        Option wrongOptionOption = new Option("option1", "Option 1 description");
        OptionGroup wrongResult = wrongOption.addOption(option);
        OptionGroup wrongTestResult = wrongOption.addOption(wrongOptionOption);
        assert (wrongResult != wrongOption); // Check if a new OptionGroup is returned
        assert (optionMap.size() == 1); // Check if the original OptionGroup was not affected
    }
}