
package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private Object value;
    private List defaultValues;
    private String property;
    private String propertyValue;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        value = new Object();
        defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        option = new OptionImpl();
        property = "property";
        propertyValue = "value";
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, value);
        assertTrue(commandLine.hasValue(option, value));
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.setValues(option, values);
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsIllegalStateException() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, property, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addDefaultProperty(property, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testDoesNotLookLikeOption() {
        String argument = "value";
        assertFalse(commandLine.looksLikeOption(argument));
    }
}
