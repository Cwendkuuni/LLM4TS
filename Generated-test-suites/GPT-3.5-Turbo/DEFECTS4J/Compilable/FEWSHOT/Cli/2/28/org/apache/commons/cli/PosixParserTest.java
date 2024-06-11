package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("f", false, "Test option f");
        options.addOption("foo", true, "Test option foo");
        options.addOption("bar", false, "Test option bar");
    }
    
    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        String[] args = {"--", "arg1", "-f", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-f", result[1]);
        assertEquals("arg2", result[2]);
    }
    
    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        String[] args = {"--", "arg1", "-f", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(5, result.length);
        assertEquals("--", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-f", result[2]);
        assertEquals("arg2", result[3]);
        assertEquals("arg3", result[4]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_InvalidOption() {
        String[] args = {"-z"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(1, result.length);
        assertEquals("-z", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOptionNoArg() {
        String[] args = {"-f"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }
    
    @Test
    public void testFlatten_ShortOptionWithArg() {
        String[] args = {"-foo", "value"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(2, result.length);
        assertEquals("-foo", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_LongOptionNoArg() {
        String[] args = {"--bar"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(1, result.length);
        assertEquals("--bar", result[0]);
    }
    
    @Test
    public void testFlatten_LongOptionWithArg() {
        String[] args = {"--foo", "value"};
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("value", result[1]);
    }
}