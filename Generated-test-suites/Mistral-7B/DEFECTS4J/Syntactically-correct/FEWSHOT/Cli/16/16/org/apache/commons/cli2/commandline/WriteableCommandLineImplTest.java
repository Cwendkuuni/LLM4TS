package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testCreateInstance() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertSame(rootOption, writeableCommandLine.getRootOption());
        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getOptionToProperties());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getValues());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getNameToOption());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getSwitches());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), writeableCommandLine.getNormalised());
        assertNull(writeableCommandLine.getPrefixes());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option1 = new Option("opt1", "option 1");
        writeableCommandLine.addOption(option1);

        assertEquals(Collections.singletonList(option1), writeableCommandLine.getOptions());
        assertSame(option1, writeableCommandLine.getNameToOption().get("opt1"));
        assertTrue(writeableCommandLine.hasOption(option1));
        assertEquals(Collections.emptyMap(), writeableCommandLine.getOptionToProperties());
        assertEquals(Collections.emptyList(), writeableCommandLine.getValues(option1, null));
        assertEquals(Collections.emptyMap(), writeableCommandLine.getSwitches());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), writeableCommandLine.getNormalised());
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        Argument argument = new Argument("arg", "argument");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, argument);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(Collections.singletonList(argument), values);
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSwitchTwice() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        Option retrievedOption = writeableCommandLine.getOption("opt");

        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        Argument value1 = new Argument("value1", "value 1");
        Argument value2 = new Argument("value2", "value 2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, value1);
        writeableCommandLine.addValue(option, value2);

        List retrievedValues = writeableCommandLine.getValues(option, null);
        assertEquals(Collections.singletonList(Collections.singletonList(value1, value2)), retrievedValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        Argument value1 = new Argument("value1", "value 1");
        Argument value2 = new Argument("value2", "value 2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, value1);

        List retrievedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(Collections.singletonList(value1), retrievedValues);
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean retrievedSwitch = writeableCommandLine.getSwitch(option, false);
        assertTrue(retrievedSwitch);
    }

    @Test
    public void testGetDefaultValues() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List retrievedValues = writeableCommandLine.getValues(option, null);
        assertEquals(defaultValues, retrievedValues);
    }

    @Test
    public void testGetDefaultSwitch() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean retrievedSwitch = writeableCommandLine.getSwitch(option, false);
        assertTrue(retrievedSwitch);
    }

    @Test
    public void testGetProperties() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Set retrievedProperties = writeableCommandLine.getProperties(option);
        assertEquals(Collections.singleton(properties), retrievedProperties);
    }

    @Test
    public void testGetProperty() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = new Option("opt", "option");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");

        String retrievedProperty = writeableCommandLine.getProperty(option, "property1", null);
        assertEquals("value1", retrievedProperty);
    }

    @Test
    public void testToString() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        writeableCommandLine.addOption(new Option("opt1", "opt1"));
        writeableCommandLine.addOption(new Option("opt2", "opt2"));
        writeableCommandLine.addOption(new Option("opt3", "opt3"));

        String expectedResult = "-opt1 -opt2 foo -opt3 bar";
        String actualResult = writeableCommandLine.toString();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetOptionsList() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        writeableCommandLine.addOption(new Option("opt1", "opt1"));
        writeableCommandLine.addOption(new Option("opt2", "opt2"));
        writeableCommandLine.addOption(new Option("opt3", "opt3"));

        List retrievedOptions = writeableCommandLine.getOptions();
        assertEquals(3, retrievedOptions.size());
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option opt1 = new Option("opt1", "opt1");
        Option opt2 = new Option("opt2", "opt2");
        Option opt3 = new Option("opt3", "opt3");

        writeableCommandLine.addOption(opt1);
        writeableCommandLine.addOption(opt2);
        writeableCommandLine.addOption(opt3);

        Set retrievedTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(3, retrievedTriggers.size());
    }
}