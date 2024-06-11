package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertTrue(option.getValue().contains("value"));
    }

    @Test
    public void testGetValues() {
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(option.getValue()).thenReturn(values);

        when(commandLine.getValues(option)).thenCallRealMethod();

        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        assertTrue(option.getValue().equals(defaultValues));
        assertFalse(option.getValue().isEmpty());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);

        try {
            commandLine.addSwitch(option, true);
            assertTrue(option.getValues().get(0).equals("true"));
        } catch (IllegalStateException e) {
            // expected
        }

        when(option.getValues()).thenReturn(new ArrayList<>());
        commandLine.addSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertNull(commandLine.looksLikeOption(null));
    }

    private void assertEquals(List expected, List actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (expected.size() != actual.size()) {
            fail("Size mismatch, expected: " + expected + ", actual: " + actual);
        } else {
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), actual.get(i));
            }
        }
    }
}