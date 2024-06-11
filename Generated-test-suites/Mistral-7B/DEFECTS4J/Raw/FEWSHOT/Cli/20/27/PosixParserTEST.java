package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        String[] expected = new String[0];

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimpleArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimpleOptions() {
        Options options = new Options();
        options.addOption("opt1", true, "o");

        String[] arguments = {"--opt1"};
        String[] expected = {"opt1"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptions() {
        Options options = new Options();
        options.addOption("longOpt1", true, "l1");
        options.addOption("longOpt2", false, "l2");

        String[] arguments = {"--longOpt1", "--longOpt2"};
        String[] expected = {"longOpt1", "longOpt2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionsWithValues() {
        Options options = new Options();
        options.addOption("longOpt1", true, "l1");
        options.addOption("longOpt2", true, "l2");

        String[] arguments = {"--longOpt1=val1", "--longOpt2=val2"};
        String[] expected = {"longOpt1=val1", "longOpt2=val2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionsWithValuesAndOtherArguments() {
        Options options = new Options();
        options.addOption("longOpt1", true, "l1");
        options.addOption("longOpt2", true, "l2");

        String[] arguments = {"--longOpt1=val1", "--longOpt2=val2", "arg3"};
        String[] expected = {"longOpt1=val1", "longOpt2=val2", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenShortOptions() {
        Options options = new Options();
        options.addOption("o", true);

        String[] arguments = {"-o"};
        String[] expected = {"o"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenShortOptionsWithValues() {
        Options options = new Options();
        options.addOption("o", true);

        String[] arguments = {"-o=val"};
        String[] expected = {"o=val"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenShortOptionsWithOtherArguments() {
        Options options = new Options();
        options.addOption("o", true);

        String[] arguments = {"-o=val", "arg2"};
        String[] expected = {"o=val", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstedTokens() {
        Options options = new Options();
        options.addOption("opt", true);
        options.addOption("opt2", true);

        String[] arguments = {"-opt--opt2"};
        String[] expected = {"opt", "opt2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstedTokensWithValues() {
        Options options = new Options();
        options.addOption("opt", true);
        options.addOption("opt2", true);

        String[] arguments = {"-opt--opt2=val2"};
        String[] expected = {"opt", "opt2=val2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("opt", true);

        String[] arguments = {"--arg", "-opt", "arg2"};
        String[] expected = {"--arg", "opt", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        String token = "--opt1--opt2=val";
        String[] expected = {"opt1", "opt2", "val"};

        PosixParser parser = new PosixParser();
        List<String> result = parser.burstToken(token, false);

        List<String> expectedList = Arrays.asList(expected);
        assertTrue(expectedList.containsAll(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenNull() {
        PosixParser parser = new PosixParser();
        parser.burstToken(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenEmpty() {
        PosixParser parser = new PosixParser();
        parser.burstToken("", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenShort() {
        PosixParser parser = new PosixParser();
        parser.burstToken("-o", false);
    }
}