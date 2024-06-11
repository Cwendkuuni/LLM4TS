package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        cmd.addOption(option);

        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        String value = "testValue";

        cmd.addValue(option, value);

        List<String> values = cmd.getValues(option, null);

        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        assertFalse(cmd.getSwitch(option, null));

        cmd.addSwitch(option, true);

        assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        assertFalse(cmd.hasOption(option));

        cmd.addOption(option);

        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option();
        Option option2 = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, arguments);

        assertNull(cmd.getOption("testTrigger"));

        cmd.addOption(option1);
        cmd.addOption(option2);

        assertNotNull(cmd.getOption(option1.getPreferredName()));
        assertNotNull(cmd.getOption(option2.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        List<String> values = cmd.getValues(option, defaultValues);

        assertEquals(defaultValues, values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        cmd.addValue(option, values.get(0));
        cmd.addValue(option, values.get(1));

        List<String> undefaultedValues = cmd.getUndefaultedValues(option);

        assertEquals(values, undefaultedValues);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        Boolean defaultValue = true;
        Boolean switchValue = false;

        assertFalse(cmd.getSwitch(option, defaultValue));

        cmd.addSwitch(option, switchValue);

        assertEquals(switchValue, cmd.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        String property = "testProperty";
        String value = "testValue";

        assertNull(cmd.getProperty(property));

        cmd.addProperty(option, property, value);

        assertEquals(value, cmd.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        String property = "testProperty";
        String value = "testValue";

        cmd.addProperty(option, property, value);

        assertEquals(value, cmd.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        cmd.addProperty(option, "property1", "value1");
        cmd.addProperty(option, "property2", "value2");

        assertEquals(properties.keySet(), cmd.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        String trigger = "-testTrigger";

        assertTrue(cmd.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        String arg1 = "arg1";
        String arg2 = "arg2";

        cmd.addNormalised(arg1);
        cmd.addNormalised(arg2);

        String expectedString = arg1 + " " + arg2;

        assertEquals(expectedString, cmd.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option();
        Option option2 = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, arguments);

        cmd.addOption(option1);
        cmd.addOption(option2);

        List<Option> options = cmd.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option();
        Option option2 = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, arguments);

        cmd.addOption(option1);
        cmd.addOption(option2);

        assertEquals(2, cmd.getOptionTriggers().size());
        assertTrue(cmd.getOptionTriggers().contains(option1.getPreferredName()));
        assertTrue(cmd.getOptionTriggers().contains(option2.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        cmd.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, cmd.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        Boolean defaultSwitch = true;

        cmd.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, cmd.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option();
        List<Argument> arguments = new ArrayList<>();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        String arg1 = "arg1";
        String arg2 = "arg2";

        cmd.addNormalised(arg1);
        cmd.addNormalised(arg2);

        List<String> normalised = cmd.getNormalised();

        assertEquals(2, normalised.size());
        assertTrue(normalised.contains(arg1));
        assertTrue(normalised.contains(arg2));
    }
}