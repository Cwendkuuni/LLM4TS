package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenOptionsOnly() {
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option" };
        String[] expected = { "--option" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option", "value" };
        String[] expected = { "--option", "value" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenMultiValueOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option", "value1", "--option", "value2" };
        String[] expected = { "--option", "value1", "--option", "value2" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "option value");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option=value" };
        String[] expected = { "--option", "value" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("o", "option", true, "option value");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option=value1", "--option=value2" };
        String[] expected = { "--option", "value1", "--option", "value2" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenOptionWithLongArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "option value");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option=value that is very very long" };
        String[] expected = { "--option", "value that is very very long" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = { "value1", "value2" };
        String[] expected = { "value1", "value2" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "option value");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option", "--", "value1", "value2" };
        String[] expected = { "--option", "--", "value1", "value2" };
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "option value");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option", "option", "value1", "value2" };
        String[] expected = { "--option", "option", "value1", "value2" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testFlattenWithLongNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "option value");
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option", "non-option that is very very long" };
        String[] expected = { "--option", "non-option that is very very long" };
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, null, false);
    }

    @Test
    public void testFlattenNullOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = { "--option", "value" };
        assertNull(parser.flatten(null, arguments, false));
    }

    @Test
    public void testBurstTokenEmpty() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("", false);
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        List tokens = parser.burstToken("o", false);
        assertEquals(Arrays.asList("-o"), tokens);
    }

    @Test
    public void testBurstTokenSingleOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        List tokens = parser.burstToken("o=value", false);
        assertEquals(Arrays.asList("-o", "value"), tokens);
    }

    @Test
    public void testBurstTokenMultiOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o1", "option1", true, "description1");
        options.addOption("o2", "option2", true, "description2");
        List tokens = parser.burstToken("o1o2", false);
        assertEquals(Arrays.asList("-o1", "-o2"), tokens);
    }

    @Test
    public void testBurstTokenOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        List tokens = parser.burstToken("o=value", false);
        assertEquals(Arrays.asList("-o", "value"), tokens);
    }

    @Test
    public void testBurstTokenLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longoption", "description", true, "long option description");
        List tokens = parser.burstToken("longoption=value", false);
        assertEquals(Arrays.asList("-longoption", "value"), tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("--", false);
        assertEquals(Arrays.asList("--"), tokens);
    }

    @Test
    public void testBurstTokenWithNonOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("non-option", false);
        assertFalse(tokens.isEmpty());
        assertEquals("non-option", tokens.get(0));
    }

    @Test
    public void testBurstTokenLongNonOption() {
        PosixParser parser = new PosixParser();
        List tokens = parser.burstToken("non-option that is very very long", false);
        assertFalse(tokens.isEmpty());
        assertEquals("non-option that is very very long", tokens.get(0));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        List tokens = parser.getTokens();
        assertEquals(Collections.singletonList("-"), tokens);
    }

    @Test
    public void testProcessSingleHyphenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        parser.setEatTheRest(true);
        parser.process("-");
        List tokens = parser.getTokens();
        assertEquals(Arrays.asList("-", "--"), tokens);
    }

    @Test
    public void testProcessSingleArgument() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        List tokens = parser.getTokens();
        assertEquals(Collections.singletonList("value"), tokens);
    }

    @Test
    public void testProcessSingleArgumentWithTwoArguments() {
        PosixParser parser = new PosixParser();
        parser.process("value1 value2");
        List tokens = parser.getTokens();
        assertEquals(Arrays.asList("value1", "value2"), tokens);
    }

    @Test
    public void testProcessSingleArgumentWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        parser.setEatTheRest(true);
        parser.process("--");
        List tokens = parser.getTokens();
        assertEquals(Arrays.asList("value", "--"), tokens);
    }

    @Test
    public void testProcessSingleArgumentWithLongArgument() {
        PosixParser parser = new PosixParser();
        parser.process("value that is very very long");
        List tokens = parser.getTokens();
        assertEquals(Collections.singletonList("value that is very very long"), tokens);
    }

    @Test
    public void testProcessSingleArgumentWithLongArgumentAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.process("value that is very very long");
        parser.setEatTheRest(true);
        parser.process("--");
        List tokens = parser.getTokens();
        assertEquals(Arrays.asList("value that is very very long", "--"), tokens);
    }

    @Test
    public void testProcessCurrentOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "description");
        parser.setOptions(options);
        parser.process("o");
        Option currentOption = parser.getCurrentOption();
        assertEquals(options.getOption("o"), currentOption);
    }

    @Test
    public void testProcessCurrentOptionWithNoOption() {
        PosixParser parser = new PosixParser();
        parser.process("no_option");
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessCurrentOptionWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longoption", "description", true, "long option description");
        parser.setOptions(options);
        parser.process("longoption");
        Option currentOption = parser.getCurrentOption();
        assertEquals(options.getOption("longoption"), currentOption);
    }

    @Test
    public void testProcessCurrentOptionWithLongOptionAndArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longoption", "description", true, "long option description");
        parser.setOptions(options);
        parser.process("longoption=value");
        Option currentOption = parser.getCurrentOption();
        assertEquals(options.getOption("longoption"), currentOption);
    }

    @Test
    public void testReset() {
        PosixParser parser = new PosixParser();
        List tokens = parser.getTokens();
        assertEquals(Collections.emptyList(), tokens);
        parser.process("option");
        tokens = parser.getTokens();
        assertEquals(Collections.singletonList("option"), tokens);
        parser.init();
        tokens = parser.getTokens();
        assertEquals(Collections.emptyList(), tokens);
    }
}