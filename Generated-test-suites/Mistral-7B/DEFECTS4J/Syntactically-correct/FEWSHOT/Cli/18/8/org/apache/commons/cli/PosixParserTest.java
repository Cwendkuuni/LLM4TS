package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[] {"--foo", "--bar=baz", "-c", "-d", "-e=-f", "-g foo bar", "--", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", false, "c");
        options.addOption("d", false, "d");
        options.addOption("e", true, "e");
        options.addOption("g", true, "g");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(13, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals("baz", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("-d", result[4]);
        assertEquals("-e", result[5]);
        assertEquals("=", result[6]);
        assertEquals("f", result[7]);
        assertEquals("--", result[8]);
        assertEquals("arg1", result[9]);
        assertEquals("arg2", result[10]);
        assertEquals("arg3", result[11]);

        assertFalse(Arrays.asList(result).contains("--foo="));
        assertFalse(Arrays.asList(result).contains("--bar="));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = new String[] {"--foo", "--bar=baz", "-c", "-d", "-e=-f", "-g foo bar", "--", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", false, "c");
        options.addOption("d", false, "d");
        options.addOption("e", true, "e");
        options.addOption("g", true, "g");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(14, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar=baz", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("-d", result[3]);
        assertEquals("-e", result[4]);
        assertEquals("=", result[5]);
        assertEquals("f", result[6]);
        assertEquals("--", result[7]);
        assertEquals("arg1", result[8]);
        assertEquals("arg2", result[9]);
        assertEquals("arg3", result[10]);
        assertEquals("--", result[11]);
        assertNull(result[12]);
    }

    @Test
    public void testBurstToken() {
        String[] args = new String[] {"--foo=-bar=baz", "--foo=-bar=qux", "--foo=quux", "--foo"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList("--foo", "=", "bar", "baz", "--foo", "=", "qux", "quux", "--foo");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String[] args = new String[] {"--foo=-bar=baz", "--foo=-bar=qux", "--foo=quux", "--foo"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List expected = Arrays.asList("--foo=-bar=baz", "--foo=quux");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
        assertNull(result[result.length]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"-"};
        String[] result = parser.flatten(new Options(), args, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--foo"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);

        options.addOption("bar", true, "b");
        result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--foo"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals(null, result[1]);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--", "arg1", "arg2", "arg3"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("arg3", result[3]);

        result = parser.flatten(options, args, true);
        assertEquals(5, result.length);
        assertEquals("--", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("arg3", result[3]);
        assertNull(result[4]);
    }

    @Test
    public void testProcessSingleHyphenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--", "-"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertNull(result[1]);
    }

    @Test
    public void testProcessSingleHyphenWithEatTheRest() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--", "-", "arg1"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-", result[1]);
        assertEquals("arg1", result[2]);

        result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testProcessOptionTokenWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--foo=arg"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.getOption("foo").setArgName("arg");

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("arg", result[1]);

        result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testProcessOptionTokenWithArgs() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--foo=arg1 arg2 arg3"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.getOption("foo").setArgName("arg");
        options.getOption("foo").setArgParser(new StringParser());

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--foo", "arg1", "arg2", "arg3");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }

        result = parser.flatten(options, args, true);
        List expected2 = Arrays.asList("--foo", "arg1 arg2 arg3");
        assertEquals(expected2.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected2.get(i), result[i]);
        }
    }

    @Test
    public void testProcessOptionTokenWithArgAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--foo=arg"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.getOption("foo").setArgName("arg");

        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertNull(result[1]);
    }

    @Test
    public void testProcessOptionTokenWithArgsAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[] {"--foo=arg1 arg2 arg3"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.getOption("foo").setArgName("arg");
        options.getOption("foo").setArgParser(new StringParser());

        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("--foo=arg1", result[0]);
        assertNull(result[1]);
        assertEquals("arg2 arg3", result[2]);
    }
}