package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();

        // Test with only options
        String[] args1 = {"-a", "-b", "valueB", "-c"};
        String[] result1 = parser.flatten(options, args1, false);
        assertEquals(args1.length, result1.length);
        for (int i = 0; i < args1.length; i++) {
            assertEquals(args1[i], result1[i]);
        }

        // Test with non-option arguments
        String[] args2 = {"-a", "arg1", "-b", "valueB", "arg2", "-c", "arg3"};
        String[] result2 = parser.flatten(options, args2, false);
        assertEquals(args2.length, result2.length);
        for (int i = 0; i < args2.length; i++) {
            assertEquals(args2[i], result2[i]);
        }

        // Test with stopAtNonOption
        String[] args3 = {"-a", "arg1", "-b", "valueB", "arg2", "-c", "arg3"};
        String[] result3 = parser.flatten(options, args3, true);
        assertEquals(4, result3.length);
        assertEquals("-a", result3[0]);
        assertEquals("arg1", result3[1]);
        assertEquals("-b", result3[2]);
        assertEquals("valueB", result3[3]);
    }
}