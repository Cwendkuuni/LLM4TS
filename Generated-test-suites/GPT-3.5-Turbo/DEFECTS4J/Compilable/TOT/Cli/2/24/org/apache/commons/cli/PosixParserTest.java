package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private static PosixParser parser;
    private static Options options;
    private static String[] args;

    @BeforeClass
    public static void setUp() {
        parser = new PosixParser();
        options = new Options();
        args = new String[0];
    }

    @Test
    public void testFlatten_StopAtNonOptionAndSingleHyphen_ExpectedNonOptionAndSingleHyphen() {
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));
        args = new String[]{"-a", "-b", "-", "c"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-a", "-b", "--", "c"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionAndOptionWithValue_ExpectedStopAtNonOption() {
        options.addOption(new Option("a", "option a", true, "Option a value"));
        options.addOption(new Option("b", "option b"));
        args = new String[]{"-a", "value", "-b", "c"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-a", "value", "-b", "c"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionAndInvalidOption_ExpectedStopAtNonOption() {
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));
        args = new String[]{"-c", "-d", "e"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-c", "-d", "e"}, result);
    }

    @Test
    public void testFlatten_DoNotStopAtNonOptionAndOption_ExpectedOptionProcessed() {
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));
        args = new String[]{"-a", "-b", "c"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-a", "-b", "c"}, result);
    }

    @Test
    public void testFlatten_DoNotStopAtNonOptionAndInvalidOption_ExpectedAllOptionsProcessed() {
        options.addOption(new Option("a", "option a"));
        args = new String[]{"-c", "-d", "e"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-c", "-d", "e"}, result);
    }

    @Test
    public void testFlatten_SpecialTokenWithValue_ExpectedTokenProcessed() {
        options.addOption(new Option("a", "option a"));
        args = new String[]{"--special=value"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--special", "value"}, result);
    }

    @Test
    public void testFlatten_SpecialTokenWithoutValue_ExpectedTokenProcessed() {
        options.addOption(new Option("a", "option a"));
        args = new String[]{"--special"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--special"}, result);
    }

    @Test
    public void testFlatten_SpecialTokenWithMultipleValues_ExpectedTokenProcessed() {
        options.addOption(new Option("a", "option a"));
        args = new String[]{"--special=value1", "--special=value2"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--special", "value1", "--special", "value2"}, result);
    }

    @Test
    public void testFlatten_NoOptions_ExpectedAllTokensProcessed() {
        args = new String[]{"-a", "--special", "value"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-a", "--special", "value"}, result);
    }

}