package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {
    
    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", false, "Define property");
        
        String[] arguments = {"-D", "foo=bar", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(5, result.length);
        assertEquals("-D", result[0]);
        assertEquals("foo=bar", result[1]);
        assertEquals("--", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }
    
    @Test
    public void testFlattenWithStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", false, "Define property");
        
        String[] arguments = {"-D", "foo=bar", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("foo=bar", result[1]);
    }
    
    @Test
    public void testFlattenWithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }
}