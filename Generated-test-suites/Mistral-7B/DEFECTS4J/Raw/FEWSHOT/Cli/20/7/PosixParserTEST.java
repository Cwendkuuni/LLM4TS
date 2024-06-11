package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_Simple() {
        Options options = new Options();
        String[] arguments = {"--foo", "--bar=baz", "-x", "--zap", "-a", "--quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "baz", "-x", "--zap", "-a", "--quux"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "--bar=baz", "-x", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--foo", "--bar=baz", "quux"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("foo", false, "f", "foo");
        options.addOption("bar", true, "b", "bar");
        String[] arguments = {"--foo", "--bar=quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "quux"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("foo", false, "f");
        String[] arguments = {"--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f", "foo");
        String[] arguments = {"-f", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-f", "quux"}, result);
    }

    @Test
    public void testFlatten_OptionWithoutValue() {
        Options options = new Options();
        options.addOption("foo", false, "f");
        String[] arguments = {"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-f"}, result);
    }

    @Test
    public void testFlatten_ShortAndLongOptions() {
        Options options = new Options();
        options.addOption("x", true, "x", "x");
        options.addOption("foo", false, "f", "foo");
        String[] arguments = {"-x", "--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-x", "--foo"}, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("x", true, "x", "x");
        String[] arguments = {"-x", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-x", "quux"}, result);
    }

    @Test
    public void testFlatten_BurstedShortOption() {
        Options options = new Options();
        options.addOption("x", false);
        options.addOption("y", false);
        String[] arguments = {"-xy", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-x", "-y", "quux"}, result);
    }

    @Test
    public void testFlatten_BurstedLongOption() {
        Options options = new Options();
        options.addOption("xy", false);
        String[] arguments = {"--xy", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--xy", "quux"}, result);
    }

    @Test
    public void testFlatten_BurstedLongOptionWithValue() {
        Options options = new Options();
        options.addOption("xy", true, "xy", "xy");
        String[] arguments = {"--xy", "quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--xy", "quux"}, result);
    }

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_EmptyOptions() {
        String[] arguments = {"--foo", "--bar=quux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "quux"}, result);
    }

    @Test
    public void testFlatten_NullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testProcess_OptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "quux"};
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(3, tokens.size());
        assertSame(arguments[0], tokens.get(0));
        assertSame(arguments[1], tokens.get(1));
        assertEquals("quux", tokens.get(2));
    }

    @Test
    public void testProcess_OptionWithoutValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f"};
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertSame(arguments[0], tokens.get(0));
        assertNull(tokens.get(1));
    }

    @Test
    public void testProcess_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f", "foo");
        String[] arguments = {"--foo", "quux"};
        parser.setOptions(options);
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(3, tokens.size());
        assertSame(arguments[0], tokens.get(0));
        assertEquals("quux", tokens.get(1));
        assertEquals("foo", tokens.get(2));
    }

    @Test
    public void testProcess_LongOptionWithoutValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", false, "f");
        String[] arguments = {"--foo"};
        parser.setOptions(options);
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertSame(arguments[0], tokens.get(0));
        assertSame(options.findOption("foo"), tokens.get(1));
    }

    @Test
    public void testProcess_SingleDash() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertSame(arguments[0], tokens.get(0));
    }

    @Test
    public void testProcess_DoubleDash() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--"};
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertSame(arguments[0], tokens.get(0));
    }

    @Test
    public void testProcess_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        parser.process(arguments, false, null);
        List tokens = parser.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testProcess_NullArguments() {
        PosixParser parser = new PosixParser();
        parser.process(null, false, null);
        List tokens = parser.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testBurstToken_Simple() {
        PosixParser parser = new PosixParser();
        String token = "--xyz";
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertSame(token, tokens.get(0));
    }

    @Test
    public void testBurstToken_WithOption() {
        PosixParser parser = new PosixParser();
        String token = "--xy";
        parser.setOptions(new Options());
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertSame("-x", tokens.get(0));
        assertSame("-y", tokens.get(1));
    }

    @Test
    public void testBurstToken_WithValue() {
        PosixParser parser = new PosixParser();
        String token = "--xyz=quux";
        parser.setOptions(new Options());
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        assertEquals(3, tokens.size());
        assertSame("-x", tokens.get(0));
        assertSame("-y", tokens.get(1));
        assertSame("quux", tokens.get(2));
    }

    @Test
    public void testBurstToken_WithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--xyz=quux";
        parser.setOptions(new Options());
        parser.burstToken(token, true);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertSame("--xyz=quux", tokens.get(0));
    }

    @Test
    public void testBurstToken_WithLongOption() {
        PosixParser parser = new PosixParser();
        String token = "--xy";
        Options options = new Options();
        options.addOption("xy", false);
        parser.setOptions(options);
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertSame("--xy", tokens.get(0));
        assertSame("xy", tokens.get(1));
    }

    @Test
    public void testBurstToken_WithLongOptionAndValue() {
        PosixParser parser = new PosixParser();
        String token = "--xyz=quux";
        Options options = new Options();
        options.addOption("xyz", false);
        parser.setOptions(options);
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        assertEquals(3, tokens.size());
        assertSame("--xyz=quux", tokens.get(0));
        assertSame("xyz", tokens.get(1));
        assertSame("quux", tokens.get(2));
    }

    @Test
    public void testBurstToken_WithLongOptionAndValueAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--xyz=quux";
        Options options = new Options();
        options.addOption("xyz", false);
        parser.setOptions(options);
        parser.burstToken(token, true);
        List tokens = parser.getTokens();
        assertEquals(1, tokens.size());
        assertSame("--xyz=quux", tokens.get(0));
    }
}