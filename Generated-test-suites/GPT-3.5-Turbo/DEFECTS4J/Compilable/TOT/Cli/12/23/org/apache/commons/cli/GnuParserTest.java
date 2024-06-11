package org.apache.commons.cli;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class GnuParserTest {

    private GnuParser parser;

    @Before
    public void setup() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten_existingOption_returnsFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = { "-a" };

        String[] expectedOutput = { "-a" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_nonOptionArguments_returnNonOptionArguments() {
        Options options = new Options();

        String[] arguments = { "arg1", "arg2", "arg3" };

        String[] expectedOutput = { "arg1", "arg2", "arg3" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_stopAtNonOptionFlag_stopFlatteningAtNonOptionArgument() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = { "-a", "nonOptionArg1", "nonOptionArg2" };

        String[] expectedOutput = { "-a", "nonOptionArg1", "nonOptionArg2" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_doubleDashFlag_addDoubleDashToTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = { "--" };

        String[] expectedOutput = { "--" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_hyphenOnlyFlag_addHyphenToTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = { "-" };

        String[] expectedOutput = { "-" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_optionWithLeadingHyphenFlag_addOptionToTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = { "-aarg1" };

        String[] expectedOutput = { "-aarg1" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_optionWithLeadingHyphenAndEqualsFlag_splitOptionAndValueToTokens() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = { "-a=arg1" };

        String[] expectedOutput = { "-a", "arg1" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_stopAtNonOptionFlag_trueAllRemainingArgumentsAreAdded() {
        Options options = new Options();

        String[] arguments = { "arg1", "arg2" };

        String[] expectedOutput = { "arg1", "arg2" };
        String[] actualOutput = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_stopAtNonOptionFlag_falseAllRemainingArgumentsAreFlattened() {
        Options options = new Options();

        String[] arguments = { "arg1", "arg2" };

        String[] expectedOutput = { "arg1", "arg2" };
        String[] actualOutput = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

}