package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private Option option1 = new OptionImpl("option1", true, "description");
    private Option option2 = new OptionImpl("option2", true, "description");
    private Option option3 = new OptionImpl("option3", true, "description");

    private WriteableCommandLine commandLine;

    public WriteableCommandLineImplTest() {
        List<String> arguments = new ArrayList<>();

        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option2));

        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        assertNull(commandLine.getValues(option2, null));

        commandLine.addValue(option2, "value");

        assertEquals(Arrays.asList("value"), commandLine.getValues(option2, null));

        commandLine.addValue(option2, "value2");

        assertEquals(Arrays.asList("value", "value2"), commandLine.getValues(option2, null));
    }

    @Test
    public void testAddSwitch() {
        assertFalse(commandLine.getSwitch(option2, null));

        commandLine.addSwitch(option2, true);

        assertTrue(commandLine.getSwitch(option2, null));

        commandLine.addSwitch(option2, false);

        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option2));

        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("option2"));

        commandLine.addOption(option2);

        assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        assertEquals(Arrays.asList(), commandLine.getValues(option2, null));

        commandLine.addValue(option2, "value");

        assertEquals(Arrays.asList("value"), commandLine.getValues(option2, null));

        commandLine.addValue(option2, "value2");

        assertEquals(Arrays.asList("value", "value2"), commandLine.getValues(option2, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertEquals(Arrays.asList(), commandLine.getUndefaultedValues(option2));

        commandLine.addValue(option2, "value");

        assertEquals(Arrays.asList("value"), commandLine.getUndefaultedValues(option2));

        commandLine.addValue(option2, "value2");

        assertEquals(Arrays.asList("value", "value2"), commandLine.getUndefaultedValues(option2));
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option2, null));

        commandLine.addSwitch(option2, true);

        assertTrue(commandLine.getSwitch(option2, null));

        commandLine.addSwitch(option2, false);

        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty("property"));

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property"));

        commandLine.addProperty(option2, "property", "value2");

        assertEquals("value2", commandLine.getProperty(option2, "property", null));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        assertEquals("defaultValue", commandLine.getProperty("property", "defaultValue"));

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties();

        assertEquals(0, properties.size());

        commandLine.addProperty("property", "value");

        properties = commandLine.getProperties();
        
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("123"));

        commandLine.addOption(option3);

        assertTrue(commandLine.looksLikeOption("option3"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());

        commandLine.addValue(option1, "value");

        assertEquals("value", commandLine.toString());

        commandLine.addValue(option2, "value2");

        assertEquals("value value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();

        assertEquals(1, options.size());
        assertTrue(options.contains(option1));

        commandLine.addOption(option2);

        options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();

        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("option1"));

        commandLine.addOption(option2);

        triggers = commandLine.getOptionTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        assertNull(commandLine.getValues(option2, null));

        commandLine.setDefaultValues(option2, Arrays.asList("default1", "default2"));

        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option2, null));

        commandLine.addValue(option2, "value");

        assertEquals(Arrays.asList("value"), commandLine.getValues(option2, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        assertNull(commandLine.getSwitch(option2, null));

        commandLine.setDefaultSwitch(option2, true);

        assertTrue(commandLine.getSwitch(option2, null));

        commandLine.addSwitch(option2, false);

        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList(), commandLine.getNormalised());

        commandLine.addValue(option1, "value");

        assertEquals(Arrays.asList("value"), commandLine.getNormalised());

        commandLine.addValue(option2, "value2");

        assertEquals(Arrays.asList("value", "value2"), commandLine.getNormalised());
    }
}