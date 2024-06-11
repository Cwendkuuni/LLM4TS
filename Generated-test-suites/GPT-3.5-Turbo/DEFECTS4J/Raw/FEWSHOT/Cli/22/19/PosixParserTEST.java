
package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        PosixParser parser = new PosixParser();

        String[] arguments1 = {"-a", "-b", "value", "-c"};
        String[] result1 = parser.flatten(options, arguments1, false);
        assertEquals(4, result1.length);
        assertEquals("-a", result1[0]);
        assertEquals("-b", result1[1]);
        assertEquals("value", result1[2]);
        assertEquals("-c", result1[3]);

        String[] arguments2 = {"-a", "-b", "value", "-c"};
        String[] result2 = parser.flatten(options, arguments2, true);
        assertEquals(3, result2.length);
        assertEquals("-a", result2[0]);
        assertEquals("-b", result2[1]);
        assertEquals("value", result2[2]);

        String[] arguments3 = {"-a", "-d", "-b", "value", "-c"};
        String[] result3 = parser.flatten(options, arguments3, true);
        assertEquals(5, result3.length);
        assertEquals("-a", result3[0]);
        assertEquals("-d", result3[1]);
        assertEquals("-b", result3[2]);
        assertEquals("value", result3[3]);
        assertEquals("-c", result3[4]);

        String[] arguments4 = {"-a", "--", "-b", "value", "-c"};
        String[] result4 = parser.flatten(options, arguments4, false);
        assertEquals(5, result4.length);
        assertEquals("-a", result4[0]);
        assertEquals("--", result4[1]);
        assertEquals("-b", result4[2]);
        assertEquals("value", result4[3]);
        assertEquals("-c", result4[4]);
    }

}
