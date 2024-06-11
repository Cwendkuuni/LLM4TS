package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine() {
            List values = null;
            Option option = null;
            Object value = null;
            List defaultValues = null;
            Boolean defaultSwitch = null;
            String property = null;
            String propertyValue = null;

            public void addOption(Option option) {
                this.option = option;
            }

            public void addValue(Option option, Object value) {
                this.option = option;
                this.value = value;
            }

            public List getValues(Option option) {
                return values;
            }

            public void setDefaultValues(Option option, List defaultValues) {
                this.option = option;
                this.defaultValues = defaultValues;
            }

            public void addSwitch(Option option, boolean value) throws IllegalStateException {
                this.option = option;
                this.value = value;
            }

            public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
                this.option = option;
                this.defaultSwitch = defaultSwitch;
            }

            public void addProperty(String property, String value) {
                this.property = property;
                this.propertyValue = value;
            }

            public boolean looksLikeOption(String argument) {
                return false;
            }
        };
    }

    @After
    public void tearDown() {
        commandLine = null;
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = new Object();
        commandLine.addValue(option, value);
        assertEquals(option, commandLine.getOption());
        assertEquals(value, commandLine.getValue());
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        List values = commandLine.getValues(option);
        assertNull(values);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List defaultValues = null;
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(option, commandLine.getOption());
        assertEquals(defaultValues, commandLine.getDefaultValues());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(option, commandLine.getOption());
        assertEquals(value, commandLine.getValue());
        commandLine.addSwitch(option, value); // should throw IllegalArgumentException
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(option, commandLine.getOption());
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch());
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(property, commandLine.getProperty());
        assertEquals(value, commandLine.getPropertyValue());
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "testArgument";
        boolean result = commandLine.looksLikeOption(argument);
        assertFalse(result);
    }
}