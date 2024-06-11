import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cmd(); // Assuming Cmd is a concrete implementation of WriteableCommandLine
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').buildValueSet();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").buildValueSet().valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").buildValueSet().valueSeparator('=').build();
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_existing() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, false);
        Assert.assertFalse(option.isSet());
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValues(option.getLongOpt()).get(0));
    }

    @Test
    public void testAddProperty_default() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.addProperty("property", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getProperty("property"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLooksLikeOption_invalidArgument() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.looksLikeOption("invalidArgument");
    }

    @Test
    public void testLooksLikeOption_validOption() {
        WriteableCommandLine commandLine = new Cmd();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.parse(new String[]{"--option"});
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }
}