import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    // Test case for addOption() method
    @Test
    public void testAddOption() {
        Option option = new OptionImpl("-f", "file", false, "Specifies the file");
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    // Test case for addValue() method
    @Test
    public void testAddValue() {
        Option option = new OptionImpl("-o", "output", true, "Specifies the output");
        String value = "output.txt";
        commandLine.addValue(option, value);
        Assert.assertEquals(1, commandLine.getValues(option).size());
        Assert.assertEquals(value, commandLine.getValues(option).get(0));
    }

    // Test case for getValues() method with valid option
    @Test
    public void testGetValues_ValidOption() {
        Option option = new OptionImpl("-o", "output", true, "Specifies the output");
        String value1 = "output1.txt";
        String value2 = "output2.txt";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);
        Assert.assertEquals(expectedValues, commandLine.getValues(option));
    }

    // Test case for getDefaultValues() method
    @Test
    public void testGetDefaultValues() {
        Option option = new OptionImpl("-d", "default", true, "Specifies the default");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    // Test case for addSwitch() method
    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("-e", "enabled", false, "Specifies if enabled");
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, commandLine.getSwitch(option));
    }

    // Test case for setDefaultSwitch() method
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("-s", "switch", false, "Specifies the switch");
        commandLine.setDefaultSwitch(option, false);
        Assert.assertEquals(false, commandLine.getDefaultSwitch(option));
    }

    // Test case for addProperty() method
    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);
        Assert.assertEquals(value, commandLine.getProperties().get(property));
    }

    // Test case for looksLikeOption() method with valid argument
    @Test
    public void testLooksLikeOption_ValidArgument() {
        String argument = "-o";
        Assert.assertTrue(commandLine.looksLikeOption(argument));
    }

    // Test case for looksLikeOption() method with invalid argument
    @Test
    public void testLooksLikeOption_InvalidArgument() {
        String argument = "value";
        Assert.assertFalse(commandLine.looksLikeOption(argument));
    }
}