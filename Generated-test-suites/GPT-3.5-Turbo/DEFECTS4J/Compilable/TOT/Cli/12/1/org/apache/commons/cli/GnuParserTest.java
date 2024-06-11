package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {
    
    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("f", false, "Test option f");
        
        // Typical use case
        String[] arguments1 = new String[] {"-f"};
        assertArrayEquals(new String[] {"-f"}, parser.flatten(options, arguments1, false));
        
        // Edge case: empty arguments
        String[] arguments2 = new String[0];
        assertArrayEquals(new String[0], parser.flatten(options, arguments2, false));
        
        // Edge case: arguments with "--"
        String[] arguments3 = new String[] {"--", "-f"};
        assertArrayEquals(new String[] {"--", "-f"}, parser.flatten(options, arguments3, false));
        
        // Error scenario: non-existing option
        String[] arguments4 = new String[] {"-x"};
        assertArrayEquals(new String[] {"-x"}, parser.flatten(options, arguments4, false));
        
        // Error scenario: null arguments
        assertThrows(NullPointerException.class, () -> parser.flatten(options, null, false));
    }
}