
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();

        // Test case 1: only options
        String[] args1 = {"-a", "-b", "value"};
        String[] expected1 = {"-a", "-b", "value"};
        String[] result1 = parser.flatten(options, args1, false);
        assertEquals(expected1.length, result1.length);
        for (int i = 0; i < expected1.length; i++) {
            assertEquals(expected1[i], result1[i]);
        }

        // Test case 2: options and arguments
        String[] args2 = {"-a", "-b", "value", "arg1", "arg2"};
        String[] expected2 = {"-a", "-b", "value", "arg1", "arg2"};
        String[] result2 = parser.flatten(options, args2, false);
        assertEquals(expected2.length, result2.length);
        for (int i = 0; i < expected2.length; i++) {
            assertEquals(expected2[i], result2[i]);
        }

        // Test case 3: options and stopAtNonOption
        String[] args3 = {"-a", "-b", "value", "arg1", "arg2"};
        String[] expected3 = {"-a", "-b", "value"};
        String[] result3 = parser.flatten(options, args3, true);
        assertEquals(expected3.length, result3.length);
        for (int i = 0; i < expected3.length; i++) {
            assertEquals(expected3[i], result3[i]);
        }
    }
}
