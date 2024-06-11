import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a concrete implementation of WriteableCommandLine

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator(' ')
                .required(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator(' ')
                .required(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator(' ')
                .required(true)
                .build();
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>(Arrays.asList("default1", "default2"));
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("default1", defaultValues.get(0));
        Assert.assertEquals("default2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(true, option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "propertyValue");
        Assert.assertEquals("propertyValue", option.getProperties().get("property"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("defaultProperty", "defaultPropertyValue");
        Assert.assertEquals("defaultPropertyValue", commandLine.getProperty("defaultProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}