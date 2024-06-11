package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        
        assertNotNull(GnuParser.flatten(options, new String[]{}));
        
        String[] arguments1 = {"-D", "property=value"};
        assertArrayEquals(arguments1, GnuParser.flatten(options, arguments1));
        
        String[] arguments2 = {"--foo=value"};
        assertArrayEquals(arguments2, GnuParser.flatten(options, arguments2));
        
        String[] arguments3 = {"-", "arg1", "arg2"};
        assertArrayEquals(arguments3, GnuParser.flatten(options, arguments3));
        
        String[] arguments4 = {"-f", "file.txt"};
        assertArrayEquals(arguments4, GnuParser.flatten(options, arguments4));
        
        String[] arguments5 = {"--", "-f"};
        assertArrayEquals(arguments5, GnuParser.flatten(options, arguments5));
        
        String[] arguments6 = {"--"};
        assertArrayEquals(arguments6, GnuParser.flatten(options, arguments6));
    }
}