package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("D", true, "Option D");
        options.addOption("foo", false, "Option foo");

        String[] args1 = {"-f"};
        String[] args2 = {"--foo"};
        String[] args3 = {"-Dproperty=value"};
        String[] args4 = {"--", "argument1", "argument2"};

        String[] result1 = GnuParser.flatten(options, args1, true);
        String[] result2 = GnuParser.flatten(options, args2, true);
        String[] result3 = GnuParser.flatten(options, args3, true);
        String[] result4 = GnuParser.flatten(options, args4, true);

        assertEquals(1, result1.length);
        assertEquals("-f", result1[0]);

        assertEquals(1, result2.length);
        assertEquals("--foo", result2[0]);

        assertEquals(2, result3.length);
        assertEquals("-D", result3[0]);
        assertEquals("property=value", result3[1]);

        assertEquals(3, result4.length);
        assertEquals("--", result4[0]);
        assertEquals("argument1", result4[1]);
        assertEquals("argument2", result4[2]);
    }
}