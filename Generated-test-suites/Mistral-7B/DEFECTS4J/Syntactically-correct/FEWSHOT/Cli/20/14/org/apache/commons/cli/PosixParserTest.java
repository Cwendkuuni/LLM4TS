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
        String[] args = {"--foo", "-o", "--bar", "-c", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-o", "--bar", "-c", "value"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-o", "--bar", "-c", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "-o", "--bar", "-c", "value"}, result);
        assertEquals(10, result.length);
    }

    @Test
    public void testFlattenLongOptionValue() {
        Options options = new Options();
        String[] args = {"--foo=value1", "--bar=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo=value1", "--bar=value2"}, result);
    }

    @Test
    public void testFlattenLongOptionNoValue() {
        Options options = new Options();
        String[] args = {"--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testFlattenShortOption() {
        Options options = new Options();
        options.addOption("o", false, "option-o");
        String[] args = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-o"}, result);
        assertEquals(Option.BUILD_DEFAULT_HELP_MESSAGE, parser.currentOption);
    }

    @Test
    public void testFlattenShortOptionValue() {
        Options options = new Options();
        options.addOption("o", true, "option-o");
        String[] args = {"-o", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-o", "value"}, result);
        assertEquals("value", parser.currentOption.getValue());
    }

    @Test
    public void testFlattenShortOptionValueMultiple() {
        Options options = new Options();
        options.addOption("o", true, "option-o");
        String[] args = {"-o", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-o", "value1", "value2"}, result);
        String[] values = parser.currentOption.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        String token = "-o";
        parser.burstToken(token, false);
        assertEquals("-o", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenShortOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "-o";
        parser.options.addOption("o", true, "option-o");
        parser.burstToken(token, false);
        assertEquals("-o", parser.tokens.get(parser.tokens.size() - 2));
        assertEquals("option-o", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenLongOption() {
        PosixParser parser = new PosixParser();
        String token = "--option";
        parser.burstToken(token, false);
        assertEquals("--option", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenLongOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "--option=arg";
        parser.burstToken(token, false);
        assertEquals("--option", parser.tokens.get(parser.tokens.size() - 2));
        assertEquals("arg", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenLongOptionMultipleArgs() {
        PosixParser parser = new PosixParser();
        String token = "--option=arg1 arg2";
        parser.burstToken(token, false);
        assertEquals("--option", parser.tokens.get(parser.tokens.size() - 2));
        assertEquals("arg1 arg2", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "nonOption";
        parser.burstToken(token, true);
        assertNull(parser.currentOption);
        assertEquals("--nonOption", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.options.addOption("o", false, "option-o");
        parser.processOptionToken("-o", false);
        assertEquals("-o", parser.tokens.get(parser.tokens.size() - 1));
        assertSame(Option.BUILD_DEFAULT_HELP_MESSAGE, parser.currentOption);
    }

    @Test
    public void testProcessOptionTokenWithArg() {
        PosixParser parser = new PosixParser();
        parser.options.addOption("o", true, "option-o");
        parser.processOptionToken("-o", false);
        assertEquals("-o", parser.tokens.get(parser.tokens.size() - 1));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionTokenWithArgValue() {
        PosixParser parser = new PosixParser();
        parser.options.addOption("o", true, "option-o");
        parser.processOptionToken("--option-o=value", false);
        assertEquals("--option-o", parser.tokens.get(parser.tokens.size() - 1));
        assertEquals("value", parser.currentOption.getValue());
    }

    @Test
    public void testProcessOptionTokenMultipleArgs() {
        PosixParser parser = new PosixParser();
        parser.options.addOption("o", true, "option-o");
        parser.processOptionToken("--option-o=arg1 arg2", false);
        assertEquals("--option-o", parser.tokens.get(parser.tokens.size() - 1));
        String[] values = parser.currentOption.getValues();
        assertEquals(2, values.length);
        assertEquals("arg1", values[0]);
        assertEquals("arg2", values[1]);
    }
}