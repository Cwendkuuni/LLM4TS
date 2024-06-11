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
    }
    
    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"-f", "file.txt", "arg1", "arg2"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-f", "file.txt", "arg1", "arg2"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithoutStopAtNonOption() {
        String[] args = {"-f", "file.txt", "arg1", "arg2"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-f", "file.txt", "arg1", "arg2"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOptionArgValue() {
        String[] args = {"-f", "file.txt"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-f", "file.txt"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOptionArgValues() {
        String[] args = {"-f", "file1.txt", "file2.txt"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-f", "file1.txt", "file2.txt"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOptionArgs() {
        String[] args = {"-f", "file.txt", "arg1", "arg2"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-f", "file.txt", "arg1", "arg2"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithMultipleOptions() {
        String[] args = {"-f", "file.txt", "-o", "output.txt"};
        options.addOption("f", true, "Specify a file");
        options.addOption("o", true, "Specify an output file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-f", "file.txt", "-o", "output.txt"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithBursting() {
        String[] args = {"-abcf", "file.txt"};
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "-c", "-f", "file.txt"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithBurstingAndNonOptionArgs() {
        String[] args = {"-abcf", "file.txt", "arg1", "arg2"};
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "-c", "-f", "file.txt", "arg1", "arg2"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOptionIdNotExisting() {
        String[] args = {"-x", "value"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-x", "value"};
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithOptionIdNotExistingAndStopAtNonOption() {
        String[] args = {"-x", "value"};
        options.addOption("f", true, "Specify a file");
        
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-x", "value"};
        
        assertEquals(expected, result);
    }
    
}