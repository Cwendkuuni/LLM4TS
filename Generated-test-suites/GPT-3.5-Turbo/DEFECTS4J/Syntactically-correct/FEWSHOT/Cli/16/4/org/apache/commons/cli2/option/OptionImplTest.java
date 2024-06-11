package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;
    
    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {};
    }
    
    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        assertFalse(option.canProcess(commandLine, null));
        
        ListIterator arguments = null;
        assertFalse(option.canProcess(commandLine, arguments));
        
        arguments = new ArrayList<String>().listIterator();
        assertFalse(option.canProcess(commandLine, arguments));
        
        arguments.add("");
        assertFalse(option.canProcess(commandLine, arguments));
        
        arguments.add("arg");
        assertTrue(option.canProcess(commandLine, arguments));
    }
    
    @Test
    public void testToString() {
        assertNotNull(option.toString());
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }
    
    @Test
    public void testEquals() {
        Object obj = null;
        assertFalse(option.equals(obj));
        
        obj = new OptionImpl(1, true) {};
        assertTrue(option.equals(obj));
        
        obj = new OptionImpl(2, true) {};
        assertFalse(option.equals(obj));
    }
    
    @Test
    public void testHashCode() {
        assertNotNull(option.hashCode());
    }
    
    @Test
    public void testFindOption() {
        assertNull(option.findOption(""));
        assertNull(option.findOption("arg"));
    }
    
    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }
    
    @Test
    public void testCheckPrefixes() {
        Set prefixes = null;
        option.checkPrefixes(prefixes);
        
        prefixes = new HashSet<String>();
        option.checkPrefixes(prefixes);
        
        prefixes.add("-");
        option.checkPrefixes(prefixes);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefix() {
        Set prefixes = new HashSet<String>();
        prefixes.add("-");
        
        String trigger = "";
        option.checkPrefix(prefixes, trigger);
        
        trigger = "-";
        option.checkPrefix(prefixes, trigger);
        
        trigger = "--";
        option.checkPrefix(prefixes, trigger);
        
        trigger = "arg";
        option.checkPrefix(prefixes, trigger);
    }
}