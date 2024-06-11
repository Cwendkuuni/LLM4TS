package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    
    @Before
    public void setUp() {
        options = mock(Options.class);
    }
    
    @Test
    public void testFlatten() {
        String[] arguments = new String[] {"--foo", "bar", "-a", "value1", "-b", "value2", "-c", "-d", "--", "extraArgument"};
        boolean stopAtNonOption = true;
        String[] expected = new String[] {"--foo", "bar", "-a", "value1", "-b", "value2", "-c", "-d"};
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    
    @Test
    public void testFlattenStopAtNonOptionFalse() {
        String[] arguments = new String[] {"--foo", "bar", "-a", "value1", "-b", "value2", "-c", "-d", "--", "extraArgument"};
        boolean stopAtNonOption = false;
        String[] expected = new String[] {"--foo", "bar", "-a", "value1", "-b", "value2", "-c", "-d", "--", "extraArgument"};
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    
    @Test
    public void testFlattenNoOptions() {
        String[] arguments = new String[] {"--foo", "bar"};
        boolean stopAtNonOption = true;
        String[] expected = new String[] {"--foo", "bar"};
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    
    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = new String[] {};
        boolean stopAtNonOption = true;
        String[] expected = new String[] {};
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(expected.length, result.length);
    }
    
    @Test
    public void testFlattenNullArguments() {
        String[] arguments = null;
        boolean stopAtNonOption = true;
        String[] expected = new String[] {};
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(expected.length, result.length);
    }
}