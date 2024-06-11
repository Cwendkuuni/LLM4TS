package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("d", true, "Option d");

        GnuParser parser = new GnuParser();

        // Test case for "--"
        String[] arguments1 = { "--" };
        assertEquals(1, parser.flatten(options, arguments1, true).length);

        // Test case for "-"
        String[] arguments2 = { "-" };
        assertEquals(1, parser.flatten(options, arguments2, true).length);

        // Test case for option with single character
        String[] arguments3 = { "-f" };
        assertEquals(1, parser.flatten(options, arguments3, true).length);

        // Test case for option with multiple characters
        String[] arguments4 = { "--d" };
        assertEquals(1, parser.flatten(options, arguments4, true).length);

        // Test case for option with argument
        String[] arguments5 = { "--d", "value" };
        assertEquals(2, parser.flatten(options, arguments5, true).length);

        // Test case for multiple options
        String[] arguments6 = { "-f", "--d" };
        assertEquals(2, parser.flatten(options, arguments6, true).length);

        // Test case for non-option argument
        String[] arguments7 = { "arg" };
        assertEquals(1, parser.flatten(options, arguments7, true).length);

        // Test case for stopAtNonOption=false
        String[] arguments8 = { "-f", "arg" };
        assertEquals(2, parser.flatten(options, arguments8, false).length);
    }
}