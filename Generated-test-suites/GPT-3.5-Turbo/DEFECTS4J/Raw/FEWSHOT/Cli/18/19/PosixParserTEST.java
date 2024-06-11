
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-f", "file.txt", "command"};
        options.addOption("f", "file", true, "Specifies the input file");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("command", result[0]);
    }
    
    @Test
    public void testFlatten_WithOptionValue() {
        String[] arguments = {"-f", "file.txt", "param1", "param2"};
        options.addOption("f", "file", true, "Specifies the input file");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("param2", result[2]);
    }
    
    @Test
    public void testFlatten_WithOptionWithoutValue() {
        String[] arguments = {"-t", "value", "param"};
        options.addOption("t", "test", false, "Specifies a test option");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-t", result[0]);
        assertEquals("value", result[1]);
        assertEquals("param", result[2]);
    }
    
    @Test
    public void testFlatten_WithOptionWithoutValue_StopAtNonOption() {
        String[] arguments = {"-t", "value", "param"};
        options.addOption("t", "test", false, "Specifies a test option");
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("param", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_WithSpecialTokenEquality() {
        String[] arguments = {"--file=input.txt", "--option1", "param1", "--option2=value"};
        options.addOption("f", "file", true, "Specifies the input file");
        options.addOption("o", "option1", false, "Specifies option 1");
        options.addOption("op", "option2", true, "Specifies option 2");
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNotNull(result);
        assertEquals(6, result.length);
        assertEquals("--file", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("--option1", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value", result[4]);
        assertEquals("param1", result[5]);
    }
    
    @Test
    public void testFlatten_WithSingleHyphen() {
        String[] arguments = {"-", "param"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("param", result[1]);
    }
    
    @Test
    public void testFlatten_NullOption() {
        String[] arguments = {"-t", "value", "param"};
        
        String[] result = parser.flatten(null, arguments, false);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-t", result[0]);
        assertEquals("value", result[1]);
        assertEquals("param", result[2]);
    }
    
    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_NullArguments() {
        String[] arguments = null;
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_EmptyOptions() {
        String[] arguments = {"-t", "value", "param"};
        
        String[] result = parser.flatten(new Options(), arguments, false);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-t", result[0]);
        assertEquals("value", result[1]);
        assertEquals("param", result[2]);
    }
    
    @Test
    public void testFlatten_InvalidOptions() {
        String[] arguments = {"-t", "value", "param"};
        
        String[] result = parser.flatten(null, arguments, false);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-t", result[0]);
        assertEquals("value", result[1]);
        assertEquals("param", result[2]);
    }
}
