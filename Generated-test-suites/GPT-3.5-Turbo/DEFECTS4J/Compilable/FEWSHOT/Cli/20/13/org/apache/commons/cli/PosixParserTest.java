
package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Description for a");
        options.addOption("b", true, "Description for b");
        options.addOption("c", false, "Description for c");

        PosixParser parser = new PosixParser();

        // Test case 1: all options present
        String[] args1 = {"-a", "-b", "value", "-c"};
        String[] expected1 = {"-a", "-b", "value", "-c"};
        assertArrayEquals(expected1, parser.flatten(options, args1, false));

        // Test case 2: start with non-option
        String[] args2 = {"nonOption", "-a", "-b", "value", "-c"};
        String[] expected2 = {"nonOption", "-a", "-b", "value", "-c"};
        assertArrayEquals(expected2, parser.flatten(options, args2, true));

        // Test case 3: stop at non-option
        String[] args3 = {"-a", "-b", "value", "-c", "nonOption"};
        String[] expected3 = {"-a", "-b", "value", "-c"};
        assertArrayEquals(expected3, parser.flatten(options, args3, true));

        // Test case 4: long option with argument
        String[] args4 = {"--b=value"};
        String[] expected4 = {"--b", "value"};
        assertArrayEquals(expected4, parser.flatten(options, args4, false));

        // Test case 5: single hyphen
        String[] args5 = {"-"};
        String[] expected5 = {"-"};
        assertArrayEquals(expected5, parser.flatten(options, args5, false));

        // Test case 6: unsupported option
        String[] args6 = {"-d"};
        String[] expected6 = {"-d"};
        assertArrayEquals(expected6, parser.flatten(options, args6, false));

        // Test case 7: burst token
        String[] args7 = {"-abc"};
        String[] expected7 = {"-a", "-b", "-c"};
        assertArrayEquals(expected7, parser.flatten(options, args7, false));
    }
}
