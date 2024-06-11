
package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        // Test when options exists for the first character of arguments entry
        String[] arguments1 = {"-a", "abc", "-b", "def"};
        String[] expected1 = {"-a", "abc", "-b", "def"};
        assertArrayEquals(expected1, new GnuParser().flatten(options, arguments1, false));

        // Test when options do not exist for the whole argument
        String[] arguments2 = {"--", "-a", "--xyz"};
        String[] expected2 = {"--", "-a", "--xyz"};
        assertArrayEquals(expected2, new GnuParser().flatten(options, arguments2, false));

        // Test when options do not exist for the whole argument and stopAtNonOption is true
        String[] arguments3 = {"--", "-a", "--xyz"};
        String[] expected3 = {"--"};
        assertArrayEquals(expected3, new GnuParser().flatten(options, arguments3, true));

        // Test when argument is "-" or "-"
        String[] arguments4 = {"-", "-"};
        String[] expected4 = {"-", "-"};
        assertArrayEquals(expected4, new GnuParser().flatten(options, arguments4, false));

        // Test when argument starts with "-" and options do not exist
        String[] arguments5 = {"-d", "abc", "xyz"};
        String[] expected5 = {"-d", "abc", "xyz"};
        assertArrayEquals(expected5, new GnuParser().flatten(options, arguments5, false));

        // Test when argument starts with "-" and options exist
        String[] arguments6 = {"-a", "-b", "-c"};
        String[] expected6 = {"-a", "-b", "-c"};
        assertArrayEquals(expected6, new GnuParser().flatten(options, arguments6, false));

        // Test when argument does not start with "-"
        String[] arguments7 = {"abc", "def"};
        String[] expected7 = {"abc", "def"};
        assertArrayEquals(expected7, new GnuParser().flatten(options, arguments7, false));

        // Test when argument is "--"
        String[] arguments8 = {"--"};
        String[] expected8 = {"--"};
        assertArrayEquals(expected8, new GnuParser().flatten(options, arguments8, false));
    }
}
