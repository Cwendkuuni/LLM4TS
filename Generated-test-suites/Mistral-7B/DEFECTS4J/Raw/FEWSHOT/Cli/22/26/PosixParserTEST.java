package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"--a"};
        String[] expected = {"--a"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleLongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"--a", "value"};
        String[] expected = {"--a", "value"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"--a", "value"};
        String[] expected = {"--a", "value"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArg_SingleToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "-b", "arg2", "token"};
        String[] expected = {"-a", "arg1", "-b", "arg2", "token"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArg_EmptyValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--b", ""};
        String[] expected = {"-a", "arg1", "--b"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArg_NoValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--b"};
        String[] expected = {"-a", "arg1", "--b"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};

        String[] actual = parser.flatten(options, args, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "nonOption"};
        String[] expected = {"-a", "arg1"};

        String[] actual = parser.flatten(options, args, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken_ValidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";

        parser.burstToken(token, false);

        List expected = Arrays.asList("-a");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(expected.get(0), parser.tokens.get(0));
    }

    @Test
    public void testBurstToken_InvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "invalid";

        parser.burstToken(token, true);

        List expected = Arrays.asList("--", "invalid");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(expected.get(0), parser.tokens.get(0));
        assertEquals(expected.get(1), parser.tokens.get(1));
    }

    @Test(expected = AssertionError.class)
    public void testBurstToken_InvalidOption_NoStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "invalid";

        parser.burstToken(token, false);

        List expected = Arrays.asList("-invalid");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(expected.get(0), parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "-a";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        List expected = Arrays.asList("-a");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(expected.get(0), parser.tokens.get(0));
    }

    @Test
    public void testProcessNonOptionToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String token = "nonOption";

        parser.processNonOptionToken(token);

        List expected = Arrays.asList("--", "nonOption");
        assertEquals(expected.size(), parser.tokens.size());
        assertEquals(expected.get(0), parser.tokens.get(0));
        assertEquals(expected.get(1), parser.tokens.get(1));
    }
}