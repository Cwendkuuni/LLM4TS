package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setUp() {
        Option rootOption = new Option("test", "Test options", true, "test");
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        optionWithValue = new Option("v", "value", 1, "val");
        commandLine.addOption(optionWithValue);

        optionWithSwitch = new Option("s", "switch", 0, null);
        commandLine.addOption(optionWithSwitch);

        optionWithProperty = new Option("p", "property", 1, null)
                .requiredArg()
                .setArgName("value");
        commandLine.addOption(optionWithProperty);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(optionWithValue, "value1");
        List values = commandLine.getValues(optionWithValue, null);
        assertEquals("value1", values.get(0));

        commandLine.addValue(optionWithValue, "value2");
        values = commandLine.getValues(optionWithValue, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        commandLine.addValue(optionWithValue, "value3");
        values = commandLine.getValues(optionWithValue, null);
        assertEquals(3, values.size());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(optionWithSwitch, true);
        assertTrue(commandLine.getSwitch(optionWithSwitch, false));

        commandLine.addSwitch(optionWithSwitch, false);
        assertFalse(commandLine.getSwitch(optionWithSwitch, true));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(optionWithProperty, "property1", "value1");
        Map properties = commandLine.getProperties(optionWithProperty);
        assertEquals(1, properties.size());
        assertEquals("property1", properties.keySet().iterator().next());
        assertEquals("value1", properties.get("property1"));

        commandLine.addProperty(optionWithProperty, "property2", "value2");
        properties = commandLine.getProperties(optionWithProperty);
        assertEquals(2, properties.size());
        assertEquals("property1", properties.keySet().iterator().next());
        assertEquals("value1", properties.get("property1"));
        assertEquals("property2", properties.keySet().iterator().next());
        assertEquals("value2", properties.get("property2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(optionWithSwitch, true);
        commandLine.addSwitch(optionWithSwitch, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueToMissingOption() {
        commandLine.addValue(new Option("missing-option", "Missing option", 0, null), "value");
    }
}