package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsInAnyOrder;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(false).build();

        commandLine.addOption(option);

        assertEquals("option", commandLine.getOptions().get(0).getLongOpt());
        assertEquals("o", commandLine.getOptions().get(0).getOpt());
        assertFalse(commandLine.getOptions().get(0).hasArg());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(true).build();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getValuesForOption("option");

        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueDuplicate() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(true).build();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        commandLine.addValue(option, "value2");
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.createWithValues("--option value1 value2");

        List values = commandLine.getValuesForOption("option");

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(true).build();

        List defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValuesForOption("option");

        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(false).build();

        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getOptionValue("option", false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(false).build();

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        Option option = Option.builder("o").longOpt("option").hasArg(false).build();

        commandLine.setDefaultSwitch(option, true);

        assertEquals(true, commandLine.getOptionValue("option", false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        commandLine.addProperty("property", "value1");

        assertEquals("value1", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();

        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("--option="));
        assertTrue(commandLine.looksLikeOption("--option value"));
        assertTrue(commandLine.looksLikeOption("option"));
        assertTrue(commandLine.looksLikeOption("option="));
        assertTrue(commandLine.looksLikeOption("option value"));

        // false cases
        assertFalse(commandLine.looksLikeOption(""));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("-option="));
        assertFalse(commandLine.looksLikeOption("-option value"));
    }
}

class MockWriteableCommandLine implements WriteableCommandLine {
    private Option[] options;
    private List<String> valuesForOption;
    private Map<String, Object> properties;
    private boolean[] switches;

    static WriteableCommandLine create() {
        return new MockWriteableCommandLine();
    }

    static WriteableCommandLine createWithValues(String arg) {
        WriteableCommandLine commandLine = new MockWriteableCommandLine();
        commandLine.parse(new DefaultParser().parse(new StringArrayInput(new String[] { arg })));
        return commandLine;
    }

    @Override
    public void addOption(Option option) {
        if (options == null) {
            options = new Option[16];
        }
        int index = findEmptyIndex();
        options[index] = option;
    }

    @Override
    public void addValue(Option option, Object value) {
        if (valuesForOption == null) {
            valuesForOption = new ArrayList<>();
        }
        valuesForOption.add(value.toString());
    }

    @Override
    public List<Option> getOptions() {
        return Arrays.asList(options);
    }

    @Override
    public List<String> getValuesForOption(String option) {
        return valuesForOption;
    }

    @Override
    public void setDefaultValues(Option option, List values) {
        if (valuesForOption == null) {
            valuesForOption = new ArrayList<>();
        }
        valuesForOption.addAll(values);
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        if (switches == null) {
            switches = new boolean[options.length];
        }
        int index = findOptionIndex(option);
        if (switches[index] != -1) {
            throw new IllegalStateException("Switch value already set for option: " + option.getLongOpt());
        }
        switches[index] = value;
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean value) {
        int index = findOptionIndex(option);
        switches[index] = value != null ? value : false;
    }

    @Override
    public void addProperty(String property, String value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(property, value);
    }

    @Override
    public String getProperty(String property) {
        return properties.getOrDefault(property, "");
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return getOptionByName(argument) != null;
    }

    private Option getOptionByName(String name) {
        for (Option option : options) {
            if (option.getLongOpt().equals(name) || option.getOpt().equals(name)) {
                return option;
            }
        }
        return null;
    }

    private int findOptionIndex(Option option) {
        for (int i = 0; i < options.length; i++) {
            if (options[i] == option) {
                return i;
            }
        }
        return -1;
    }

    private int findEmptyIndex() {
        for (int i = 0; i < options.length; i++) {
            if (options[i] == null) {
                return i;
            }
        }
        return -1;
    }
}