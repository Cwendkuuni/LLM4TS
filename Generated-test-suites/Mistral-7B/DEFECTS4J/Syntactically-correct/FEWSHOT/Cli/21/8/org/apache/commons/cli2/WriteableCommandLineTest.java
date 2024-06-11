package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setup() {
        writeableCommandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        verify(writeableCommandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        writeableCommandLine.addValue(option, "value1");
        verify(option).setValue(Arrays.asList("value1"));

        writeableCommandLine.addValue(option, "value2");
        verify(option).setValue(Arrays.asList("value1", "value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = Arrays.asList("value1", "value2");
        when(option.getValue()).thenReturn(values);

        List result = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = Arrays.asList("defaultValue1", "defaultValue2");

        writeableCommandLine.setDefaultValues(option, defaults);
        verify(option).setValue(defaults);
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option, true);
        verify(option).setValues(Collections.singletonList(Boolean.TRUE));

        assertThrows(IllegalStateException.class, () -> writeableCommandLine.addSwitch(option, true));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, Boolean.TRUE);
        verify(option).setValues(Collections.singletonList(Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty(option, "property", "value");
        verify(option).addPropertyValue("property", "value");

        writeableCommandLine.addProperty("property", "value");
        verify(writeableCommandLine).addProperty(option, "property", "value");
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(writeableCommandLine.looksLikeOption("arg"));

        writeableCommandLine = mock(WriteableCommandLine.class, new WriteableCommandLineAnswer());
        assertTrue(writeableCommandLine.looksLikeOption("--arg"));
        assertTrue(writeableCommandLine.looksLikeOption("-a"));
    }

    @Test
    public void testCurrentOption() {
        Option currentOption = mock(Option.class);
        when(writeableCommandLine.getCurrentOption()).thenReturn(currentOption);

        writeableCommandLine.setCurrentOption(option);
        assertSame(option, currentOption);
    }
}

class WriteableCommandLineAnswer implements WriteableCommandLine {
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
        // empty
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        // empty
    }

    @Override
    public void addProperty(Option option, String property, String value) {
        // empty
    }

    @Override
    public void addProperty(String property, String value) {
        // empty
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return true;
    }

    @Override
    public Option getCurrentOption() {
        return null;
    }

    @Override
    public void setCurrentOption(Option currentOption) {
        // empty
    }
}