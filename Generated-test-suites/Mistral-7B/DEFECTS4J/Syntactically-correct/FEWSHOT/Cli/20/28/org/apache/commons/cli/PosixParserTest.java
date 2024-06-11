package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] expected = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = new String[] { "-a", "1", "--b", "2", "--c=3" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "-a", "--b", "--c=3" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("a", "a", true, "A");
        options.addOption("b", "b", false, "B");
        String[] arguments = new String[] { "--a", "1", "--b", "2", "--c=3", "--no-c" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--a", "--b", "--no-c", "--c=3" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortAndLongOptions() {
        Options options = new Options();
        options.addOption("a", "a", true, "A");
        options.addOption("b", "b", false, "B");
        String[] arguments = new String[] { "-a", "1", "--b", "2", "-c", "3", "--no-c" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "-a", "--b", "--no-c", "-c" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgOption() {
        Options options = new Options();
        options.addOption("a", true, "a", "A");
        String[] arguments = new String[] { "--a=1" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--a=1" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgAndValueOption() {
        Options options = new Options();
        options.addOption("a", true, "a", "A");
        options.addOption("b", true, "b", "B");
        String[] arguments = new String[] { "--a=1", "--b=2" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--a=1", "--b=2" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArgAndValueAndMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "a", "A");
        options.addOption("b", true, "b", "B");
        options.addOption("c", true, "c", "C");
        String[] arguments = new String[] { "--a=1", "--b=2", "--c=3" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--a=1", "--b=2", "--c=3" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a", "A");
        String[] arguments = new String[] { "--a=1", "nonOption" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--a=1" };
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDashOption() {
        Options options = new Options();
        String[] arguments = new String[] { "--" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashOption() {
        Options options = new Options();
        String[] arguments = new String[] { "--" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDashOptionAfterArgs() {
        Options options = new Options();
        String[] arguments = new String[] { "arg1", "arg2", "--" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "arg1", "arg2", "--" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndDashOption() {
        Options options = new Options();
        String[] arguments = new String[] { "--", "nonOption" };
        PosixParser parser = new PosixParser();
        String[] expected = new String[] { "--" };
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenSimple() {
        Options options = new Options();
        String[] expected = new String[] { "-a", "-b" };
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-a-b", false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        String[] expected = new String[] { "--a", "--b" };
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--a--b", false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithValue() {
        Options options = new Options();
        String[] expected = new String[] { "--a=1", "--b=2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--a=1--b=2", false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        String[] expected = new String[] { "--a=1", "--" };
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--a=1--", true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // valid option
        assertSame(options.getOption("a"), parser.processOptionToken("-a", false, options));

        // invalid option
        assertNull(parser.processOptionToken("nonOption", false, options));

        // valid option with stopAtNonOption
        Options optionsStopAtNonOption = new Options();
        assertSame(options.getOption("a"), parser.processOptionToken("-a", true, options));
    }

    @Test
    public void testProcessOptionTokenWithLongOption() {
        Options options = new Options();
        options.addOption("x", false, "x", "X");
        PosixParser parser = new PosixParser();

        // valid long option
        assertSame(options.getOption("x"), parser.processOptionToken("--x", false, options));

        // valid long option with argument
        assertSame(options.getOption("x"), parser.processOptionToken("--x=value", false, options));

        // invalid option
        assertNull(parser.processOptionToken("nonOption", false, options));

        // valid option with stopAtNonOption
        Options optionsStopAtNonOption = new Options();
        assertSame(options.getOption("x"), parser.processOptionToken("--x", true, options));
    }

}