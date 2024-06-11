package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        // Test case: No arguments
        String[] arguments1 = {};
        String[] expectedOutput1 = {};
        assertArrayEquals(expectedOutput1, parser.flatten(options, arguments1, false));

        // Test case: Only "--"
        String[] arguments2 = {"--"};
        String[] expectedOutput2 = {"--"};
        assertArrayEquals(expectedOutput2, parser.flatten(options, arguments2, false));

        // Test case: Only "-"
        String[] arguments3 = {"-"};
        String[] expectedOutput3 = {"-"};
        assertArrayEquals(expectedOutput3, parser.flatten(options, arguments3, false));

        // Test case: Argument starts with "-"
        String[] arguments4 = {"-Dproperty=value"};
        String[] expectedOutput4 = {"-D", "property=value"};
        assertArrayEquals(expectedOutput4, parser.flatten(options, arguments4, false));

        // Test case: Stop at non-option
        String[] arguments5 = {"-Dproperty=value", "nonOption"};
        String[] expectedOutput5 = {"-D", "property=value", "nonOption"};
        assertArrayEquals(expectedOutput5, parser.flatten(options, arguments5, true));
    }

}
