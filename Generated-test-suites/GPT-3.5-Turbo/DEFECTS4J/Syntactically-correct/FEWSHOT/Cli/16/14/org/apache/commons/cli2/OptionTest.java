package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.cli.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {
    
    private Option option;
    
    @Before
    public void setup() {
        option = new OptionImpl();
    }
    
    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = ListIterator.of("-f", "value");
        option.process(commandLine, args);
        
        assertTrue(commandLine.hasOption("f"));
        assertEquals("value", commandLine.getValue("f"));
        assertFalse(args.hasNext());
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);
        
        assertNull(commandLine.getValue("f"));
    }
    
    @Test
    public void testCanProcessWithStringArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(option.canProcess(commandLine, "-f"));
        assertTrue(option.canProcess(commandLine, "--f"));
        assertFalse(option.canProcess(commandLine, "value"));
    }
    
    @Test
    public void testCanProcessWithListIteratorArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = ListIterator.of("-f", "value");
        
        assertTrue(option.canProcess(commandLine, arguments));
        assertFalse(arguments.hasNext());
    }
    
    @Test
    public void testGetTriggers() {
        Set triggers = option.getTriggers();
        
        assertTrue(triggers.contains("f"));
        assertTrue(triggers.contains("f"));
        assertFalse(triggers.contains("value"));
    }
    
    @Test
    public void testGetPrefixes() {
        Set prefixes = option.getPrefixes();
        
        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
    }
    
    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.validate(commandLine);
        
        assertFalse(commandLine.hasErrors());
    }
    
    @Test
    public void testHelpLines() {
        int depth = 0;
        Set helpSettings = null;
        Comparator comp = null;
        
        List helpLines = option.helpLines(depth, helpSettings, comp);
        
        assertTrue(helpLines.isEmpty());
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = null;
        Comparator comp = null;
        
        option.appendUsage(buffer, helpSettings, comp);
        
        assertEquals("", buffer.toString());
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("", option.getPreferredName());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("", option.getDescription());
    }
    
    @Test
    public void testGetId() {
        assertEquals(0, option.getId());
    }
    
    @Test
    public void testFindOption() {
        Option result = option.findOption("f");
        
        assertNull(result);
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }
    
}