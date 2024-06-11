package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("f", false, "Test option");
        options.addOption("foo", true, "Test option with argument");
        options.addOption("bar", false, "Test option");
    }
    
    @Test
    public void testFlatten_withStopAtNonOption() {
        String[] result = parser.flatten(options, new String[] {"-f", "file.txt"}, true);
        
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }
    
    @Test
    public void testFlatten_withoutStopAtNonOption() {
        String[] result = parser.flatten(options, new String[] {"-f", "file.txt"}, false);
        
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
    }
    
    @Test
    public void testFlatten_longOptionWithValue() {
        String[] result = parser.flatten(options, new String[] {"--foo=value"}, false);
        
        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }
    
    @Test
    public void testFlatten_singleHyphen() {
        String[] result = parser.flatten(options, new String[] {"-"}, false);
        
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_shortOption() {
        String[] result = parser.flatten(options, new String[] {"-f"}, false);
        
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }
    
    @Test
    public void testFlatten_shortOptionWithoutArgument() {
        String[] result = parser.flatten(options, new String[] {"-f"}, true);
        
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }
    
    @Test
    public void testFlatten_shortOptionWithArgument() {
        String[] result = parser.flatten(options, new String[] {"--foo", "value"}, false);
        
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_shortOptionNotInOptions() {
        String[] result = parser.flatten(options, new String[] {"-x"}, false);
        
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }
    
    @Test
    public void testFlatten_shortOptionNotInOptionsWithStopAtNonOption() {
        String[] result = parser.flatten(options, new String[] {"-x"}, true);
        
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }
    
    @Test
    public void testFlatten_nonOption() {
        String[] result = parser.flatten(options, new String[] {"file.txt"}, false);
        
        assertEquals(1, result.length);
        assertEquals("file.txt", result[0]);
    }
    
    @Test
    public void testFlatten_nonOptionWithStopAtNonOption() {
        String[] result = parser.flatten(options, new String[] {"file.txt"}, true);
        
        assertEquals(1, result.length);
        assertEquals("file.txt", result[0]);
    }
    
    @Test
    public void testFlatten_multipleOptions() {
        String[] result = parser.flatten(options, new String[] {"-f", "--foo=value", "-bar"}, false);
        
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--foo", result[1]);
        assertEquals("-bar", result[2]);
    }
}