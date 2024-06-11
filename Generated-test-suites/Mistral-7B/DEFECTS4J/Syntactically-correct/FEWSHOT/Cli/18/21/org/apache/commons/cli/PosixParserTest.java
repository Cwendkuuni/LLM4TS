package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "-c", "--d=e", "-e"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "-a", "--d", "e", "-c", "-e"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgs() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "--bar=baz", "qux", "-c", "corge"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "-a", "--bar", "baz", "qux", "-c", "corge"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] args = {"--foo", "--bar"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "--bar"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "--bar", "qux", "-c", "corge"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--foo", "-a", "qux", "-c", "corge"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "b");
        options.addOption("c", true, "c");
        String[] args = {"-abc"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "-b", "-c"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "b");
        options.addOption("c", true, "c");
        String[] args = {"-abc", "--def"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--def"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongBursting() {
        Options options = new Options();
        options.addOption("ab", true);
        options.addOption("cd", true);
        String[] args = {"-abcdef"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-ab", "-cd"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("ab", true);
        options.addOption("cd", true);
        String[] args = {"-abcdef", "--xyz"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-abcdef", "--xyz"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        List<String> tokens = parser.flatten(new Options(), args, false);
        assertEquals("-", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("foo", true);
        PosixParser parser = new PosixParser();
        String[] args = {"-foo"};
        List<String> tokens = parser.flatten(options, args, false);
        assertEquals("-foo", tokens.get(0));
        assertEquals("foo", tokens.get(1));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionTokenWithArgument() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        PosixParser parser = new PosixParser();
        String[] args = {"-foo=bar"};
        List<String> tokens = parser.flatten(options, args, false);
        assertEquals("-foo", tokens.get(0));
        assertEquals("foo", tokens.get(1));
        assertEquals("bar", tokens.get(2));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessLongOptionToken() {
        Options options = new Options();
        options.addOption("foo", true);
        PosixParser parser = new PosixParser();
        String[] args = {"--foo"};
        List<String> tokens = parser.flatten(options, args, false);
        assertEquals("--foo", tokens.get(0));
        assertEquals("foo", tokens.get(1));
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();
        String token = "-abc";
        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("-a", "-b", "-c");
        assertTrue(expected.equals(parser.tokens));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();
        String token = "-abcdef";
        parser.burstToken(token, true);
        List<String> expected = Arrays.asList("-abcdef");
        assertTrue(expected.equals(parser.tokens));
    }

    @Test
    public void testBurstTokenWithLongTokens() {
        Options options = new Options();
        options.addOption("ab", true);
        options.addOption("cd", true);
        PosixParser parser = new PosixParser();
        String token = "-abcdef";
        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("-ab", "-cd");
        assertTrue(expected.equals(parser.tokens));
    }
}