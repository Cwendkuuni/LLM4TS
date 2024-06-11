package org.apache.commons.cli2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("test");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        @Override
        public void addOption(final Option option) {
            // implementation
        }

        @Override
        public void addValue(final Option option, final Object value) {
            // implementation
        }

        @Override
        public List<String> getUndefaultedValues(final Option option) {
            // implementation
            return null;
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            // implementation
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            // implementation
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            // implementation
        }

        @Override
        public void addProperty(final Option option, final String property, final String value) {
            // implementation
        }

        @Override
        public void addProperty(final String property, final String value) {
            // implementation
        }

        @Override
        public boolean looksLikeOption(final String argument) {
            // implementation
            return false;
        }

        @Override
        public Option getCurrentOption() {
            // implementation
            return null;
        }

        @Override
        public void setCurrentOption(final Option currentOption) {
            // implementation
        }
    }
}