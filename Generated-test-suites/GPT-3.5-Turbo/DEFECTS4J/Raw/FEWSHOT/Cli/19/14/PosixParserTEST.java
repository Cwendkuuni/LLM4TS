
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten_EmptyOptions_StopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "--", "-arg2"};
        
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(2, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_EmptyOptions_DoNotStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "--", "-arg2"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(4, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("--", flattened[2]);
        assertEquals("-arg2", flattened[3]);
    }
    
    @Test
    public void testFlatten_OptionsWithArg_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "--", "-arg2"};
        
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(2, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
    }
    
    @Test
    public void testFlatten_OptionsWithoutArg_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "--", "-arg2"};
        
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(1, flattened.length);
        assertEquals("-f", flattened[0]);
    }
    
    @Test
    public void testFlatten_OptionsWithoutArg_DoNotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "--", "-arg2"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(3, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("--", flattened[2]);
    }
    
    @Test
    public void testFlatten_OptionsWithArgAndMultipleValues_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArgs().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "arg2", "--", "-arg3"};
        
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(3, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("arg2", flattened[2]);
    }
    
    @Test
    public void testFlatten_OptionsWithArgAndMultipleValues_DoNotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArgs().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "arg2", "--", "-arg3"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(5, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("arg2", flattened[2]);
        assertEquals("--", flattened[3]);
        assertEquals("-arg3", flattened[4]);
    }
    
    @Test
    public void testFlatten_OptionsWithArgAndMultipleValuesAndNonOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArgs().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "arg2", "-arg3"};
        
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(3, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("arg2", flattened[2]);
    }
    
    @Test
    public void testFlatten_OptionsWithArgAndMultipleValuesAndNonOption_DoNotStopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArgs().build());
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "arg1", "arg2", "-arg3"};
        
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(4, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("arg2", flattened[2]);
        assertEquals("-arg3", flattened[3]);
    }
}
