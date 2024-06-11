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
    public void testFlattenEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "--bar=value", "--baz", "arg1", "--qux"};
        Options options = new Options();
        Option optionFoo = Option.builder("foo").build();
        Option optionBar = Option.builder("bar").hasArg().build();
        Option optionBaz = Option.builder("baz").build();
        options.addOption(optionFoo);
        options.addOption(optionBar);
        options.addOption(optionBaz);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals("value", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("--baz", result[4]);
        assertEquals("--qux", result[5]);
    }

    @Test
    public void testFlattenWithLongOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--long-option-with-value=value", "arg1", "--another-long-option"};
        Options options = new Options();
        Option optionLongOption = Option.builder("longOption").hasArg().build();
        options.addOption(optionLongOption);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("--long-option-with-value", result[0]);
        assertEquals("value", result[1]);
        assertEquals("--another-long-option", result[2]);
    }

    @Test
    public void testFlattenWithShortOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "-b", "-a", "arg1", "-c", "-x"};
        Options options = new Options();
        Option optionF = Option.builder("f").build();
        Option optionB = Option.builder("b").build();
        Option optionA = Option.builder("a").build();
        Option optionC = Option.builder("c").hasArg().build();
        Option optionX = Option.builder("x").build();
        options.addOption(optionF);
        options.addOption(optionB);
        options.addOption(optionA);
        options.addOption(optionC);
        options.addOption(optionX);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("-x", result[5]);
    }

    @Test
    public void testFlattenWithMixedShortAndLongOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOption", "-b", "--shortOptionWithValue=value", "-a", "--longOptionWithArg=arg"};
        Options options = new Options();
        Option optionLongOption = Option.builder("longOption").build();
        Option optionShortOption = Option.builder("s").hasArg().build();
        options.addOption(optionLongOption);
        options.addOption(optionShortOption);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("--shortOptionWithValue", result[2]);
        assertEquals("value", result[3]);
        assertEquals("--longOptionWithArg", result[4]);
    }

    @Test
    public void testFlattenWithValueAfterShortOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "arg1", "-b=value", "arg2"};
        Options options = new Options();
        Option optionA = Option.builder("a").build();
        Option optionB = Option.builder("b").hasArg().build();
        options.addOption(optionA);
        options.addOption(optionB);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "arg1", "bar"};
        Options options = new Options();
        Option optionFoo = Option.builder("foo").build();
        options.addOption(optionFoo);

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenWithEmptyValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--qux", "--quux=", "--quuz"};
        Options options = new Options();
        Option optionQux = Option.builder("qux").build();
        Option optionQuux = Option.builder("quux").hasArg().build();
        Option optionQuuz = Option.builder("quuz").build();
        options.addOption(optionQux);
        options.addOption(optionQuux);
        options.addOption(optionQuuz);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("--qux", result[0]);
        assertEquals("", result[1]);
        assertEquals("--quuz", result[2]);
    }

    @Test
    public void testBurstTokenEmpty() {
        PosixParser parser = new PosixParser();
        String token = "";

        parser.burstToken(token, false);

        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenSingleCharacter() {
        PosixParser parser = new PosixParser();
        String token = "a";

        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenSingleCharacterWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "a";

        parser.burstToken(token, true);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("a", tokens.get(0));
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenShortOption() {
        PosixParser parser = new PosixParser();
        String token = "-a";

        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenShortOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "-a";
        Options options = new Options();
        Option optionA = Option.builder("a").hasArg().build();
        options.addOption(optionA);

        parser.init();
        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertFalse(parser.eatTheRest);
        assertSame(optionA, parser.currentOption);
    }

    @Test
    public void testBurstTokenShortOptionWithArgValue() {
        PosixParser parser = new PosixParser();
        String token = "-a=value";

        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("value", tokens.get(1));
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenShortOptionWithArgValueAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-a=value";

        parser.burstToken(token, true);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("-a=value", tokens.get(0));
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenLongOption() {
        PosixParser parser = new PosixParser();
        String token = "--longOption";

        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("--longOption", tokens.get(0));
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenLongOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "--longOption=value";

        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("--longOption", tokens.get(0));
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenLongOptionWithArgValueAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--longOption=value";

        parser.burstToken(token, true);

        List tokens = parser.tokens;
        assertEquals(1, tokens.size());
        assertEquals("--longOption=value", tokens.get(0));
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenLongOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        String token = "--longOptionWithArg arg1 arg2 arg3";

        parser.burstToken(token, false);

        List tokens = parser.tokens;
        assertEquals(3, tokens.size());
        assertEquals("--longOptionWithArg", tokens.get(0));
        assertEquals("arg1", tokens.get(1));
        assertEquals("arg2", tokens.get(2));
        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenLongOptionWithMultipleArgsAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--longOptionWithArg arg1 arg2 arg3";

        parser.burstToken(token, true);

        List tokens = parser.tokens;
        assertEquals(2, tokens.size());
        assertEquals("--longOptionWithArg arg1 arg2 arg3", tokens.get(0));
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }
}