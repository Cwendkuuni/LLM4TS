package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_EmptyOptionsArray_ReturnsEmptyArray() {
        String[] arguments = {};
        assertArrayEquals("Flatten should return an empty array if options are empty",
                new String[]{},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_NoOptions_ReturnsArgumentsArray() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        assertArrayEquals("Flatten should return the same arguments array if no options are provided",
                arguments,
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsTokensUntilNonOption() {
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};
        assertArrayEquals("Flatten should stop at the first non-option when stopAtNonOption is true",
                new String[]{"-a", "-b", "value"},
                parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_LongOptionWithValue_SplitsOptionAndValue() {
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"--optionA=value"};
        assertArrayEquals("Flatten should split a long option with value into two tokens",
                new String[]{"--optionA", "value"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_SingleHyphenOption_ReturnsHyphen() {
        String[] arguments = {"-"};
        assertArrayEquals("Flatten should return a single hyphen as is",
                new String[]{"-"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_TwoCharacterOptionWithNoArgs_AddsOptionToTokens() {
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a"};
        assertArrayEquals("Flatten should add a two-character option to tokens without further processing",
                new String[]{"-a"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_TwoCharacterOptionWithArgs_AddsOptionToTokens() {
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a"};
        assertArrayEquals("Flatten should add a two-character option with argument to tokens without further processing",
                new String[]{"-a"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_UnknownTwoCharacterOptionWithStopAtNonOption_ReturnsTokensUntilNonOption() {
        String[] arguments = {"-x", "value", "arg1", "arg2"};
        assertArrayEquals("Flatten should stop at the first non-option when option is not recognized and stopAtNonOption is true",
                new String[]{"-x", "value", "arg1", "arg2"},
                parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_UnknownTwoCharacterOptionWithoutStopAtNonOption_AddsTwoCharacterTokenToTokens() {
        String[] arguments = {"-x", "value", "arg1", "arg2"};
        assertArrayEquals("Flatten should add an unknown two-character option to tokens and continue processing",
                new String[]{"-x"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_BurstTokenWithValidOption_AddsOptionToTokens() {
        options.addOption("a", false, "Option A");

        String[] arguments = {"-ab"};
        assertArrayEquals("Flatten should add a valid option from a burst token to tokens",
                new String[]{"-a", "-b"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_BurstTokenWithValidOptionAndValue_AddsOptionAndValueToTokens() {
        options.addOption("a", true, "Option A");

        String[] arguments = {"-abvalue"};
        assertArrayEquals("Flatten should add a valid option and its value from a burst token to tokens",
                new String[]{"-a", "value"},
                parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlatten_BurstTokenWithUnknownOptionAndStopAtNonOption_ReturnsTokensUntilNonOption() {
        String[] arguments = {"-xabc", "value", "arg1", "arg2"};
        assertArrayEquals("Flatten should stop at the first non-option when option is not recognized in burst token and stopAtNonOption is true",
                new String[]{"-xabc", "value", "arg1", "arg2"},
                parser.flatten(options, arguments, true));
    }

    @Test
    public void testFlatten_BurstTokenWithUnknownOptionWithoutStopAtNonOption_AddsBurstTokenToTokens() {
        String[] arguments = {"-xabc", "value", "arg1", "arg2"};
        assertArrayEquals("Flatten should add an unknown option from a burst token to tokens",
                new String[]{"-xabc"},
                parser.flatten(options, arguments, false));
    }

}