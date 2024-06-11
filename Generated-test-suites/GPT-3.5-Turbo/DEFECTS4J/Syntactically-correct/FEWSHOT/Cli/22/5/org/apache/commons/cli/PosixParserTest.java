package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("foo").argName("bar").build());

        PosixParser parser = new PosixParser();

        // Test "--foo" option with value
        String[] arguments1 = {"--foo=bar"};
        String[] expected1 = {"--foo", "bar"};
        String[] result1 = parser.flatten(options, arguments1, true);
        assertArrayEquals(expected1, result1);

        // Test "--foo" option without value
        String[] arguments2 = {"--foo"};
        String[] expected2 = {"--foo"};
        String[] result2 = parser.flatten(options, arguments2, true);
        assertArrayEquals(expected2, result2);

        // Test "-f" option without value
        String[] arguments3 = {"-f"};
        String[] expected3 = {"-f"};
        String[] result3 = parser.flatten(options, arguments3, true);
        assertArrayEquals(expected3, result3);

        // Test "--" token without options after it
        String[] arguments4 = {"--"};
        String[] expected4 = {"--"};
        String[] result4 = parser.flatten(options, arguments4, true);
        assertArrayEquals(expected4, result4);

        // Test token without "-" or "--" prefix
        String[] arguments5 = {"foo"};
        String[] expected5 = {"foo"};
        String[] result5 = parser.flatten(options, arguments5, true);
        assertArrayEquals(expected5, result5);
    }
}