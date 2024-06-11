package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyArgs() {
        Options options = new Options();
        String[] args = {};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testSingleArg() {
        Options options = new Options();
        String[] args = {"arg1"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("arg1", result[0]);
    }

    @Test
    public void testDoubleDash() {
        Options options = new Options();
        String[] args = {"--arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("--arg", result[0]);
    }

    @Test
    public void testSingleHyphen() {
        Options options = new Options();
        String[] args = {"-"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption("opt", true, "Description");
        String[] args = {"-opt"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("-opt", result[0]);
        assertEquals("true", result[1]);
    }

    @Test
    public void testOptionWithArg() {
        Options options = new Options();
        options.addOption("opt", true, "Description");
        options.addOption("optarg", true, "Description");
        String[] args = {"-opt", "arg1"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("-opt", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longopt", true, "Description");
        String[] args = {"--longopt"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("--longopt", result[0]);
    }

    @Test
    public void testLongOptionWithArg() {
        Options options = new Options();
        options.addOption("longopt", true, "Description");
        options.addOption("longoptarg", true, "Description");
        String[] args = {"--longopt", "arg1"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("--longopt", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testBurstShortOption() {
        Options options = new Options();
        options.addOption("opt", true, "Description");
        String[] args = {"-opta", "arg1", "-optb", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("-opta", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-optb", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testBurstLongOption() {
        Options options = newOptions();
        options.addOption("longopt", true, "Description");
        String[] args = {"--longopta", "arg1", "--longoptb", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("--longopta", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--longoptb", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("opt", true, "Description");
        String[] args = {"-opt", "arg1", "non-option"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals("-opt", result[0]);
        assertEquals(" arg1", result[1]);
        assertEquals("non-option", result[2]);
    }

    @Test
    public void testSpecialTokens() {
        Options options = new Options();
        String[] args = {"--foo=bar", "--bar=baz"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("--foo=bar", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("--bar=baz", result[2]);
        assertEquals("baz", result[3]);
    }

    @Test
    public void testEmptyOptionArgument() {
        Options options = new Options();
        options.addOption("opt", true, "Description");
        String[] args = {"-opt", ""};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals("-opt", result[0]);
        assertNull(result[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        Options options = new Options();
        String[] args = {"-invalid"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // This test should fail with an IllegalArgumentException
    }
}