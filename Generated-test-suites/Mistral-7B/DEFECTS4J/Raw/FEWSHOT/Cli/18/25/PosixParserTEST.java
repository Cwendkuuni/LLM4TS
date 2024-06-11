package org.apache.commons.cli;

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
    public void testFlattenEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenSimple() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--foo"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }

    @Test
    public void testFlattenSimpleWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"-f"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo=bar"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlattenBurstedOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"-f--bar"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-f--bar", result[0]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenBurstedOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo=bar--baz"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--baz", result[2]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo", "-f", "bar"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(4, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("-f", result[1]);
        assertEquals("bar", result[2]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenStopAtNonOptionBurstedOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo=bar--baz"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("--foo=bar", result[0]);
        assertEquals("--baz", result[1]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenUnknownOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--qux"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--qux", result[0]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenUnknownOptionBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--qux--bar"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--qux--bar", result[0]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenUnhandledToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"foo", "bar"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenUnhandledTokenBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"foo--bar", "baz"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("foo--bar", result[0]);
        assertEquals("baz", result[1]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenUnhandledTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"foo--bar", "baz"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("foo--bar", result[0]);
        assertEquals("baz", result[1]);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "--foo";
        parser.burstToken(token, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenMultipleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "--foo=bar--baz";
        parser.burstToken(token, false);
        assertEquals(3, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0).toString());
        assertEquals("bar", parser.tokens.get(1).toString());
        assertEquals("--baz", parser.tokens.get(2).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenUnknownOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "--qux--bar";
        parser.burstToken(token, false);
        assertEquals(2, parser.tokens.size());
        assertEquals("--qux--bar", parser.tokens.get(0).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenUnknownOptionBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "--qux--bar--baz";
        parser.burstToken(token, false);
        assertEquals(3, parser.tokens.size());
        assertEquals("--qux--bar", parser.tokens.get(0).toString());
        assertEquals("--baz", parser.tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "--foo=bar--baz";
        parser.burstToken(token, true);
        assertEquals(3, parser.tokens.size());
        assertEquals("--foo=bar", parser.tokens.get(0).toString());
        assertEquals("--baz", parser.tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenStopAtNonOptionBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String token = "--qux--bar--baz";
        parser.burstToken(token, true);
        assertEquals(3, parser.tokens.size());
        assertEquals("--qux--bar", parser.tokens.get(0).toString());
        assertEquals("--baz", parser.tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String token = "--foo=bar";
        parser.burstToken(token, false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-f--foo", parser.tokens.get(0).toString());
        assertEquals("bar", parser.tokens.get(1).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenWithArgBursted() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String token = "--foo=bar--baz";
        parser.burstToken(token, false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-f--foo", parser.tokens.get(0).toString());
        assertEquals("bar", parser.tokens.get(1).toString());
        assertEquals("--baz", parser.tokens.get(2).toString());
        assertNull(parser.currentOption);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }
}