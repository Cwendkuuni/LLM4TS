package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        // create an instance of GnuParser
        GnuParser parser = new GnuParser();

        // create some test data
        Options options = new Options();
        String[] arguments = {"-a", "value1", "--option", "value2", "nonOption"};

        // call the flatten method and get the result
        String[] result = parser.flatten(options, arguments, true);

        // assert that the result is as expected
        assertEquals("Unexpected length of flattened arguments", 4, result.length);
        assertEquals("Unexpected value in flattened arguments", "-a", result[0]);
        assertEquals("Unexpected value in flattened arguments", "value1", result[1]);
        assertEquals("Unexpected value in flattened arguments", "--option", result[2]);
        assertEquals("Unexpected value in flattened arguments", "value2", result[3]);
    }
}
