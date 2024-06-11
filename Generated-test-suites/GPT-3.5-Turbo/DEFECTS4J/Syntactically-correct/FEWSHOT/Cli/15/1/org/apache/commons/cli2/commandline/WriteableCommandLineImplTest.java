package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private Option option1;
    private Option option2;
    private Option option3;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option1 = new Argument();
        option2 = new Argument();
        option3 = new Argument();
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option2));
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        assertNull(commandLine.getValues(option1, null));
        commandLine.addValue(option1, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, null));
    }

    @Test
    public void testAddSwitch() {
        assertFalse(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option2));
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("trigger1"));
        commandLine.addOption(option2);
        assertEquals(option2, commandLine.getOption("trigger2"));
    }

    @Test
    public void testGetValues() {
        assertNull(commandLine.getValues(option1, null));
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        assertEquals(defaultValues, commandLine.getValues(option1, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertNull(commandLine.getUndefaultedValues(option1));
        commandLine.addValue(option1, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getUndefaultedValues(option1));
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property1"));
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
    }

    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty("property1"));
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
    }

    @Test
    public void testGetProperties() {
        assertFalse(commandLine.getProperties().contains("property1"));
        commandLine.addProperty(option1, "property1", "value1");
        assertTrue(commandLine.getProperties().contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("trigger1"));
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>() {{
            add("--trigger1");
        }});
        assertTrue(commandLine.looksLikeOption("--trigger1"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>() {{
            add("arg1");
            add("arg2");
        }});
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
        commandLine.addOption(option1);
        assertFalse(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testGetOptionTriggers() {
        assertFalse(commandLine.getOptionTriggers().contains("trigger1"));
        commandLine.addOption(option1);
        assertTrue(commandLine.getOptionTriggers().contains("trigger1"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        commandLine.setDefaultValues(option1, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>() {{
            add("arg1");
            add("arg2");
        }});
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
}