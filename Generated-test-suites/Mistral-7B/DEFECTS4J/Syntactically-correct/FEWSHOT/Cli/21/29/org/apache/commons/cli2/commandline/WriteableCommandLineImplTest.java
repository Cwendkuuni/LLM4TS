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
    public void testAddOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromList = (Option) options.get(0);
        assertEquals(option, optionFromList);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Option result = writeableCommandLine.getOption("o");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);

        writeableCommandLine.addSwitch(option, false);
        switchValue = writeableCommandLine.getSwitch(option, true);
        assertFalse(switchValue);

        writeableCommandLine.addSwitch(option, null);
        switchValue = writeableCommandLine.getSwitch(option, false);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        String propertyValue1 = writeableCommandLine.getProperty(option, "property1");
        assertEquals("value1", propertyValue1);

        String propertyValue2 = writeableCommandLine.getProperty(option, "property2");
        assertEquals("value2", propertyValue2);

        String propertyValue3 = writeableCommandLine.getProperty(option, "property3");
        assertNull(propertyValue3);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Map optionToPropertiesMap = writeableCommandLine.getOptionToProperties();
        Map expectedOptionToPropertiesMap = new HashMap();
        expectedOptionToPropertiesMap.put(option, properties);

        assertEquals(expectedOptionToPropertiesMap, optionToPropertiesMap);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Set propertiesSet = writeableCommandLine.getProperties(option);
        Set expectedPropertiesSet = properties.keySet();

        assertEquals(expectedPropertiesSet, propertiesSet);
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        String result = writeableCommandLine.toString();
        String expected = "option value1 value2";

        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "an option1");
        Option option2 = new Option("o2", "option2", false, "an option2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();

        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        assertEquals(expectedOptions, options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", true, "an option1");
        Option option2 = new Option("o2", "option2", false, "an option2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();

        Set expectedOptionTriggers = new HashSet<>();
        expectedOptionTriggers.add("o1");
        expectedOptionTriggers.add("o2");

        assertEquals(expectedOptionTriggers, optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        List defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(0, values.size());

        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, unfaultedValues.size());

        List expectedValues = new ArrayList<>();
        expectedValues.add("default1");
        expectedValues.add("default2");

        values = writeableCommandLine.getValues(option, Collections.emptyList());
        assertEquals(expectedValues, values);

        unfaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(expectedValues, unfaultedValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = Boolean.TRUE;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        List normalised = writeableCommandLine.getNormalised();

        List expectedNormalised = new ArrayList<>();
        expectedNormalised.add("o");

        assertEquals(expectedNormalised, normalised);
    }
}