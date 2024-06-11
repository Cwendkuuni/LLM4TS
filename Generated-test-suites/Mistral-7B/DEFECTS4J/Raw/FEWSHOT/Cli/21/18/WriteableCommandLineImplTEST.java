package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromList = (Option) options.get(0);
        assertEquals(option, optionFromList);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Argument arg = new Argument("arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        Argument argFromList = (Argument) values.get(0);
        assertEquals(arg, argFromList);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addOption(option);

        boolean hasOption = writeableCommandLine.hasOption(option);
        assertTrue(hasOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addOption(option);

        Option optionFromMap = writeableCommandLine.getOption("opt");
        assertEquals(option, optionFromMap);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Argument arg1 = new Argument("arg1");
        Argument arg2 = new Argument("arg2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        Argument arg1FromList = (Argument) values.get(0);
        assertEquals(arg1, arg1FromList);
        Argument arg2FromList = (Argument) values.get(1);
        assertEquals(arg2, arg2FromList);
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Argument arg1 = new Argument("arg1");
        Argument arg2 = new Argument("arg2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        PropertyOption propertyOption = new PropertyOption("prop", "property", true);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "prop", "value");

        String propertyValue = writeableCommandLine.getProperty("prop");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        PropertyOption propertyOption = new PropertyOption("prop", "property", true);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "prop", "value");

        Properties properties = writeableCommandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value", properties.getProperty("prop"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        PropertyOption propertyOption1 = new PropertyOption("prop1", "property1", true);
        PropertyOption propertyOption2 = new PropertyOption("prop2", "property2", true);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "prop1", "value1");
        writeableCommandLine.addProperty(option, "prop2", "value2");

        Set properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("prop1"));
        assertTrue(properties.contains("prop2"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Argument arg1 = new Argument("arg1");
        Argument arg2 = new Argument("arg2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        String commandLineString = writeableCommandLine.toString();
        String expectedCommandLineString = "arg1 arg2";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        Option option1FromList = (Option) options.get(0);
        assertEquals(option1, option1FromList);
        Option option2FromList = (Option) options.get(1);
        assertEquals(option2, option2FromList);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Argument arg1 = new Argument("arg1");
        Argument arg2 = new Argument("arg2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        List defaultValues = new ArrayList();
        defaultValues.add(new Argument("defaultArg1"));
        defaultValues.add(new Argument("defaultArg2"));

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        Argument arg1FromList = (Argument) values.get(0);
        assertEquals(arg1, arg1FromList);
        Argument arg2FromList = (Argument) values.get(1);
        assertEquals(arg2, arg2FromList);

        List defaultValuesFromMap = (List) writeableCommandLine.getDefaultValues(option);
        assertEquals(2, defaultValuesFromMap.size());
        Argument defaultArg1FromList = (Argument) defaultValuesFromMap.get(0);
        assertEquals(arg1, defaultArg1FromList);
        Argument defaultArg2FromList = (Argument) defaultValuesFromMap.get(1);
        assertEquals(arg2, defaultArg2FromList);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, false);

        Boolean defaultSwitch = false;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertFalse(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = new Option("opt", "option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Argument arg1 = new Argument("arg1");
        Argument arg2 = new Argument("arg2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, arg1);
        writeableCommandLine.addValue(option, arg2);

        List normalised = writeableCommandLine.getNormalised();
        assertEquals(2, normalised.size());
        String arg1FromList = (String) normalised.get(0);
        assertEquals("arg1", arg1FromList);
        String arg2FromList = (String) normalised.get(1);
        assertEquals("arg2", arg2FromList);
    }
}