package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        WriteableCommandLine commandLine = null;
        assertFalse(option.canProcess(commandLine, null));
        
        commandLine = new WriteableCommandLineImpl();
        assertFalse(option.canProcess(commandLine, null));
        
        commandLine = new WriteableCommandLineImpl();
        commandLine.addArgument("test");
        assertFalse(option.canProcess(commandLine, null));
    }
    
    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        assertEquals("", option.toString());
    }
    
    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        OptionImpl option3 = new OptionImpl(2, false) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, "test");
    }
    
    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        assertEquals(1 * 37, option.hashCode());

        option.setPreferredName("option");
        assertEquals((1 * 37 + "option".hashCode()) * 37, option.hashCode());

        option.setDescription("description");
        assertEquals(((1 * 37 + "option".hashCode()) * 37 + "description".hashCode()) * 37, option.hashCode());

        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");
        option.setPrefixes(prefixes);
        assertEquals((((1 * 37 + "option".hashCode()) * 37 + "description".hashCode()) * 37) + prefixes.hashCode(), option.hashCode());

        Set<String> triggers = new HashSet<>();
        triggers.add("-t");
        triggers.add("--trigger");
        option.setTriggers(triggers);
        assertEquals((((((1 * 37 + "option".hashCode()) * 37 + "description".hashCode()) * 37) + prefixes.hashCode()) * 37) + triggers.hashCode(), option.hashCode());
    }
    
    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        assertNull(option.findOption(null));
        
        option.addTrigger("t");
        assertEquals(option, option.findOption("t"));
        assertNull(option.findOption("test"));
    }
    
    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        assertTrue(option.isRequired());
        
        option = new OptionImpl(2, false) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
        
        commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);
        
        assertTrue(commandLine.getValues(option).isEmpty());
    }
    
    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting level, ResourceHelper helper) {
                
            }
        };
        
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");
        option.setPrefixes(prefixes);
        
        option.checkPrefixes(prefixes);
        option.checkPrefixes(new HashSet<>());
        
        Exception exception = null;
        try {
            Set<String> invalidPrefixes = new HashSet<>();
            invalidPrefixes.add("/");
            option.checkPrefixes(invalidPrefixes);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertTrue(exception.getMessage().contains("option trigger"));
    }
}