package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    
    private PosixParser parser;
    
    @Before
    public void setup() {
        parser = new PosixParser();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] args = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] expected = {"-a", "-b", "value2", "nonOption"};
        
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] args = {"-a", "value1", "-b", "value2", "nonOption"};
        String[] expected = {"-a", "value1", "-b", "value2", "nonOption"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SpecialToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] args = {"--", "-a"};
        String[] expected = {"--", "-a"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] args = {"-"};
        String[] expected = {"-"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionExists() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] args = {"-a"};
        String[] expected = {"-a"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionExists_ArgValues() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        
        String[] args = {"-a", "value1"};
        String[] expected = {"-a", "value1"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionDoesNotExist_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] args = {"-b"};
        String[] expected = {"-b"};
        
        String[] result = parser.flatten(options, args, true);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionDoesNotExist_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] args = {"-b"};
        String[] expected = {"-b"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] args = {"-ab"};
        String[] expected = {"-a", "-b"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_ArgValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        
        String[] args = {"-abvalue"};
        String[] expected = {"-a", "value", "-b"};
        
        String[] result = parser.flatten(options, args, false);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NullArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        
        String[] args = null;
        
        String[] result = parser.flatten(options, args, false);
        
        assertNull(result);
    }
}