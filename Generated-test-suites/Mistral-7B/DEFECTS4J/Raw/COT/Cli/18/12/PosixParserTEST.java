package org.junit.tests;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PosixParser class
 */
public class PosixParserTest {

    

    @Test
    public void testEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        try {
            parser.flatten(new Options(), args, false);
            fail("Expected ParserException");
        } catch (ParserException e) {
            // expected
        }
    }

    @Test
    public void testSimpleArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar", "arg1", "arg2", "-x"};
        String[] expected = {"--foo", "--bar", "arg1", "arg2", "-x"};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testValidOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo", "arg1", "arg2"};
        String[] expected = {"--foo", "arg1", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testValidOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo"};
        String[] expected = {"--foo"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testValidOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo=arg1", "--foo=arg2"};
        String[] expected = {"--foo", "arg1", "--foo", "arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] expected = {"-"};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test
    public void testDoubleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        String[] expected = {"--"};
        assertArrayEquals(expected, parser.flatten(new Options(), args, false));
    }

    @Test(expected = ParserException.class)
    public void testInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--baz"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo", "-x", "--bar", "arg1"};
        String[] expected = {"--foo", "-x"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testStopAtNonOptionWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo", "--baz"};
        String[] expected = {"--foo", "--baz"};
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testBurstSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"-foobar"};
        String[] expected = {"--foo", "foo"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testBurstMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] args = {"-foobar", "-barbaz"};
        String[] expected = {"--foo", "foo", "--bar", "bar"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testBurstOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] args = {"-foobar=arg1", "-barbaz=arg2"};
        String[] expected = {"--foo", "foo=arg1", "--bar", "bar=arg2"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testBurstOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        String[] args = {"-foobar=arg1,arg2", "-barbaz=arg3,arg4"};
        String[] expected = {"--foo", "foo=arg1,arg2", "--bar", "bar=arg3,arg4"};
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testReset() {
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), new String[0], false);
        assertNull(parser.getCurrentOption());
        assertFalse(parser.hasTokens());
    }

    
}