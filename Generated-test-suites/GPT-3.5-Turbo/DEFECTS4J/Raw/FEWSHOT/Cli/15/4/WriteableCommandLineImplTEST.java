package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setup() {
        option = new Option();
        option.setPreferredName("option");
        
        argument = new Argument();
        argument.setPreferredName("argument");
        
        List rootOptions = new ArrayList();
        rootOptions.add(option);
        
        commandLine = new WriteableCommandLineImpl(new Option(), rootOptions);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean bool = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, bool);
    }
    
    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        boolean present = commandLine.hasOption(option);
        assertEquals(true, present);
    }
    
    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option returnedOption = commandLine.getOption("option");
        assertEquals(option, returnedOption);
    }
    
    @Test
    public void testGetValues() {
        List defaults = Collections.singletonList("default");
        commandLine.setDefaultValues(option, defaults);
        
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));

        commandLine.addValue(option, "value");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default", values.get(0));
        assertEquals("value", values.get(1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        Boolean bool = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, bool);
    }
    
    @Test
    public void testGetProperty() {
        String value = commandLine.getProperty("property");
        assertNull(value);
        
        commandLine.addProperty(option, "property", "value");
        value = commandLine.getProperty("property");
        assertEquals("value", value);
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        Set properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("property", properties.toArray()[0]);
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        String value = commandLine.getProperty(option, "property", "default");
        assertEquals("default", value);
        
        commandLine.addProperty(option, "property", "value");
        value = commandLine.getProperty(option, "property", "default");
        assertEquals("value", value);
    }
    
    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties();
        assertEquals(0, properties.size());

        commandLine.addProperty(option, "property", "value");
        properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertEquals("property", properties.toArray()[0]);
    }
    
    @Test
    public void testLooksLikeOption() {
        boolean looksLikeOption = commandLine.looksLikeOption("--option");
        assertEquals(true, looksLikeOption);
        
        looksLikeOption = commandLine.looksLikeOption("-option");
        assertEquals(true, looksLikeOption);
        
        looksLikeOption = commandLine.looksLikeOption("option");
        assertEquals(false, looksLikeOption);
    }
    
    @Test
    public void testToString() {
        commandLine.addValue(argument, "value");
        String string = commandLine.toString();
        assertEquals("value", string);
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertEquals("option", triggers.toArray()[0]);
    }
}