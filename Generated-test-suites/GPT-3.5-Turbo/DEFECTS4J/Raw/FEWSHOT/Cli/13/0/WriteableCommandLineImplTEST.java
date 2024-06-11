
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
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        option1 = new Option();
        option1.setPreferredName("option1");
        option1.setPrefixes(Arrays.asList("-"));
        option1.setTriggers(Arrays.asList("-o1"));
        
        option2 = new Option();
        option2.setPreferredName("option2");
        option2.setPrefixes(Arrays.asList("-"));
        option2.setTriggers(Arrays.asList("-o2"));
        
        List<String> arguments = new ArrayList<String>();
        arguments.add("arg1");
        arguments.add("arg2");
        
        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        
        List<String> values = commandLine.getValues(option1, null);
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        
        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option foundOption = commandLine.getOption("-o1");
        assertEquals(option1, foundOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        
        List<String> values = commandLine.getValues(option1, null);
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        
        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        
        String propertyValue = commandLine.getProperty("property1", "");
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property1", "value1");
        
        String propertyValue = commandLine.getProperty("property1", "");
        assertEquals("value1", propertyValue);
        
        String defaultValue = commandLine.getProperty("property2", "default");
        assertEquals("default", defaultValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        String commandLineString = commandLine.toString();
        assertEquals("arg1 arg2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option2);
        
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option2);
        
        List<String> triggers = new ArrayList<String>(commandLine.getOptionTriggers());
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
        assertTrue(triggers.contains("-o1"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<String>();
        defaults.add("default1");
        defaults.add("default2");
        
        commandLine.setDefaultValues(option1, defaults);
        
        List<String> values = commandLine.getValues(option1, null);
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        
        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}
