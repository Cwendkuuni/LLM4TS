package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] arguments = { "--foo", "-a", "-b", "--bar=123", "--baz" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("--");
        expected.add("b");
        expected.add("--bar");
        expected.add("123");
        expected.add("--");
        expected.add("baz");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = { "--foo", "-a", "-b", "--bar=123", "--baz" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("a");
        expected.add("b");
        expected.add("--bar=123");
        expected.add("--baz");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        String[] arguments = { "--foo", "-a", "value" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("value");
        expected.add("--");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenOptionWithArgs() {
        Options options = new Options();
        Option option = new Option("a", "args", 1, "default");
        options.addOption(option);
        String[] arguments = { "--foo", "-a", "arg1", "arg2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("arg1");
        expected.add("arg2");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "arg1", true, "default1");
        Option option2 = new Option("b", "arg2", true, "default2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = { "--foo", "-a", "value1", "-b", "value2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("value1");
        expected.add("--");
        expected.add("b");
        expected.add("value2");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenShortOption() {
        Options options = new Options();
        String[] arguments = { "--foo", "-a", "-b", "--bar=123" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("a");
        expected.add("--");
        expected.add("b");
        expected.add("--bar");
        expected.add("123");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenSpecialToken() {
        Options options = new Options();
        String[] arguments = { "--foo", "--", "arg1", "arg2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenStopAtNonOptionSpecialToken() {
        Options options = new Options();
        String[] arguments = { "--foo", "--", "arg1", "arg2" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("arg1");
        expected.add("arg2");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, null, false);
        assertNull(result);
    }

    @Test
    public void testFlattenStopAtNonOptionNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, null, true);
        assertNull(result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String token = "-";
        parser.processSingleHyphen(token);
        String[] expected = { "-" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "-a";
        parser.processOptionToken(token, false);
        String[] expected = { "-a" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        parser.init();
        parser.processOptionToken(token, true);
        String[] tokens = (String[]) parser.getTokens().toArray(new String[0]);
        assertEquals(2, tokens.length);
        assertSame(tokens[0], "--");
        assertEquals(tokens[1], "a");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String value = "value";
        parser.process(value);
        String[] expected = { "--", value };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "-abcd";
        parser.burstToken(token, false);
        String[] expected = { "-a", "-b", "-c", "-d" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-abcd";
        parser.burstToken(token, true);
        String[] expected = { "--", "abcd" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testBurstTokenWithOption() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "-aarg";
        parser.burstToken(token, false);
        String[] expected = { "-a", "arg" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        assertEquals(option, parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenWithOptionArg() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "-aarg1arg2";
        parser.burstToken(token, false);
        String[] expected = { "-a", "arg1", "arg2" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        assertEquals(option, parser.getCurrentOption());
        assertNull(parser.getCurrentOptionValue());
    }

    @Test
    public void testBurstTokenWithOptionShortArg() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "-aarg1";
        parser.burstToken(token, false);
        String[] expected = { "-a", "arg1" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        assertEquals(option, parser.getCurrentOption());
        assertEquals("arg1", parser.getCurrentOptionValue());
    }

    @Test
    public void testBurstTokenWithOptionLongArg() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "--a=arg1arg2";
        parser.burstToken(token, false);
        String[] expected = { "--a", "arg1arg2" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        assertEquals(option, parser.getCurrentOption());
        assertEquals("arg1arg2", parser.getCurrentOptionValue());
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "-abcd";
        parser.burstToken(token, true);
        String[] expected = { "--", "abcd" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionOption() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "--a";
        parser.burstToken(token, true);
        String[] expected = { "--a" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        assertNull(parser.getCurrentOption());
        assertNull(parser.getCurrentOptionValue());
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionLongOption() {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "default");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String token = "--a=arg";
        parser.burstToken(token, true);
        String[] expected = { "--a=arg" };
        assertArrayEquals(expected, parser.getTokens().toArray(new String[0]));
        assertNull(parser.getCurrentOption());
        assertNull(parser.getCurrentOptionValue());
    }
}