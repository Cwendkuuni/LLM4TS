package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--foo", "bar"}, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        options.addOption("f", false, "A flag option");
        String[] args = {"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-f"}, result);
    }

    @Test
    public void testFlattenWithTwoCharacterOption() {
        Options options = new Options();
        options.addOption("oo", false, "An option with two letters");
        String[] args = {"--oo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--oo"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "A long option");
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenWithShortOptionAndValue() {
        Options options = new Options();
        options.addOption("s", true, "A short option with a value");
        String[] args = {"-s", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-s", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValue() {
        Options options = new Options();
        options.addOption("l", "value", true, "A long option with a value");
        String[] args = {"--l", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--l", "value"}, result);
    }

    @Test
    public void testFlattenWithOptionRequiringMultipleValues() {
        Options options = new Options();
        options.addOption("v", true, ValueConstants.MULTI_VALued, "An option requiring multiple values");
        String[] args = {"-v", "value1", "-v", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-v", "value1"}, result);
        assertEquals(new String[]{"-v", "value2"}, Arrays.copyOfRange(result, 1, result.length));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("s", false, "A flag option");
        String[] args = {"foo", "-s", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"foo", "-s", "bar"}, result);
    }

    @Test
    public void testFlattenWithOptionAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "An option with an argument");
        String[] args = {"-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-a", "arg"}, result);
    }

    @Test
    public void testFlattenWithBurstableToken() {
        Options options = new Options();
        options.addOption("foo", false, "An option with the id 'foo'");
        String[] args = {"--foo-bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--foo", "bar"}, result);
    }

    @Test
    public void testFlattenWithBurstableTokenAndValue() {
        Options options = new Options();
        options.addOption("foo", true, "An option with the id 'foo'");
        String[] args = {"--foo-bar", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--foo", "value"}, result);
    }

    @Test
    public void testFlattenWithBurstableTokenAndMultipleArguments() {
        Options options = new Options();
        options.addOption("foo", true, ValueConstants.UNLIMITED_VALUES, "An option with the id 'foo'");
        String[] args = {"--foo-bar1", "value1", "--foo-bar2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--foo", "value1"}, Arrays.copyOfRange(result, 0, result.length - 1));
        assertEquals(new String[]{"--foo", "value2"}, result);
    }

    @Test
    public void testFlattenWithBurstableTokenAndMultipleArgumentsAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, ValueConstants.UNLIMITED_VALUES, "An option with the id 'foo'");
        String[] args = {"--foo-bar1", "value1", "--", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"--foo", "value1"}, result);
        assertEquals(new String[]{"nonOption"}, Arrays.copyOfRange(result, 1, result.length));
    }

    @Test
    public void testBurstToken() {
        String token = "--foo-bar";
        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, false);
        assertEquals(Arrays.asList("--foo", "bar"), result);
    }

    @Test
    public void testBurstTokenWithValue() {
        String token = "--foo-bar=value";
        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, false);
        assertEquals(Arrays.asList("--foo", "value"), result);
    }

    @Test
    public void testBurstTokenWithMultipleValues() {
        String token = "--foo-bar1=value1 --foo-bar2=value2";
        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, false);
        List expected = Arrays.asList(Arrays.asList("--foo", "value1"), Arrays.asList("--foo", "value2"));
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--foo-bar1=value1 --";
        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, true);
        List expected = Arrays.asList(Arrays.asList("--foo", "value1"));
        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionAndMultipleValues() {
        String token = "--foo-bar1=value1 --foo-bar2=value2 --";
        PosixParser parser = new PosixParser();
        List result = parser.burstToken(token, true);
        List expected = Arrays.asList(Arrays.asList("--foo", "value1"));
        assertEquals(expected, result);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar"};
        parser.init();
        parser.process(args[0]);
        assertTrue(parser.tokens.isEmpty());
        parser.currentOption = new Option("foo", false, null, "Option foo");
        parser.init();
        parser.process(args[0]);
        List expected = Arrays.asList("--foo");
        assertEquals(expected, parser.tokens);

        parser.init();
        parser.eatTheRest = true;
        parser.process(args[0]);
        List expected2 = Arrays.asList("--", "--foo");
        assertEquals(expected2, parser.tokens);

        parser.init();
        parser.currentOption = null;
        parser.process("--foo=");
        parser.process("bar");
        List expected3 = Arrays.asList("--foo", "bar");
        assertEquals(expected3, parser.tokens);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        parser.init();
        parser.processSingleHyphen(args[0]);
        List expected = Arrays.asList("-");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("f", false, null, "Option f");
        PosixParser parser = new PosixParser();
        parser.init();
        parser.processOptionToken("f", false);
        assertSame(options.getOption("f"), parser.currentOption);
        List expected = Arrays.asList("-f");
        assertEquals(expected, parser.tokens);

        parser.init();
        parser.processOptionToken("--foo", true);
        assertNull(parser.currentOption);
        List expected2 = Arrays.asList("--foo");
        assertEquals(expected2, parser.tokens);

        parser.init();
        parser.eatTheRest = true;
        parser.processOptionToken("--foo", true);
        List expected3 = Arrays.asList("--foo");
        assertEquals(expected3, parser.tokens);
    }
}