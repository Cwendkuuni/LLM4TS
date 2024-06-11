package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        option1 = new Option("o", "option1")
                .required(false)
                .longOpt("longOption1")
                .hasArg(true)
                .valueSeparator('=');

        option2 = new Option("s", "option2")
                .required(false)
                .longOpt("longOption2")
                .hasArg(false);

        writeableCommandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        assertTrue(writeableCommandLine.hasOption(option1));
        assertTrue(writeableCommandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");

        List values = writeableCommandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option1, true);
        assertTrue(writeableCommandLine.getSwitch(option1, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        writeableCommandLine.addSwitch(option1, true);
        writeableCommandLine.addSwitch(option1, true);
    }

    @Test
    public void testHasOption() {
        assertTrue(writeableCommandLine.hasOption(option1));
        assertFalse(writeableCommandLine.hasOption(new Option("nonexistent")));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption1 = writeableCommandLine.getOption("option1");
        assertEquals(option1, retrievedOption1);
        assertNull(writeableCommandLine.getOption("nonexistent"));
    }

    @Test
    public void testGetValues() {
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");

        List values = writeableCommandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");

        List values = writeableCommandLine.getUndefaultedValues(option1);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(option1, true);

        assertTrue(writeableCommandLine.getSwitch(option1, false));
        assertFalse(writeableCommandLine.getSwitch(option1, true));
    }

    @Test
    public void testGetProperty() {
        writeableCommandLine.addProperty(option1, "property1", "value1");

        assertEquals("value1", writeableCommandLine.getProperty(option1, "property1", null));
        assertNull(writeableCommandLine.getProperty(option1, "property2", null));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty(option1, "property1", "value1");

        Map properties = writeableCommandLine.getProperties(option1);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.get("property1"));
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty(option1, "property1", "value1");
        writeableCommandLine.addProperty(option1, "property2", "value2");

        Map properties = writeableCommandLine.getProperties(option1);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        writeableCommandLine = new WriteableCommandLineImpl(new Option("test"), arguments);

        String result = writeableCommandLine.toString();
        String expected = "arg1 arg2";
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains
                (option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }
}