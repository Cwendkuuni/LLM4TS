import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = Option.builder("option").longOpt("long-option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        Assert.assertEquals(1, cmdLine.getOptions().size());
        Assert.assertEquals(option, cmdLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = Option.builder("option").valueSeparator('=').build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        Boolean switchValue = cmdLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_AddSwitch_SwitchAlreadySet() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        Assert.assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        Option actualOption = cmdLine.getOption("option");
        Assert.assertEquals(option, actualOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        List values = cmdLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        Boolean switchValue = cmdLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        Option option = Option.builder("option")
                .build()
                .setValueSeparator('=')
                .valueSeparator(':')
                .required(false)
                .longOpt("long-option")
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        cmdLine.addProperty(option, "property", "value");
        String propertyValue = cmdLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = Option.builder("option")
                .build()
                .setValueSeparator('=')
                .valueSeparator(':')
                .required(false)
                .longOpt("long-option")
                .build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        cmdLine.addProperty(option, "property", properties);
        Map<String, String> actualProperties = cmdLine.getProperties(option);
        Assert.assertEquals(1, actualProperties.size());
        Assert.assertEquals("value1", actualProperties.get("property1"));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        String cmdLineString = cmdLine.toString();
        Assert.assertTrue(cmdLineString.contains(option.getLongOpt()));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        List options = cmdLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        Set optionTriggers = cmdLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains(option1.getPreferredName()));
        Assert.assertTrue(optionTriggers.contains(option2.getPreferredName()));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultValues() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        cmdLine.setDefaultValues(option, defaultValues);
        List values = cmdLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultSwitch() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.setDefaultSwitch(option, true);
        Boolean switchValue = cmdLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetNormalised() {
        Option option = Option.builder("option").build();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        cmdLine.addOption(option);
        List normalised = cmdLine.getNormalised();
        Assert.assertEquals(0, normalised.size());
    }
}