package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser = new PosixParser();
    private Options options = new Options();
    
    @Test
    public void testFlatten() {
        String[] arguments = { "-a", "arg1", "--option", "arg2", "-c", "arg3" };
        
        String[] result = parser.flatten(options, arguments, false);
        
        String[] expected = { "-a", "arg1", "--option", "arg2", "-c", "arg3" };
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = { "-a", "arg1", "--option", "arg2", "-c", "arg3" };
        
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = { "-a", "arg1", "--option", "arg2", "-c", "arg3" };
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        
        String[] result = parser.flatten(options, arguments, false);
        
        String[] expected = {};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenEmptyOptions() {
        String[] arguments = { "-a", "arg1", "--option", "arg2", "-c", "arg3" };
        
        String[] result = parser.flatten(new Options(), arguments, false);
        
        String[] expected = {};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenStopAtNonOptionNoOption() {
        String[] arguments = { "arg1", "arg2", "arg3" };
        
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = { "arg1", "arg2", "arg3" };
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenBurstToken() {
        String[] arguments = { "-abc", "arg1" };
        
        String[] result = parser.flatten(options, arguments, false);
        
        String[] expected = { "-a", "-b", "-c", "arg1" };
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        String[] arguments = { "-abc", "arg1" };
        
        String[] result = parser.flatten(options, arguments, true);
        
        String[] expected = { "-a", "-b", "-c", "arg1" };
        assertArrayEquals(expected, result);
    }
}
