import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("optionName"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("testValue"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), values);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = commandLine.getOption(option).getPropertyValue("propertyName");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--optionName"));
        Assert.assertFalse(commandLine.looksLikeOption("testValue"));
    }

    @Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();
        commandLine.setCurrentOption(option1);
        commandLine.setCurrentOption(option2);
        Assert.assertEquals(option2, commandLine.getCurrentOption());
    }

    private WriteableCommandLine createMockCommandLine() {
        return new CliOptions();
    }

}