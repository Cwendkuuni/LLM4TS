package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption_shouldAddOptionToOptionsListAndNameToOptionMap() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        Map nameToOption = writeableCommandLine.getNameToOption();

        assertEquals(1, options.size());
        assertEquals(option, options.get(0));

        assertEquals(option.getPreferredName(), nameToOption.get(option.getPreferredName()));
        assertEquals(option, nameToOption.get(option.getLongOpt()));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue_shouldAddValueToValueList() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch_shouldAddSwitchToSwitchesMap() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addSwitch(option, true);

        Map switches = writeableCommandLine.getSwitches();

        assertEquals(true, switches.get(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_shouldThrowIllegalStateExceptionWhenSwitchIsAlreadySet() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption_shouldReturnTrueIfOptionIsPresent() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption_shouldReturnFalseIfOptionIsNotPresent() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        assertFalse(writeableCommandLine.hasOption(new Option("n", "noopt")));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption_shouldReturnOptionWithSpecifiedName() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addOption(option);

        Option returnedOption = writeableCommandLine.getOption("opt");

        assertEquals(option, returnedOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteableCommandLineImpl_getOption_shouldThrowIllegalArgumentExceptionIfNoSuchOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        writeableCommandLine.getOption("noopt");
    }

    @Test
    public void testWriteableCommandLineImpl_getValues_shouldReturnValuesListForSpecifiedOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getValues_shouldReturnEmptyListIfNoValuesSpecifiedForOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_getValues_shouldReturnDefaultValuesIfNoValuesAndDefaultValuesSpecified() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues_shouldReturnEmptyListIfNoValuesSpecifiedForOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        List values = writeableCommandLine.getUndefaultedValues(option);

        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch_shouldReturnSwitchValueForSpecifiedOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);

        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch_shouldReturnDefaultSwitchValueForSpecifiedOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.setDefaultSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);

        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch_shouldReturnNullIfNoSwitchSpecifiedForOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);

        assertNull(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty_shouldReturnPropertyValueForSpecifiedOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, "property1", "value1");

        String propertyValue = writeableCommandLine.getProperty(option, "property1");

        assertEquals("value1", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty_shouldReturnDefaultPropertyValueForSpecifiedOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        Properties properties = new Properties();
        properties.setProperty("property1", "defaultValue1");
        writeableCommandLine.addProperty(option, "property1", "defaultValue1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        String propertyValue = writeableCommandLine.getProperty(option, "property2");

        assertEquals("value2", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty_shouldReturnNullIfNoPropertySpecifiedForOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        String propertyValue = writeableCommandLine.getProperty(option, "property1");

        assertNull(propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties_shouldReturnPropertiesMapForSpecifiedOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, "property1", "value1");

        Map propertiesMap = writeableCommandLine.getProperties(option);

        assertEquals(1, propertiesMap.size());
        assertEquals("property1", propertiesMap.keySet().iterator().next());
        assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties_shouldReturnEmptyMapIfNoPropertiesSpecifiedForOption() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);

        Map propertiesMap = writeableCommandLine.getProperties(option);

        assertEquals(0, propertiesMap.size());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption_shouldReturnTrueIfStringStartsWithOptionPrefix() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "opt"), null);

        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertTrue(writeableCommandLine.looksLikeOption("opt"));
        assertTrue(writeableCommandLine.looksLikeOption("--opt"));
        assertTrue(writeableCommandLine.looksLikeOption("-o"));
        assertTrue(writeableCommandLine.looksLikeOption("-opt"));

        assertFalse(writeableCommandLine.looksLikeOption("--noopt"));
        assertFalse(writeableCommandLine.looksLikeOption("-noopt"));
        assertFalse(writeableCommandLine.looksLikeOption("noopt"));
        assertFalse(writeableCommandLine.looksLikeOption("foo"));
        assertFalse(writeableCommandLine.looksLikeOption("bar"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString_shouldReturnStringRepresentationOfCommandLineArguments() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        String commandLineString = writeableCommandLine.toString();

        assertEquals("value1 value2", commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions_shouldReturnListOfOptionsSpecifiedInCommandLine() {
        Option option1 = new Option("o1", "opt1");
        Option option2 = new Option("o2", "opt2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();

        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers_shouldReturnSetOfOptionTriggers() {
        Option option1 = new Option("o1", "opt1");
        Option option2 = new Option("o2", "opt2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues_shouldAddDefaultValuesToDefaultValuesMap() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        Map defaultValuesMap = writeableCommandLine.getDefaultValues();

        assertEquals(1, defaultValuesMap.size());
        assertEquals(option, defaultValuesMap.keySet().iterator().next());
        assertEquals(defaultValues, defaultValuesMap.get(option));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch_shouldAddDefaultSwitchToDefaultSwitchesMap() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.setDefaultSwitch(option, true);

        Map defaultSwitchesMap = writeableCommandLine.getDefaultSwitches();

        assertEquals(1, defaultSwitchesMap.size());
        assertEquals(option, defaultSwitchesMap.keySet().iterator().next());
        assertEquals(Boolean.TRUE, defaultSwitchesMap.get(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised_shouldReturnListOfNormalisedCommandLineArguments() {
        Option option = new Option("o", "opt");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addValue(option, "value1 ");
        writeableCommandLine.addValue(option, "value2");

        List normalised = writeableCommandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("value1", normalised.get(0));
        assertEquals("value2", normalised.get(1));
    }
}