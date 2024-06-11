package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        // Test with valid options
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        options.addOption(Option.builder("abc").hasArg().build());
        options.addOption(Option.builder("d").build());
        options.addOption(Option.builder("e").build());

        String[] args = {"-f", "value", "-abc", "123", "-d", "-e"};
        String[] result = parser.flatten(options, args, true);

        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-abc", result[2]);
        assertEquals("123", result[3]);
        assertEquals("-d", result[4]);
        assertEquals("-e", result[5]);

        // Test with stopAtNonOption set to false
        args = new String[]{"-f", "value", "nonOption", "-abc", "123"};
        result = parser.flatten(options, args, false);

        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
        assertEquals("-abc", result[3]);
        assertEquals("123", result[4]);

        // Test with no options
        args = new String[]{"arg1", "arg2"};
        result = parser.flatten(new Options(), args, true);

        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);

        // Test with empty arguments
        args = new String[]{};
        result = parser.flatten(new Options(), args, true);

        assertNotNull(result);
        assertEquals(0, result.length);
    }
}