package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");
        
        GnuParser parser = new GnuParser();
        
        // Test when stopAtNonOption is false
        String[] arguments = new String[]{"-a", "-b", "value1", "-c", "value2"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value1", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("value2", flattened[4]);
        
        arguments = new String[]{"--", "-a", "-b", "value1", "-c", "value2"};
        flattened = parser.flatten(options, arguments, false);
        assertEquals("--", flattened[0]);
        assertEquals("-a", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value1", flattened[3]);
        assertEquals("-c", flattened[4]);
        assertEquals("value2", flattened[5]);
        
        // Test when stopAtNonOption is true
        arguments = new String[]{"-a", "-b", "value1", "-c", "value2"};
        flattened = parser.flatten(options, arguments, true);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value1", flattened[2]);
        
        arguments = new String[]{"--", "-a", "-b", "value1", "-c", "value2"};
        flattened = parser.flatten(options, arguments, true);
        assertEquals("--", flattened[0]);
        assertEquals("-a", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value1", flattened[3]);
    }
}