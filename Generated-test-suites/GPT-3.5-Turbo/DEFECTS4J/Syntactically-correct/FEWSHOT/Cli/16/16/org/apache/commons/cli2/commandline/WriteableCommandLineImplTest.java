
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new Option();
        option.setPreferredName("opt");
        option.setTrigger("--opt");
        option.setDescription("Test Option");
        
        argument = new Argument();
        argument.setPreferredName("arg");
        argument.setPrefix("-");
        argument.setDescription("Test Argument");
        
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("opt"));
        assertEquals(option, commandLine.getOption("--opt"));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value");
        
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(Arrays.asList("value"), values);
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("opt"));
    }
    
    @Test
    public void testGetValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(argument, defaultValues);
        
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(defaultValues, values);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        List<String> values = commandLine.getUndefaultedValues(argument);
        assertEquals(new ArrayList<>(), values);
    }
    
    @Test
    public void testGetSwitch() {
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        
        Boolean value = commandLine.getSwitch(option, false);
        assertEquals(defaultValue, value);
    }
    
    @Test
    public void testGetProperty() {
        String defaultValue = "default";
        String value = commandLine.getProperty(option, "property", defaultValue);
        assertEquals(defaultValue, value);
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testGetPropertyDefault() {
        String defaultValue = "default";
        String value = commandLine.getProperty("property");
        assertEquals(defaultValue, value);
    }
    
    @Test
    public void testLookLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        commandLine.addOption(option);
        commandLine.addValue(argument, "value");
        
        String expected = "--opt -arg value";
        String actual = commandLine.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        
        List<Option> options = commandLine.getOptions();
        assertEquals(Arrays.asList(option), options);
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        
        List<String> triggers = commandLine.getOptionTriggers();
        assertEquals(Arrays.asList("opt", "--opt"), triggers);
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(argument, defaultValues);
        
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(defaultValues, values);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        
        Boolean value = commandLine.getSwitch(option, false);
        assertEquals(defaultValue, value);
    }
    
    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(new ArrayList<>(), normalised);
    }
}
