package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with three arguments");
    }

    @Test
    public void testFlattenSimple() {
        String[] args = {"-a", "--b=value", "-c", "arg1", "arg2", "arg3"};
        String[] expected = {"-a", "--b", "value", "-c", "arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"-a", "--non-option", "-c", "arg1", "arg2", "arg3"};
        String[] expected = {"-a", "--non-option", "-c", "arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyArgs() {
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleArg() {
        String[] args = {"arg"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        String[] args = {"--"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        String[] args = {"-x"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-x"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"--long-option"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        String[] args = {"--long-option=value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testBurstToken() {
        String token = "--long-option-with-three-arguments";
        String[] expected = {"-l", "--option", "with", "three", "arguments"};
        List<String> result = parser.burstToken(token, false);
        assertArrayEquals(expected.clone(), result.toArray(new String[result.size()]));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--long-option-with-three-arguments-non-option";
        String[] expected = {"--long-option-with-three-arguments", "--non-option"};
        List<String> result = parser.burstToken(token, true);
        assertArrayEquals(expected, result.toArray(new String[result.size()]));
    }

    @Test
    public void testProcessOptionToken() {
        String token = "--long-option";
        String expectedOptionId = "long-option";
        Option option = new Option(expectedOptionId, true, "Option long-option");
        parser.processOptionToken(token, false);
        assertSame(option, parser.currentOption);
    }

    @Test(expected = AssertionError.class)
    public void testProcessOptionTokenWithInvalidOption() {
        String token = "--invalid-option";
        parser.processOptionToken(token, false);
    }

    @Test
    public void testProcess() {
        String token = "arg1";
        parser.process(token);
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.size(), 2);
        parser.process(token);
        assertEquals(parser.tokens.size(), 3);
        assertEquals(parser.tokens.get(1), "arg1");
    }

    @Test
    public void testProcessWithOption() {
        String token = "-a";
        parser.process(token);
        assertEquals(parser.currentOption, options.getOption("a"));
    }

    @Test
    public void testProcessWithInvalidOption() {
        String token = "-x";
        parser.process(token);
        assertNull(parser.currentOption);
    }

    @Test
    public void testGobble() {
        String[] args = {"--long-option", "arg1", "arg2", "arg3"};
        parser.flatten(options, args, true);
        assertEquals(parser.tokens.size(), 5);
        assertEquals(parser.tokens.get(1), "--long-option");
        assertEquals(parser.tokens.get(2), "arg1");
        assertEquals(parser.tokens.get(3), "arg2");
        assertEquals(parser.tokens.get(4), "arg3");
        parser.gobble(Arrays.asList(args).iterator());
        assertEquals(parser.tokens.size(), 6);
        assertEquals(parser.tokens.get(5), "--");
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (!Arrays.equals((Object[]) expected, (Object[]) actual)) {
            throw new AssertionError("Expected: " + Arrays.toString((Object[]) expected) + "\n" +
                    "  but was: " + Arrays.toString((Object[]) actual));
        }
    }
}