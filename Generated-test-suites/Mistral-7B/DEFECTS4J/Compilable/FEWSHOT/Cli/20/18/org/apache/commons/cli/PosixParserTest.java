package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with argument");
        options.addOption("c", true, "Option C with argument");
        parser = new PosixParser();
    }

    @Test
    public void testEmpty() {
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testSingleToken() {
        String[] args = {"arg1"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("arg1", result[0]);
    }

    @Test
    public void testSingleOption() {
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testSingleLongOption() {
        String[] args = {"--a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("--a", result[0]);
    }

    @Test
    public void testOptionWithArgument() {
        String[] args = {"-b", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("-b", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testOptionWithMultipleArguments() {
        String[] args = {"-c", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("-c", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testOptionWithLongArgument() {
        String[] args = {"--c", "long arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("--c", result[0]);
        assertEquals("long arg", result[1]);
    }

    @Test
    public void testMultipleOptions() {
        String[] args = {"-a", "-b", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg", result[2]);
    }

    @Test
    public void testLongOptionWithArgument() {
        String[] args = {"--a", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("--a", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testLongOptionWithMultipleArguments() {
        String[] args = {"--c", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("--c", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"arg1", "-b", "arg2", "-c"};
        String[] result = parser.flatten(options, args, true);
        assertEquals("arg1", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testBursting() {
        String[] args = {"-abc"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testBurstingWithLongOption() {
        String[] args = {"--abc"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("--a", result[0]);
        assertEquals("--b", result[1]);
        assertEquals("--c", result[2]);
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        String[] args = {"--abc", "arg"};
        String[] result = parser.flatten(options, args, true);
        assertEquals("--abc", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testBurstingWithLongOptionAndArguments() {
        String[] args = {"--abc", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals("--a", result[0]);
        assertEquals("--b", result[1]);
        assertEquals("--c", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testBurstingWithStopAtNonOptionAndArguments() {
        String[] args = {"--abc", "arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, true);
        assertEquals("--abc arg1 arg2 arg3", Arrays.toString(result));
    }
}