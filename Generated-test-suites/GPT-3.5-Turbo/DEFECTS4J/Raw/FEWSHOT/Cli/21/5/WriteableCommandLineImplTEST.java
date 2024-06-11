package org.apache.commons.cli2.commandline;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Argument();
        assertFalse(commandLine.hasOption(option3));
        commandLine.addOption(option3);
        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Argument();
        Object value1 = "value1";
        assertNull(commandLine.getValues(option1, null));
        commandLine.addValue(option1, value1);
        List<Object> values = commandLine.getValues(option1, null);
        assertEquals(value1, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Argument();
        assertFalse(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Argument();
        assertFalse(commandLine.hasOption(option1));
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Argument();
        assertNull(commandLine.getOption("option1"));
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Argument();
        assertNull(commandLine.getValues(option1, null));
        commandLine.addValue(option1, "value1");
        List<Object> values = commandLine.getValues(option1, null);
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Argument();
        assertNull(commandLine.getUndefaultedValues(option1));
        commandLine.addValue(option1, "value1");
        List<Object> values = commandLine.getUndefaultedValues(option1);
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Argument();
        assertNull(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property1"));
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        assertEquals("defaultValue", commandLine.getProperty("property1", "defaultValue"));
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        Properties properties = commandLine.getProperties();
        assertEquals(0, properties.size());
        commandLine.addProperty("property1", "value1");
        Set<String> keys = commandLine.getProperties().stringPropertyNames();
        assertTrue(keys.contains("property1"));
        assertEquals("value1", commandLine.getProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        Option option1 = new Argument();
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Argument();
        assertFalse(commandLine.getValues(option1, null).isEmpty());
        List<Object> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option1, defaults);
        List<Object> values = commandLine.getValues(option1, null);
        assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Argument();
        assertFalse(commandLine.getSwitch(option1, null));
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        List<Object> normalised = commandLine.getNormalised();
        assertTrue(normalised.isEmpty());
        Option option1 = new Argument();
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
    }
}