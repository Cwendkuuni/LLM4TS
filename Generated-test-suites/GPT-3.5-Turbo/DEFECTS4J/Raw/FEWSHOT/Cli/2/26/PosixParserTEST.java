package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Arrays;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option A");
        options.addOption("b", "beta", true, "Option B");

        PosixParser parser = new PosixParser();

        // Test empty arguments
        String[] emptyArgs = {};
        String[] result = parser.flatten(options, emptyArgs, false);
        assertEquals(0, result.length);

        // Test single option
        String[] singleOptionArgs = {"-a"};
        result = parser.flatten(options, singleOptionArgs, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);

        // Test option with argument
        String[] optionWithArg = {"-b", "value"};
        result = parser.flatten(options, optionWithArg, false);
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);

        // Test stopAtNonOption=true
        String[] stopAtNonOption = {"-a", "value", "-b"};
        result = parser.flatten(options, stopAtNonOption, true);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);

        // Test bursting
        options.addOption("c", "charlie", true, "Option C");
        String[] burstArgs = {"-abc"};
        result = parser.flatten(options, burstArgs, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);

        // Test invalid option
        String[] invalidOption = {"-d"};
        result = parser.flatten(options, invalidOption, false);
        assertEquals(1, result.length);
        assertEquals("-d", result[0]);
    }
}