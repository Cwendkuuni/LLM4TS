package org.apache.commons.cli2.commandline;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        commandLine.addValue(option, "value1");
        List valueList = (List) commandLine.getValues(option, new ArrayList());
        assertTrue(valueList.contains("value1"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        assertNull(commandLine.getOption("--test"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("--test"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        assertNull(commandLine.getValues(option, new ArrayList()));
        commandLine.addValue(option, "value1");
        List valueList = (List) commandLine.getValues(option, new ArrayList());
        assertEquals(Collections.singletonList("value1"), valueList);
    }
    
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        assertNull(commandLine.getSwitch(option, false));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1", ""));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        assertEquals("default", commandLine.getProperty("property1", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        assertFalse(commandLine.looksLikeOption("test"));
        assertTrue(commandLine.looksLikeOption("--test"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        commandLine.addValue(new Option("--test", "Test Option"), "value1");
        commandLine.addValue(new Option("--test", "Test Option"), "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        commandLine.addOption(new Option("--test", "Test Option"));
        commandLine.addOption(new Option("--test2", "Test Option 2"));
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(new Option("--test", "Test Option")));
        assertTrue(options.contains(new Option("--test2", "Test Option 2")));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        commandLine.addOption(new Option("--test", "Test Option"));
        commandLine.addOption(new Option("--test2", "Test Option 2"));
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("--test"));
        assertTrue(triggers.contains("--test2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        commandLine.setDefaultValues(option, new ArrayList());
        assertTrue(commandLine.getValues(option, Collections.singletonList("default")).isEmpty());
        commandLine.addValue(option, "value1");
        commandLine.setDefaultValues(option, Collections.singletonList("default"));
        List valueList = (List) commandLine.getValues(option, new ArrayList());
        assertEquals(Collections.singletonList("value1"), valueList);
        commandLine.addValue(option, "value2");
        valueList = (List) commandLine.getValues(option, new ArrayList());
        assertEquals(Arrays.asList("value1", "value2"), valueList);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        Option option = new Option("--test", "Test Option");
        commandLine.setDefaultSwitch(option, false);
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("--test", "Test Option"), new ArrayList());
        List normalised = commandLine.getNormalised();
        assertTrue(normalised.isEmpty());
        commandLine.addValue(new Argument(), "value1");
        normalised = commandLine.getNormalised();
        assertEquals(Collections.singletonList("value1"), normalised);
    }
}