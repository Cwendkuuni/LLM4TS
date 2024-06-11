
package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setup() {
        option1 = new Option();
        option1.setPreferredName("option1");

        option2 = new Option();
        option2.setPreferredName("option2");

        List<String> arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option2));
        assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");

        assertTrue(commandLine.hasOption(option1));
        assertEquals("value1", commandLine.getValues(option1, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        assertEquals(option1, commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option3"));
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = Arrays.asList("value3", "value4");

        List<String> values = commandLine.getValues(option1, defaultValues);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        assertTrue(commandLine.getSwitch(option1, false));
        assertNull(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");

        assertEquals("value1", commandLine.getProperty("property1", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();

        assertEquals(1, options.size());
        assertEquals(option1, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertFalse(commandLine.getOptionTriggers().contains("option3"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option1, defaults);

        assertEquals(defaults, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}
