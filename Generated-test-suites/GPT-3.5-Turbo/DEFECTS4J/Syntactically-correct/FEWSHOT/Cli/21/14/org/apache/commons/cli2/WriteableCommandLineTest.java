
package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test");

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test");
        Object value = "value";

        commandLine.addValue(option, value);

        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);

        assertEquals(1, undefaultedValues.size());
        assertEquals(value, undefaultedValues.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("test");
        Object value1 = "value1";
        Object value2 = "value2";

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);

        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains(value1));
        assertTrue(undefaultedValues.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);

        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains("default1"));
        assertTrue(undefaultedValues.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("test");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_Exception() {
        Option option = new OptionImpl("test");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("test");
        String property = "key";
        String value = "value";

        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddProperty_Default() {
        String property = "key";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options;
        private List<Object> values;
        private List<Object> undefaultedValues;
        private List<Object> defaultValues;
        private List<Option> switches;
        private List<Boolean> switchValues;
        private List<Property> properties;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            undefaultedValues = new ArrayList<>();
            defaultValues = new ArrayList<>();
            switches = new ArrayList<>();
            switchValues = new ArrayList<>();
            properties = new ArrayList<>();
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add(value);
            undefaultedValues.add(value);
        }

        @Override
        public List<Object> getUndefaultedValues(Option option) {
            return undefaultedValues;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues.addAll(defaultValues);
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switches.contains(option)) {
                throw new IllegalStateException("Switch already added");
            }

            switches.add(option);
            switchValues.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            switchValues.add(defaultSwitch);
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(new Property(option, property, value));
        }

        @Override
        public void addProperty(String property, String value) {
            properties.add(new Property(null, property, value));
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        private class OptionImpl implements Option {
            private String name;

            public OptionImpl(String name) {
                this.name = name;
            }

            @Override
            public String getName() {
                return name;
            }
        }

        private class Property {
            private Option option;
            private String key;
            private String value;

            public Property(Option option, String key, String value) {
                this.option = option;
                this.key = key;
                this.value = value;
            }

            public Option getOption() {
                return option;
            }

            public String getKey() {
                return key;
            }

            public String getValue() {
                return value;
            }
        }

        @Override
        public boolean hasOption(final String string) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean getSwitchValue(final Option option) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Object getPropertyValue(final String property) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getPropertyValue(final Option option, final String property) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getProperty(final Option option, final String property) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getProperty(final String property) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<Object> getValues(final Option option) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<Option> getOptions() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
