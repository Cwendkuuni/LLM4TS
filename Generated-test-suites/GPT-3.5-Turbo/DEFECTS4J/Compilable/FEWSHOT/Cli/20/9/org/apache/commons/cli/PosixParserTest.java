package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten_noOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("arg1", flattened[0]);
        assertEquals("arg2", flattened[1]);
    }
    
    @Test
    public void testFlatten_singleCharOption() {
        Options options = new Options();
        Option option = new Option("a", "description");
        options.addOption(option);
        String[] arguments = {"-a", "arg1"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_doubleCharOption() {
        Options options = new Options();
        Option option = new Option("ab", "description");
        options.addOption(option);
        String[] arguments = {"-ab", "arg1"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("-ab", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_invalidOption() {
        Options options = new Options();
        String[] arguments = {"-c", "arg1"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("-c", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", "description");
        options.addOption(option);
        String[] arguments = {"-a", "arg1"};
        boolean stopAtNonOption = true;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_longOption() {
        Options options = new Options();
        Option option = new Option("f", "foo", false, "description");
        options.addOption(option);
        String[] arguments = {"--foo", "arg1"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("--foo", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_longOptionWithValue() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "description");
        options.addOption(option);
        String[] arguments = {"--foo=bar"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, flattened.length);
        assertEquals("--foo", flattened[0]);
        assertEquals("bar", flattened[1]);
    }
    
    @Test
    public void testFlatten_doubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--", "-a", "arg1"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(3, flattened.length);
        assertEquals("--", flattened[0]);
        assertEquals("-a", flattened[1]);
        assertEquals("arg1", flattened[2]);
    }
    
    @Test
    public void testFlatten_stopAtNonOptionWithRemainingTokens() {
        Options options = new Options();
        Option option = new Option("a", "description");
        options.addOption(option);
        String[] arguments = {"-a", "arg1", "arg2"};
        boolean stopAtNonOption = true;
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("arg2", flattened[2]);
    }

}