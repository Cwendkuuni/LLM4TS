package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.arguments.Argument;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An example option")
                .create();

        commandLine.addOption(option);

        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .create();

        Argument arg = ArgumentBuilder.withName("arg1")
                .create();

        commandLine.addOption(option);
        commandLine.addValue(option, arg.getValue());

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(arg.getValue(), values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .isRequired(false)
                .create();

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .create();

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .create();

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option1");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .create();

        Argument arg1 = ArgumentBuilder.withName("arg1").setValue("value1")
                .create();
        Argument arg2 = ArgumentBuilder.withName("arg2").setValue("value2")
                .create();

        commandLine.addOption(option);
        commandLine.addValue(option, arg1.getValue());
        commandLine.addValue(option, arg2.getValue());

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals(Arrays.asList(arg1.getValue(), arg2.getValue()), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .create();

        Argument arg1 = ArgumentBuilder.withName("arg1").setValue("value1")
                .create();
        Argument arg2 = ArgumentBuilder.withName("arg2").setValue("value2")
                .create();

        commandLine.addOption(option);
        commandLine.addValue(option, arg1.getValue());

        List unfaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, unfaultedValues.size());
        assertEquals(arg1.getValue(), unfaultedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .isRequired(false)
                .create();

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        commandLine.addSwitch(option, false);

        switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        PropertyOption propertyOption = OptionBuilder.withLongOpt("option1")
                .hasArg(true)
                .valueSeparator('=')
                .create();

        commandLine.addProperty(propertyOption, "property", "value");

        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        PropertyOption propertyOption = OptionBuilder.withLongOpt("option1")
                .hasArg(true)
                .valueSeparator('=')
                .create();

        commandLine.addProperty(propertyOption, "property", "value");

        Map<String, String> properties = commandLine.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertEquals("value", properties.get("property"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        PropertyOption propertyOption1 = OptionBuilder.withLongOpt("option1")
                .hasArg(true)
                .valueSeparator('=')
                .create();

        PropertyOption propertyOption2 = OptionBuilder.withLongOpt("option2")
                .hasArg(true)
                .valueSeparator('=')
                .create();

        commandLine.addProperty(propertyOption1, "property1", "value1");
        commandLine.addProperty(propertyOption2, "property2", "value2");

        Map<String, String> properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>(Arrays.asList("arg1", "arg2")));

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option1 = OptionBuilder.withLongOpt("option1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option1 = OptionBuilder.withLongOpt("option1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .create();

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = OptionBuilder.withLongOpt("option1")
                .isRequired(false)
                .create();

        commandLine.setDefaultSwitch(option, Boolean.TRUE);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertTrue(switchValue);
    }
}