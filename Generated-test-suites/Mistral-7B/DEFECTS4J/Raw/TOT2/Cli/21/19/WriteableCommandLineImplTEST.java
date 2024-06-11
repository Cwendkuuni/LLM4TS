package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());

        commandLine.addOption(createOption("opt2"));

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option option1 = (Option) options.get(0);
        Option option2 = (Option) options.get(1);
        Assert.assertEquals("opt", option1.getLongOpt());
        Assert.assertEquals("opt", option1.getOpt());
        Assert.assertEquals("opt", option1.getName());
        Assert.assertEquals("opt description", option1.getDescription());
        Assert.assertEquals("opt2", option2.getLongOpt());
        Assert.assertEquals("opt2", option2.getOpt());
        Assert.assertEquals("opt2", option2.getName());
        Assert.assertNull(option2.getDescription());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List values = commandLine.getValues(commandLine.getOption("opt"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        commandLine.addSwitch(commandLine.getOption("opt"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("opt"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        commandLine.addSwitch(commandLine.getOption("opt"), true);
        commandLine.addSwitch(commandLine.getOption("opt"), true);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        Assert.assertTrue(commandLine.hasOption(commandLine.getOption("opt")));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        Option option = commandLine.getOption("opt");
        Assert.assertEquals("opt", option.getLongOpt());
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("opt", option.getName());
        Assert.assertEquals("opt description", option.getDescription());
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List values = commandLine.getValues(commandLine.getOption("opt"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        values = commandLine.getValues(commandLine.getOption("opt"), Collections.singletonList("value3"));
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List values = commandLine.getUndefaultedValues(commandLine.getOption("opt"));
        Assert.assertEquals(0, values.size());

        commandLine.setDefaultValues(commandLine.getOption("opt"), Collections.singletonList("value3"));

        values = commandLine.getUndefaultedValues(commandLine.getOption("opt"));
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        commandLine.addSwitch(commandLine.getOption("opt"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("opt"), false);
        Assert.assertTrue(switchValue);

        switchValue = commandLine.getSwitch(commandLine.getOption("opt"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(new PropertyOption("prop", false, "defaultValue"));
        commandLine.addProperty(commandLine.getOption("opt"), "prop", "value");

        String propertyValue = commandLine.getProperty("prop");
        Assert.assertEquals("value", propertyValue);

        propertyValue = commandLine.getProperty("prop", "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(new PropertyOption("prop", false, "defaultValue"));

        Map<String, String> properties = new HashMap<>();
        properties.put("prop", "value");

        commandLine.addProperty(commandLine.getOption("opt"), "prop", properties);

        Map<String, String> propertiesValue = commandLine.getProperties(commandLine.getOption("opt"));
        Assert.assertEquals(1, propertiesValue.size());
        Assert.assertEquals("value", propertiesValue.get("prop"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(new PropertyOption("prop1", false, "defaultValue1"));
        commandLine.addOption(new PropertyOption("prop2", false, "defaultValue2"));
        commandLine.addProperty(commandLine.getOption("opt"), "prop1", new HashMap<String, String>(){{
            put("prop1.key1", "prop1.value1");
            put("prop1.key2", "prop1.value2");
        }});

        Map<String, String> propertiesValue1 = commandLine.getProperties(commandLine.getOption("opt"));
        Assert.assertEquals(2, propertiesValue1.size());
        Assert.assertEquals("prop1.value1", propertiesValue1.get("prop1.key1"));
        Assert.assertEquals("prop1.value2", propertiesValue1.get("prop1.key2"));

        Map<String, String> propertiesValue2 = commandLine.getProperties(commandLine.getOption("opt"), "prop2");
        Assert.assertEquals(1, propertiesValue2.size());
        Assert.assertEquals("defaultValue2", propertiesValue2.get("prop2"));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());

        Assert.assertFalse(commandLine.looksLikeOption("arg"));
        Assert.assertTrue(commandLine.looksLikeOption("--opt"));
        Assert.assertTrue(commandLine.looksLikeOption("-opt"));
        Assert.assertTrue(commandLine.looksLikeOption("-opt="));
        Assert.assertTrue(commandLine.looksLikeOption("--opt="));
        Assert.assertTrue(commandLine.looksLikeOption("--opt-"));
        Assert.assertTrue(commandLine.looksLikeOption("-opt-"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.singletonList("arg1"));

        String commandLineString = commandLine.toString();
        Assert.assertEquals("--opt arg1", commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt1"), Collections.emptyList());
        commandLine.addOption(createOption("opt2"));

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());

        Option option1 = (Option) options.get(0);
        Option option2 = (Option) options.get(1);

        Assert.assertEquals("opt1", option1.getLongOpt());
        Assert.assertEquals("opt1", option1.getOpt());
        Assert.assertEquals("opt1", option1.getName());
        Assert.assertEquals("opt1 description", option1.getDescription());

        Assert.assertEquals("opt2", option2.getLongOpt());
        Assert.assertEquals("opt2", option2.getOpt());
        Assert.assertEquals("opt2", option2.getName());
        Assert.assertNull(option2.getDescription());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt1"), Collections.emptyList());
        commandLine.addOption(createOption("opt1"));
        commandLine.addOption(createOption("opt2"));

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("opt1"));
        Assert.assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        List defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        defaultValues.add("value2");

        commandLine.setDefaultValues(commandLine.getOption("opt"), defaultValues);

        List values = commandLine.getValues(commandLine.getOption("opt"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.emptyList());
        commandLine.addOption(createOption("opt"));

        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(commandLine.getOption("opt"), defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("opt"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("opt"), Collections.singletonList("arg1"));

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
    }
}