import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=')
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(1, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));

        commandLine.addValue(option, "value2");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(2, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.setDefaultValues(option, values);

        commandLine.addValue(option, "value3");
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(3, undefaultedValues.size());
        Assert.assertEquals("value1", undefaultedValues.get(0));
        Assert.assertEquals("value2", undefaultedValues.get(1));
        Assert.assertEquals("value3", undefaultedValues.get(2));

        List<Object> defaultValues = commandLine.getDefaultValues(option);
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("value1", defaultValues.get(0));
        Assert.assertEquals("value2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, option.isSet());

        try {
            commandLine.addSwitch(option, false);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, option.getValues().get(0));

        commandLine.setDefaultSwitch(option, false);
        Assert.assertEquals(false, option.getValues().isEmpty());
        Assert.assertEquals(false, option.getValues().get(0));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();

        commandLine.addProperty(Option.builder("p").longOpt("property").build(), "propertyKey", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getProperty("propertyKey"));

        commandLine.addProperty("defaultPropertyKey", "defaultPropertyValue");
        Assert.assertEquals("defaultPropertyValue", commandLine.getProperty("defaultPropertyKey"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLine();

        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("--value"));
        Assert.assertFalse(commandLine.looksLikeOption("-v"));
    }
}

class CliCommandLine extends AbstractCommandLine implements WriteableCommandLine {
    // empty implementation
}