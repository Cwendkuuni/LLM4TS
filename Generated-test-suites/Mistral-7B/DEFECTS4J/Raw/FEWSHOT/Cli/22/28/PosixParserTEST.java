package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "-b", "-c=d"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "-a", "-b", "-c", "d"}, result);
    }

    @Test
    public void testFlattenLongOptionsWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] arguments = {"--foo", "--bar=baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "baz"}, result);
    }

    @Test
    public void testFlattenLongOptionsWithoutValue() {
        Options options = new Options();
        options.addOption("foo", false, "f");
        options.addOption("bar", false, "b");
        String[] arguments = {"--foo", "--bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "--bar"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] arguments = {"--foo", "-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--foo", "-a", "-b"}, result);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] arguments = {"-foobar", "-abc"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-foo", "bar", "-ab", "c"}, result);
    }

    @Test
    public void testFlattenWithBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] arguments = {"-foobar", "-abc"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-foobar", "-abc"}, result);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.processNonOptionToken("--foo", tokens);
        assertEquals("--", tokens.get(0));
        assertEquals("foo", tokens.get(1));
        assertNull(tokens.get(2));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("foo", false, "f");
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.processOptionToken("--foo", tokens, false);
        assertEquals("--foo", tokens.get(0));
        assertNull(tokens.get(1));
    }

    @Test
    public void testProcessOptionTokenWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.processOptionToken("--foo=bar", tokens, false);
        assertEquals("--foo", tokens.get(0));
        assertEquals("bar", tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.burstToken("-foobar", tokens, false);
        assertEquals("-foo", tokens.get(0));
        assertEquals("bar", tokens.get(1));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        parser.burstToken("-foobar", tokens, true);
        assertEquals("-foobar", tokens.get(0));
        assertNull(tokens.get(1));
    }
}