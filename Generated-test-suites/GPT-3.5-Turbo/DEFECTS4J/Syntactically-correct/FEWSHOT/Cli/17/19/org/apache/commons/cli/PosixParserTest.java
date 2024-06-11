
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").argName("arg").build());

        PosixParser parser = new PosixParser();

        // Test case 1: No options provided
        String[] arguments = {};
        String[] expected = {};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);

        // Test case 2: Single option provided
        arguments = new String[]{"-a"};
        expected = new String[]{"-a"};
        actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);

        // Test case 3: Multiple options provided
        arguments = new String[]{"-a", "-b"};
        expected = new String[]{"-a", "-b"};
        actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);

        // Test case 4: Option with argument provided
        arguments = new String[]{"-c", "argValue"};
        expected = new String[]{"-c", "argValue"};
        actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);

        // Test case 5: Stop at non-option
        arguments = new String[]{"-a", "nonOption1", "-b", "nonOption2"};
        expected = new String[]{"-a"};
        actual = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, actual);
    }
}
