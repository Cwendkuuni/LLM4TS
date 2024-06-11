package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments1 = {"-a", "arg1", "-b", "arg2"};
        String[] expected1 = {"-a", "arg1", "-b", "arg2"};
        assertArrayEquals(expected1, GnuParser.flatten(options, arguments1, false));
        assertArrayEquals(expected1, GnuParser.flatten(options, arguments1, true));

        String[] arguments2 = {"--", "-a", "-b"};
        String[] expected2 = {"--", "-a", "-b"};
        assertArrayEquals(expected2, GnuParser.flatten(options, arguments2, false));
        assertArrayEquals(expected2, GnuParser.flatten(options, arguments2, true));

        String[] arguments3 = {"-a", "-b"};
        String[] expected3 = {"-a", "-b"};
        assertArrayEquals(expected3, GnuParser.flatten(options, arguments3, false));
        assertArrayEquals(expected3, GnuParser.flatten(options, arguments3, true));

        String[] arguments4 = {"-a", "-c"};
        String[] expected4 = {"-a", "-c"};
        assertArrayEquals(expected4, GnuParser.flatten(options, arguments4, false));
        assertArrayEquals(expected4, GnuParser.flatten(options, arguments4, true));

        String[] arguments5 = {"-b", "value"};
        String[] expected5 = {"-b", "value"};
        assertArrayEquals(expected5, GnuParser.flatten(options, arguments5, false));
        assertArrayEquals(expected5, GnuParser.flatten(options, arguments5, true));

        String[] arguments6 = {"-d"};
        String[] expected6 = {"-d"};
        assertArrayEquals(expected6, GnuParser.flatten(options, arguments6, false));
        assertArrayEquals(expected6, GnuParser.flatten(options, arguments6, true));

        String[] arguments7 = {};
        String[] expected7 = {};
        assertArrayEquals(expected7, GnuParser.flatten(options, arguments7, false));
        assertArrayEquals(expected7, GnuParser.flatten(options, arguments7, true));

        assertNull(GnuParser.flatten(null, arguments1, false));
        assertNull(GnuParser.flatten(options, null, false));
        assertNull(GnuParser.flatten(null, null, false));
    }
}