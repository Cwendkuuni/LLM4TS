
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser = new PosixParser();

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"-f", "value1", "-b", "value2", "value3"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"-f", "value1", "-b", "value2", "value3"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("value3", result[4]);
    }

    @Test
    public void testFlatten_IgnoreInvalidOption() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"-f", "-invalid"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_ArgumentWithEqualSign() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"--foo=bar"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_ProcessSingleHyphen() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ProcessHyphenAtStart() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"-f", "-bar"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-bar", result[1]);
    }

    @Test
    public void testFlatten_ProcessNonOptionStop() {
        Options options = new Options();
        options.addOption("f", false, "foo");
        options.addOption("b", true, "bar");
        options.addOption("a", false, "baz");

        String[] arguments = {"-f", "value1", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }
}
