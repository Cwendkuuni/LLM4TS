package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        assertIterableEquals(Arrays.asList(), Arrays.asList(result));
    }

    @Test
    public void testFlattenSimple() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar", "--baz=qux", "-x", "--", "arg"};
        Options options = new Options();
        options.addRequiredOption("f", "foo");
        options.addOption("b", "bar");
        options.addOption("z", "zaz");
        options.addOption("x", "xor");

        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--foo", "--bar", "--zaz", "-x", "--", "arg");
        assertIterableEquals(expected, Arrays.asList(result));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar", "--baz=qux", "-x", "--", "arg", "-z"};
        Options options = new Options();
        options.addRequiredOption("f", "foo");
        options.addOption("b", "bar");
        options.addOption("z", "zaz");
        options.addOption("x", "xor");
        options.addOption("z", "zaz2");

        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("--foo", "--bar", "--baz=qux", "-x", "--zaz2", "arg", "-z");
        assertIterableEquals(expected, Arrays.asList(result));
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndUnknownOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar", "--baz=qux", "-x", "--", "arg", "-z", "--unknown"};
        Options options = new Options();
        options.addRequiredOption("f", "foo");
        options.addOption("b", "bar");
        options.addOption("z", "zaz");
        options.addOption("x", "xor");

        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("--foo", "--bar", "--baz=qux", "-x", "--arg", "-z", "--unknown");
        assertIterableEquals(expected, Arrays.asList(result));
    }

    @Test
    public void testBurstTokenSimple() {
        PosixParser parser = new PosixParser();
        String token = "-xx";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);
        List expected = Arrays.asList("-x", "-x");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithArg() {
        PosixParser parser = new PosixParser();
        String token = "-xx--qux";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);
        List expected = Arrays.asList("-x", "--qux");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-xx--qux";
        boolean stopAtNonOption = true;

        parser.burstToken(token, stopAtNonOption);
        List expected = Arrays.asList("--", "-x", "--qux");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionAndUnknownOption() {
        PosixParser parser = new PosixParser();
        String token = "-xx--qux--unknown";
        boolean stopAtNonOption = true;

        parser.burstToken(token, stopAtNonOption);
        List expected = Arrays.asList("--", "-x", "--qux", "--unknown");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "-x";
        boolean stopAtNonOption = false;
        Options options = new Options();
        options.addOption("x", "xor");

        parser.processOptionToken(token, stopAtNonOption);
        List expected = Arrays.asList("-x");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-x";
        boolean stopAtNonOption = true;
        Options options = new Options();

        parser.processOptionToken(token, stopAtNonOption);
        List expected = Arrays.asList("--", "-x");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "arg";
        Options options = new Options();

        parser.processNonOptionToken(token);
        List expected = Arrays.asList("--", "arg");
        assertIterableEquals(expected, parser.tokens);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
    }
}