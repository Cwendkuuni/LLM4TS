package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptionParser().getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().equals("true"));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().equals("true"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getOption(option.getLongOpt()).getPropertyValue("property"));
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        assertFalse(commandLine.looksLikeOption("argument"));
    }
}