package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("foo", true, "Option foo");
        options.addOption("bar", true, "Option bar");

        String[] args = { "-f", "--foo", "fooValue", "-bar", "barValue", "nonOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(7, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--foo", result[1]);
        assertEquals("fooValue", result[2]);
        assertEquals("-bar", result[3]);
        assertEquals("barValue", result[4]);
        assertEquals("--", result[5]);
        assertEquals("nonOption", result[6]);
    }
    
    @Test
    public void testFlatten_stopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("foo", true, "Option foo");
        options.addOption("bar", true, "Option bar");

        String[] args = { "-f", "--foo", "fooValue", "-bar", "barValue", "nonOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(8, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--foo", result[1]);
        assertEquals("fooValue", result[2]);
        assertEquals("-bar", result[3]);
        assertEquals("barValue", result[4]);
        assertEquals("--", result[5]);
        assertEquals("nonOption", result[6]);
        assertEquals("nonOption", result[7]);
    }
    
    @Test
    public void testFlatten_withOptions_missingOptionValue() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("foo", true, "Option foo");
        options.addOption("bar", true, "Option bar");

        String[] args = { "-f", "-foo", "-bar", "barValue", "nonOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(6, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-foo", result[1]);
        assertEquals("-bar", result[2]);
        assertEquals("barValue", result[3]);
        assertEquals("--", result[4]);
        assertEquals("nonOption", result[5]);
    }
    
    @Test
    public void testFlatten_withOptions_missingOptionValue_stopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("foo", true, "Option foo");
        options.addOption("bar", true, "Option bar");

        String[] args = { "-f", "-foo", "-bar", "barValue", "nonOption" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(7, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-foo", result[1]);
        assertEquals("-bar", result[2]);
        assertEquals("barValue", result[3]);
        assertEquals("--", result[4]);
        assertEquals("nonOption", result[5]);
        assertEquals("nonOption", result[6]);
    }
}