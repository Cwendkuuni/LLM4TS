package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLine writeableCommandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setUp() {
        Option rootOption = new Option("test", "Test options", true, "test");
        List arguments = new ArrayList();
        arguments.add("--test");
        arguments.add("--property=value");
        arguments.add("--switch");
        arguments.add("--value1");
        arguments.add("--value2");

        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        optionWithValue = new Option("value", "Value option", true, "value");
        optionWithSwitch = new Option("switch", "Switch option", false, null);
        optionWithProperty = new Option("property", "Property option", true, "property");
        optionWithProperty.setArgName("propertyName");
        optionWithProperty.setArgNameRequired(false);
        optionWithProperty.setDefaultValue("defaultValue");

        writeableCommandLine.addOption(optionWithValue);
        writeableCommandLine.addOption(optionWithSwitch);
        writeableCommandLine.addOption(optionWithProperty);
    }

    @Test
    public void testAddValue() {
        writeableCommandLine.addValue(optionWithValue, "testValue");
        List values = writeableCommandLine.getValues(optionWithValue, null);
        assertEquals("testValue", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueTwice() {
        writeableCommandLine.addValue(optionWithValue, "testValue");
        writeableCommandLine.addValue(optionWithValue, "testValue2");
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(optionWithSwitch, true);
        Boolean switchValue = writeableCommandLine.getSwitch(optionWithSwitch, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetValues() {
        writeableCommandLine.addValue(optionWithValue, "testValue1");
        writeableCommandLine.addValue(optionWithValue, "testValue2");
        List values = writeableCommandLine.getValues(optionWithValue, null);
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        List undefaultedValues = writeableCommandLine.getUndefaultedValues(optionWithValue);
        assertTrue(undefaultedValues.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(optionWithSwitch, true);
        Boolean switchValue = writeableCommandLine.getSwitch(optionWithSwitch, false);
        assertTrue(switchValue);

        writeableCommandLine.addSwitch(optionWithSwitch, false);
        switchValue = writeableCommandLine.getSwitch(optionWithSwitch, true);
        assertFalse(switchValue);
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty(optionWithProperty, "propertyName1", "propertyValue1");
        Map properties = writeableCommandLine.getProperties(optionWithProperty);
        assertEquals(1, properties.size());
        assertEquals("propertyName1", properties.keySet().iterator().next());
        assertEquals("propertyValue1", properties.get("propertyName1"));
    }

    @Test
    public void testAddPropertyWithDefaultValue() {
        writeableCommandLine.addProperty(optionWithProperty, "propertyName2", "defaultValue");
        Map properties = writeableCommandLine.getProperties(optionWithProperty);
        assertEquals(1, properties.size());
        assertEquals("propertyName2", properties.keySet().iterator().next());
        assertEquals("defaultValue", properties.get("propertyName2"));
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty(optionWithProperty, "propertyName3", "propertyValue3");
        Map properties = writeableCommandLine.getProperties(optionWithProperty);
        assertEquals(1, properties.size());
        assertEquals("propertyName3", properties.keySet().iterator().next());
        assertEquals("propertyValue3", properties.get("propertyName3"));

        properties = writeableCommandLine.getProperties();
        assertTrue(properties.isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseError() {
        Option rootOption = new Option("test", "Test options", true, "test");
        List arguments = new ArrayList();
        arguments.add("--test");
        arguments.add("--invalidOption");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        writeableCommandLine.parse();
    }
}