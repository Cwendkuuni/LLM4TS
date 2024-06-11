package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenWithOption() {
        options.addOption("f", false, "option f");
        String[] arguments = {"-f"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenWithNonOption() {
        options.addOption("f", false, "option f");
        String[] arguments = {"nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlattenWithMultipleOptionsAndNonOption() {
        options.addOption("f", false, "option f");
        options.addOption("o", false, "option o");
        String[] arguments = {"-f", "-o", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-o", result[1]);
        assertEquals("--", result[2]);
        assertEquals("nonOption", result[3]);
    }
}