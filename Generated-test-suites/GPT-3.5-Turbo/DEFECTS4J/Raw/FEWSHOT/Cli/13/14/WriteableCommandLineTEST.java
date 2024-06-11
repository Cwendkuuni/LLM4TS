package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandline;
    private Option option;
    private Object value;

    @Before
    public void setup() {
        commandline = new WriteableCommandLineImpl();
        option = new Option("test");
        value = new Object();
    }

    @Test
    public void testAddOption() {
        commandline.addOption(option);
        assertTrue(commandline.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        commandline.addOption(option);
        commandline.addValue(option, value);
        assertTrue(commandline.hasValue(option));
        assertEquals(value, commandline.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        option = new Option("test", true, "test option");
        commandline.addOption(option);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        commandline.addValue(option, "value1");
        commandline.addValue(option, "value2");

        List<String> actualValues = commandline.getArgumentValues(option);
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSetDefaultValues() {
        option = new Option("test", true, "test option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        defaultValues.add("value2");
        commandline.setDefaultValues(option, defaultValues);

        assertTrue(commandline.hasOption("test"));
        assertEquals(defaultValues, commandline.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() throws IllegalStateException {
        commandline.addOption(option);
        commandline.addSwitch(option, true);
        assertTrue(commandline.hasSwitch(option));
        assertTrue(commandline.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        commandline.addOption(option);
        commandline.addSwitch(option, true);
        commandline.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandline.setDefaultSwitch(option, true);
        assertTrue(commandline.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";
        commandline.addProperty(property, value);

        assertTrue(commandline.hasProperty(property));
        assertEquals(value, commandline.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandline.looksLikeOption("-test"));
        assertTrue(commandline.looksLikeOption("--test"));
        assertFalse(commandline.looksLikeOption("test"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private Option option;
        private Object value;
        private List<Object> defaultValues;
        private boolean switchValue;
        private Boolean defaultSwitch;
        private String property;
        private String propertyValue;

        @Override
        public void addOption(Option option) {
            this.option = option;
        }

        @Override
        public void addValue(Option option, Object value) {
            this.option = option;
            this.value = value;
        }

        @Override
        public List<Object> getArgumentValues(Option option) {
            if (this.option.equals(option)) {
                List<Object> values = new ArrayList<>();
                values.add(value);
                return values;
            }
            return new ArrayList<>();
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.option = option;
            this.defaultValues = defaultValues;
        }

        @Override
        public boolean hasOption(String name) {
            return this.option != null && this.option.getName().equals(name);
        }

        @Override
        public Object getValue(Option option) {
            if (this.option.equals(option)) {
                return value;
            }
            return null;
        }

        @Override
        public List<Object> getDefaultValues(Option option) {
            if (this.option.equals(option)) {
                return defaultValues;
            }
            return new ArrayList<>();
        }

        @Override
        public boolean hasValue(Option option) {
            return this.option.equals(option) && value != null;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (this.option.equals(option)) {
                switchValue = value;
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public boolean hasSwitch(Option option) {
            return this.option.equals(option) && switchValue;
        }

        @Override
        public boolean getSwitch(Option option) {
            return switchValue;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.option = option;
            this.defaultSwitch = defaultSwitch;
        }

        @Override
        public Boolean getDefaultSwitch(Option option) {
            if (this.option.equals(option)) {
                return defaultSwitch;
            }
            return null;
        }

        @Override
        public void addProperty(String property, String value) {
            this.property = property;
            this.propertyValue = value;
        }

        @Override
        public boolean hasProperty(String property) {
            return this.property != null && this.property.equals(property);
        }

        @Override
        public String getProperty(String property) {
            if (this.property.equals(property)) {
                return propertyValue;
            }
            return null;
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }
    }
}