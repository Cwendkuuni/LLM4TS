package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = new String[]{"--foo", "-a", "-b", "-c=value", "--", "arg1", "arg2", "--", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--foo", "-a", "-b", "-c", "value", "arg1", "arg2", "--", "arg3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a");
        String[] args = new String[]{"--foo", "-a", "-b", "-c=value", "--", "arg1", "arg2", "--", "arg3", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        String[] expected = new String[]{"--foo", "-a", "-b", "--", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"arg1", "arg2", "arg3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] args = new String[] {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[] {};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = new String[]{"--foo", "-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--foo", "-a", "arg"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultiArgument() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = new String[]{"--foo", "-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--foo", "-a", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "o");
        String[] args = new String[]{"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--longOption", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "o");
        options.addOption("arg", true, "a");
        String[] args = new String[]{"--longOption=arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--longOption", "arg"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoSuchOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = new String[]{"--nonOption", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = new String[]{"--nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-ab"};
        String[] result = parser.flatten(options, args, false)[0].split(" ");
        String[] expected = new String[]{"-a", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a--value", "-b"};
        String[] result = parser.flatten(options, args, false)[0].split(" ");
        String[] expected = new String[]{"-a", "--value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenNoOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--"};
        String[] result = parser.flatten(options, args, false)[0].split(" ");
        assertTrue(result.length == 0);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"-a--nonOption"};
        String[] result = parser.flatten(options, args, true)[0].split(" ");
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--longOption"};
        String[] result = parser.flatten(options, args, false)[0].split(" ");
        assertEquals("--longOption", result[0]);
    }
}