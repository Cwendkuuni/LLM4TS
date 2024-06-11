package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals(0, commandLine.getOptions().size());
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        assertNull(commandLine.getValue(option));
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertEquals(0, commandLine.getUndefaultedValues(option).size());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test
    public void testSetDefaultValues() {
        assertNull(commandLine.getDefaultValues(option));
        List defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        assertNull(commandLine.getSwitch(option));
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        assertNull(commandLine.getDefaultSwitch(option));
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty(option, "property"));
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        assertNull(commandLine.getProperty("property"));
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertEquals(true, commandLine.looksLikeOption("-option"));
        assertEquals(false, commandLine.looksLikeOption("value"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options;
    private Option currentOption;
    private Object value;
    private List defaultValues;
    private Boolean defaultSwitch;
    private String property;
    private String defaultValue;

    public WriteableCommandLineImpl() {
        options = new ArrayList<>();
    }

    @Override
    public void addOption(final Option option) {
        options.add(option);
    }

    @Override
    public void addValue(final Option option, final Object value) {
        this.value = value;
    }

    @Override
    public List getUndefaultedValues(final Option option) {
        if (value == null) {
            return new ArrayList<>();
        } else {
            List values = new ArrayList<>();
            values.add(value);
            return values;
        }
    }

    @Override
    public void setDefaultValues(final Option option, final List defaultValues) {
        this.defaultValues = defaultValues;
    }

    @Override
    public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
        if (defaultSwitch != null) {
            throw new IllegalStateException();
        } else {
            defaultSwitch = value;
        }
    }

    @Override
    public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
        this.defaultSwitch = defaultSwitch;
    }

    @Override
    public void addProperty(final Option option, final String property, final String value) {
        this.property = property;
        this.defaultValue = value;
    }

    @Override
    public void addProperty(final String property, final String value) {
        this.property = property;
        this.defaultValue = value;
    }

    @Override
    public boolean looksLikeOption(final String argument) {
        return argument.startsWith("-");
    }

    @Override
    public Option getCurrentOption() {
        return currentOption;
    }

    @Override
    public void setCurrentOption(final Option currentOption) {
        this.currentOption = currentOption;
    }
}