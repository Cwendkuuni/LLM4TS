package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=bar", "--baz=qux", "-c", "-d", "-e arg1 arg2", "--", "file.txt", "-f"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(11, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--baz", result[2]);
        assertEquals("qux", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("-d", result[5]);
        assertEquals("--", result[6]);
        assertEquals("file.txt", result[7]);
        assertEquals("-f", result[8]);
        assertNull(result[9]);
        assertNull(result[10]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=bar", "--baz=qux", "-c", "-d", "-e arg1 arg2", "--", "file.txt", "-f", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(12, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--baz", result[2]);
        assertEquals("qux", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("-d", result[5]);
        assertEquals("--", result[6]);
        assertEquals("file.txt", result[7]);
        assertEquals("-f", result[8]);
        assertEquals("nonOption", result[9]);
        assertNull(result[10]);
    }

    @Test
    public void testProcessSpecialToken() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--foo=bar", "--", "arg1", "arg2"};

        parser.flatten(new Options(), arguments, false);

        List<String> tokens = parser.getTokens();
        assertEquals(5, tokens.size());
        assertEquals("--foo", tokens.get(0));
        assertEquals("bar", tokens.get(1));
        assertEquals("--", tokens.get(2));
        assertEquals("arg1", tokens.get(3));
        assertEquals("arg2", tokens.get(4));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-"};

        parser.flatten(new Options(), arguments, false);

        List<String> tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("c", true, "short option with no argument");
        options.addOption("d", false, "short option with no argument");
        options.addOption("e", true, "short option with argument");

        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-c", "-e", "arg1"};

        parser.flatten(options, arguments, false);

        List<String> tokens = parser.getTokens();
        assertEquals(5, tokens.size());
        assertEquals("-c", tokens.get(0));
        assertEquals("--", tokens.get(1));
        assertEquals("e", tokens.get(2));
        assertEquals("arg1", tokens.get(3));
        assertEquals(null, tokens.get(4));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("f", true, "short option with argument");

        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-ffoo"};

        parser.flatten(options, arguments, false);

        List<String> tokens = parser.getTokens();
        assertEquals(3, tokens.size());
        assertEquals("-f", tokens.get(0));
        assertEquals("-f", tokens.get(1));
        assertEquals("foo", tokens.get(2));
    }

    @Test(expected = AssertionError.class)
    public void testBurstTokenWithInvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"-x"};

        parser.flatten(options, arguments, false);

        assertEquals(1, parser.getTokens().size());
        assertEquals("-x", parser.getTokens().get(0));

        // This test case should throw an AssertionError because
        // the "x" option is not defined in the Options object.
        parser.burstToken("x", false);
    }
}