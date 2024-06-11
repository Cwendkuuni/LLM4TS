package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        
        // Add options
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
    }
    
    @Test
    public void testFlatten_NoArguments() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleOptionNoArgument() {
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleOptionWithArgument() {
        String[] arguments = {"-b", "value"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-b", "value"};
        assertArrayEquals(expected, result);   
    }
    
    @Test
    public void testFlatten_MultipleOptionsWithArguments() {
        String[] arguments = {"-b", "value1", "-a", "-c", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-b", "value1", "-a", "-c", "value2"};
        assertArrayEquals(expected, result);   
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-b", "value1", "nonoption", "-c", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-b", "value1", "nonoption"};
        assertArrayEquals(expected, result);   
    }
    
    @Test
    public void testFlatten_IgnoreNonOption() {
        String[] arguments = {"-b", "value1", "nonoption", "-c", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-b", "value1", "nonoption", "-c", "value2"};
        assertArrayEquals(expected, result);   
    }
}