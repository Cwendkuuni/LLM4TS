package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "add option");
        options.addOption("b", true, "boolean option");
        options.addOption("c", "create", false, "create option");

        GnuParser parser = new GnuParser();

        // Test 1: No arguments
        String[] arguments = {};
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(0, flattenedArguments.length);

        // Test 2: Single hyphen
        arguments = new String[]{"-"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(1, flattenedArguments.length);
        assertEquals("-", flattenedArguments[0]);

        // Test 3: Double hyphen
        arguments = new String[]{"--"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(1, flattenedArguments.length);
        assertEquals("--", flattenedArguments[0]);

        // Test 4: Non-option argument
        arguments = new String[]{"nonOptionArg"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(1, flattenedArguments.length);
        assertEquals("nonOptionArg", flattenedArguments[0]);

        // Test 5: Option without argument
        arguments = new String[]{"-a"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(1, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);

        // Test 6: Option with argument
        arguments = new String[]{"-b", "argValue"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(2, flattenedArguments.length);
        assertEquals("-b", flattenedArguments[0]);
        assertEquals("argValue", flattenedArguments[1]);

        // Test 7: Option with abbreviated name
        arguments = new String[]{"-c"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(1, flattenedArguments.length);
        assertEquals("-c", flattenedArguments[0]);

        // Test 8: Option with abbreviated name and argument
        arguments = new String[]{"-c", "argValue"};
        flattenedArguments = parser.flatten(options, arguments, false);
        assertNotNull(flattenedArguments);
        assertEquals(2, flattenedArguments.length);
        assertEquals("-c", flattenedArguments[0]);
        assertEquals("argValue", flattenedArguments[1]);

        // Test 9: Stop at non-option
        arguments = new String[]{"-a", "nonOptionArg", "-b", "argValue"};
        flattenedArguments = parser.flatten(options, arguments, true);
        assertNotNull(flattenedArguments);
        assertEquals(3, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("nonOptionArg", flattenedArguments[1]);
        assertEquals("-b", flattenedArguments[2]);

        // Test 10: Stop at non-option with argument
        arguments = new String[]{"-a", "nonOptionArg", "-b", "argValue", "nonOptionArg2"};
        flattenedArguments = parser.flatten(options, arguments, true);
        assertNotNull(flattenedArguments);
        assertEquals(4, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("nonOptionArg", flattenedArguments[1]);
        assertEquals("-b", flattenedArguments[2]);
        assertEquals("argValue", flattenedArguments[3]);
    }
}