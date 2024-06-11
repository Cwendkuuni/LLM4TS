package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_constructor() {
        Option rootOption = new Option("root", "rootOption", true, "Root option");
        List arguments = new ArrayList();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertSame(rootOption, writeableCommandLine.getRootOption());
        assertSame(arguments, writeableCommandLine.getArguments());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Argument argument = new Argument("arg", "argument");
        writeableCommandLine.addValue(option, argument);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(argument, values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option);
        Option result = writeableCommandLine.getOption("opt");
        assertEquals(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Argument arg1 = new Argument("arg1", "arg1");
        Argument arg2 = new Argument("arg2", "arg2");
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals(arg1, values.get(0));
        assertEquals(arg2, values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Argument arg1 = new Argument("arg1", "arg1");
        Argument arg2 = new Argument("arg2", "arg2");
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addProperty(option, "property", "value");
        String result = writeableCommandLine.getProperty(option, "defaultValue");
        assertEquals("value", result);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, properties);
        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        assertEquals(1, propertiesMap.size());
        assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, properties);
        Set<String> propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(properties.stringPropertyNames()));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        String result = writeableCommandLine.toString();
        String expected = "-opt1 [arg] -opt2";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List optionsList = writeableCommandLine.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.contains(option1));
        assertTrue(optionsList.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggersSet = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggersSet.size());
        assertTrue(optionTriggersSet.containsAll(Arrays.asList("opt1", "opt2")));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List normalised = writeableCommandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("opt1"));
        assertTrue(normalised.contains("opt2"));
    }
}