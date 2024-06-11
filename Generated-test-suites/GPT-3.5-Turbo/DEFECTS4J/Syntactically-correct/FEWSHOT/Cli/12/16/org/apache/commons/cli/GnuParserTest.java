package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "a option");
        options.addOption("b", true, "b option");
        options.addOption("c", false, "c option");
        
        String[] arguments1 = new String[] {"-a", "-b", "value", "--", "extra"};
        String[] result1 = GnuParser.flatten(options, arguments1, true);
        assertEquals(4, result1.length);
        assertEquals("-a", result1[0]);
        assertEquals("-b", result1[1]);
        assertEquals("value", result1[2]);
        assertEquals("extra", result1[3]);
        
        String[] arguments2 = new String[] {"--c", "value1", "value2"};
        String[] result2 = GnuParser.flatten(options, arguments2, false);
        assertEquals(3, result2.length);
        assertEquals("--c", result2[0]);
        assertEquals("value1", result2[1]);
        assertEquals("value2", result2[2]);
        
        String[] arguments3 = new String[] {"-d", "value"};
        String[] result3 = GnuParser.flatten(options, arguments3, true);
        assertEquals(2, result3.length);
        assertEquals("-d", result3[0]);
        assertEquals("value", result3[1]);
        
        String[] arguments4 = new String[] {"--", "-e", "value"};
        String[] result4 = GnuParser.flatten(options, arguments4, false);
        assertEquals(3, result4.length);
        assertEquals("--", result4[0]);
        assertEquals("-e", result4[1]);
        assertEquals("value", result4[2]);
        
        Options emptyOptions = new Options();
        String[] arguments5 = new String[] {};
        String[] result5 = GnuParser.flatten(emptyOptions, arguments5, true);
        assertEquals(0, result5.length);
        
        String[] arguments6 = null;
        String[] result6 = GnuParser.flatten(emptyOptions, arguments6, true);
        assertNull(result6);
    }
}