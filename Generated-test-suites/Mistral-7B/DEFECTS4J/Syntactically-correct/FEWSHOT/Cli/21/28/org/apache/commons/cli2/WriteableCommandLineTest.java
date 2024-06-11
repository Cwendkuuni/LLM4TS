package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    // Test addOption and getOptions methods
    @Test
    public void testAddOptionAndGetOptions() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "force", true, "apply force");
        cmd.addOption(option);
        List<Option> options = cmd.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testAddOptionTwice() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "force", true, "apply force");
        cmd.addOption(option);
        try {
            cmd.addOption(option);
        } catch (IllegalStateException e) {
            // expected
        }
        List<Option> options = cmd.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.addOption(option);
        cmd.addValue(option, "input.txt");
        List<Object> values = cmd.getValues(option);
        assertEquals(1, values.size());
        assertEquals("input.txt", values.get(0));
    }

    @Test
    public void testAddValueTwice() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.addOption(option);
        cmd.addValue(option, "input.txt");
        try {
            cmd.addValue(option, "output.txt");
        } catch (IllegalStateException e) {
            // expected
        }
        List<Object> values = cmd.getValues(option);
        assertEquals(1, values.size());
        assertEquals("input.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.addOption(option);
        cmd.addValue(option, "input.txt");
        List<Object> values = cmd.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("input.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.addOption(option);
        List<Object> values = cmd.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        List<String> defaults = Arrays.asList("input.txt", "output.txt");
        cmd.addOption(option);
        cmd.setDefaultValues(option, defaults);
        List<Object> values = cmd.getValues(option);
        assertEquals(2, values.size());
        assertEquals("input.txt", values.get(0));
        assertEquals("output.txt", values.get(1));
    }

    @Test
    public void testSetDefaultValuesEmpty() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.addOption(option);
        cmd.setDefaultValues(option, Collections.emptyList());
        List<Object> values = cmd.getValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("v", "verbose", false, "verbose output");
        cmd.addOption(option);
        try {
            cmd.addSwitch(option, true);
        } catch (IllegalStateException e) {
            // expected
        }
        try {
            cmd.addSwitch(option, false);
            cmd.addSwitch(option, true);
        } catch (IllegalStateException e) {
            // expected
        }
        Boolean defaultSwitch = cmd.getDefaultSwitch(option);
        assertNull(defaultSwitch);
        List<Boolean> switches = cmd.getSwitches(option);
        assertEquals(1, switches.size());
        assertEquals(true, switches.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("v", "verbose", false, "verbose output");
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, true);
        Boolean defaultSwitch = cmd.getDefaultSwitch(option);
        assertEquals(true, defaultSwitch);
        List<Boolean> switches = cmd.getSwitches(option);
        assertEquals(0, switches.size());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("p", "prefix", true, "prefix to use");
        cmd.addOption(option);
        cmd.addProperty(option, "path", "data/");
        String value = cmd.getProperty(option, "path");
        assertEquals("data/", value);
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("p", "prefix", true, "prefix to use");
        cmd.addOption(option);
        cmd.addProperty(option, "prefix", "data/");
        String value = cmd.getProperty(option, "prefix");
        assertEquals("data/", value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        assertTrue(cmd.looksLikeOption("--help"));
        assertTrue(cmd.looksLikeOption("-h"));
        assertTrue(cmd.looksLikeOption("--no-help"));
        assertFalse(cmd.looksLikeOption("input.txt"));
        assertFalse(cmd.looksLikeOption(""));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.setCurrentOption(option);
        assertEquals(option, cmd.getCurrentOption());
        Option otherOption = new Option("v", "verbose", false, "verbose output");
        cmd.setCurrentOption(otherOption);
        assertEquals(otherOption, cmd.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine cmd = new MockWriteableCommandLine();
        Option option = new Option("f", "file", true, "file to process");
        cmd.setCurrentOption(option);
        cmd.setCurrentOption(null);
        assertNull(cmd.getCurrentOption());
    }

    // MockWriteableCommandLine class
    private static class MockWriteableCommandLine implements WriteableCommandLine {
        private Option currentOption = null;

        @Override
        public void addOption(Option option) {
            // empty
        }

        @Override
        public void addValue(Option option, Object value) {
            // empty
        }

        @Override
        public List getUndefaultedValues(Option option) {
            return Collections.emptyList();
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // empty
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (getSwitches(option) != null) {
                throw new IllegalStateException();
            }
            setSwitches(option, Collections.singletonList(value));
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            setSwitches(option, defaultSwitch == null ? null : Collections.singletonList(defaultSwitch));
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            setProperty(option, property, value);
        }

        @Override
        public void addProperty(String property, String value) {
            // empty
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("--") || argument.startsWith("-");
        }

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        @Override
        public void setCurrentOption(Option option) {
            this.currentOption = option;
        }

        @Override
        public List<Option> getOptions() {
            return Collections.emptyList();
        }

        @Override
        public List<Boolean> getSwitches(Option option) {
            return null;
        }

        @Override
        public String getProperty(Option option, String property) {
            return getProperty(option, property, null);
        }

        @Override
        public String getProperty(Option option, String property, String defaultValue) {
            if (option.getValueDescriptions().containsKey(property)) {
                return option.getValueDescriptions().get(property);
            }
            return defaultValue;
        }

        @Override
        public void setSwitches(Option option, List<Boolean> switches) {
            if (option.getValueDescriptions().containsKey("switch")) {
                option.getValueDescriptions().put("switch", switches);
            }
        }

        @Override
        public void setProperty(Option option, String property, String value) {
            if (option.getValueDescriptions().containsKey(property)) {
                option.getValueDescriptions().put(property, value);
            }
        }
    }
}