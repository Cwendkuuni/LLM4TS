package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("v").build());
        options.addOption(Option.builder("a").argName("arg").hasArg().build());

        PosixParser parser = new PosixParser();

        // Test empty arguments
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test arguments with options
        arguments = new String[]{"-f", "-v", "-a", "arg1"};
        expected = new String[]{"-f", "-v", "-a", "arg1"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test arguments with non-options
        arguments = new String[]{"-f", "nonoption", "-v", "nonoption2"};
        expected = new String[]{"-f", "nonoption", "-v", "nonoption2"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test arguments with stopAtNonOption
        arguments = new String[]{"-f", "nonoption", "-v", "nonoption2"};
        expected = new String[]{"-f"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);

        // Test arguments with option with argument
        arguments = new String[]{"-a", "arg1"};
        expected = new String[]{"-a", "arg1"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test arguments with option without argument
        arguments = new String[]{"-f", "-a"};
        expected = new String[]{"-f", "-a"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test arguments with option without argument but followed by another option
        arguments = new String[]{"-a", "-v"};
        expected = new String[]{"-a", "-v"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}