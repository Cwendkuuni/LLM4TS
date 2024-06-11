
package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Option option3;
    private Argument argument1;
    private Argument argument2;
    
    @Before
    public void setUp() {
       option1 = new Option("option1");
       option2 = new Option("option2");
       option3 = new Option("option3");
       argument1 = new Argument();
       argument2 = new Argument();
       
       List<Option> options = new ArrayList<>();
       options.add(option1);
       options.add(option2);
       options.add(option3);
       
       List<Argument> arguments = new ArrayList<>();
       arguments.add(argument1);
       arguments.add(argument2);
       
       commandLine = new WriteableCommandLineImpl(option1, arguments);
       commandLine.addOption(option2);
       commandLine.addOption(option3);
    }
    
    @Test
    public void testAddOption() {
        assertTrue(commandLine.getOptions().contains(option2));
        assertTrue(commandLine.getOptions().contains(option3));
    }
    
    @Test
    public void testGetOption() {
        assertEquals(option2, commandLine.getOption("option2"));
        assertEquals(option3, commandLine.getOption("option3"));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");
        
        List<String> expectedValues1 = new ArrayList<>();
        expectedValues1.add("value1");
        
        List<String> expectedValues2 = new ArrayList<>();
        expectedValues2.add("value2");
        
        assertEquals(expectedValues1, commandLine.getValues(argument1, null));
        assertEquals(expectedValues2, commandLine.getValues(argument2, null));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);
        
        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
    }
    
    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertTrue(commandLine.hasOption(option3));
    }
    
    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property1"));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        
        Set<String> expectedProperties = Collections.singleton("property1");
        
        assertEquals(expectedProperties, commandLine.getProperties(option1));
        assertEquals(expectedProperties, commandLine.getProperties());
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        assertEquals("default", commandLine.getProperty(option1, "property1", "default"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");
        
        String expectedToString = "value1 value2";
        
        assertEquals(expectedToString, commandLine.toString());
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        
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
        List<String> normalised = new ArrayList<>();
        normalised.add("value");
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, normalised);
        
        assertEquals(normalised, commandLine.getNormalised());
    }
}
