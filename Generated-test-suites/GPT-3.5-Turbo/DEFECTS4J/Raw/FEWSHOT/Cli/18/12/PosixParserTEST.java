package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;
    
    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-f", "file.txt", "arg1", "arg2"};
        options.addOption("f", true, "file option");
        
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }
    
    @Test
    public void testFlatten_NotStopAtNonOption() {
        String[] args = {"-f", "file.txt", "arg1", "arg2"};
        options.addOption("f", true, "file option");
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_SpecialToken() {
        String[] args = {"--file=file.txt"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(2, result.length);
        assertEquals("--file", result[0]);
        assertEquals("file.txt", result[1]);
    }
    
    @Test
    public void testFlatten_OptionExists() {
        String[] args = {"-f", "file.txt"};
        options.addOption("f", true, "file option");
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
    }
    
    @Test
    public void testFlatten_OptionNotExist_StopAtNonOption() {
        String[] args = {"-f", "file.txt"};
        
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }
    
    @Test
    public void testFlatten_OptionNotExist_NotStopAtNonOption() {
        String[] args = {"-f", "file.txt"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        String[] args = {"-abc", "file.txt"};
        options.addOption("a", false, "a option");
        options.addOption("b", false, "b option");
        options.addOption("c", true, "c option");
        
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(2, result.length);
        assertEquals("-abc", result[0]);
        assertEquals("file.txt", result[1]);
    }
    
    @Test
    public void testFlatten_BurstToken_NotStopAtNonOption() {
        String[] args = {"-abc", "file.txt"};
        options.addOption("a", false, "a option");
        options.addOption("b", false, "b option");
        options.addOption("c", true, "c option");
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("file.txt", result[3]);
    }
}