package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        options.addOption("f", false, "A boolean option");
        options.addOption("foo", 3, "An option with an integer value");
        String[] arguments = {"-f", "bar", "--foo", "1", "2", "3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-f", "bar", "--foo", "1", "2", "3"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");
        options.addOption("bar", 3, "An option with an integer value");
        String[] arguments = {"--foo", "--bar=1", "--baz", "qux"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--foo", "--bar", "1"}, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");
        String[] arguments = {"-f", "--bar", "-foo"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-f", "--bar", "-foo"}, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--baz", "-invalid"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--baz", "-invalid"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");
        String[] arguments = {"--foo=longvalue"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--foo=longvalue"}, result);
    }

    @Test
    public void testProcessOptionToken() {
        String[] arguments = {"-f", "--foo", "-bar"};

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-f", false);
        assertSame(options, parser.options);
        assertEquals("-f", parser.getCurrentOption());

        parser.init();
        parser.processOptionToken("--foo", false);
        assertNull(parser.getCurrentOption());

        parser.init();
        parser.processOptionToken("-bar", false);
        assertEquals("-bar", parser.getCurrentOption());
    }

    @Test
    public void testProcessNonOptionToken() {
        String[] arguments = {"-f", "arg1", "--foo", "arg2"};

        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("arg1");
        assertEquals("--", parser.getCurrentOption());
        assertArrayEquals(new String[]{"arg1"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));

        parser.init();
        parser.processNonOptionToken("arg2");
        assertNull(parser.getCurrentOption());
        assertArrayEquals(new String[]{"--", "arg2"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test(expected = AssertionError.class)
    public void testProcessNonOptionTokenWithEatTheRest() {
        String[] arguments = {"-f", "arg1", "--foo", "arg2"};

        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("arg2");
        parser.eatTheRest = true;
        parser.processNonOptionToken("arg3");
    }

    @Test
    public void testBurstToken() {
        String token = "-foobar";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-foobar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));

        parser.init();
        parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--", "foobar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testBurstTokenWithOption() {
        String token = "-foobar";
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-foo", "-bar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));

        parser.init();
        parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--foo", "--bar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testBurstTokenWithValue() {
        String token = "--foo=bar";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        assertArrayEquals(new String[]{"--foo", "bar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));

        parser.init();
        parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--foo=bar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "-foo-bar";

        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--", "-foo-bar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));

        parser.init();
        parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-foo", "-bar"}, parser.getTokens().toArray(new String[parser.getTokens().size()]));
    }
}