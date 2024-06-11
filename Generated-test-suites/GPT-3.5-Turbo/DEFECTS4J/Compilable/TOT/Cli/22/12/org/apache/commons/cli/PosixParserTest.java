package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions_ReturnsInputArguments() {
        Options options = new Options();

        String[] inputArguments = {"arg1", "arg2"};
        String[] expectedOutput = {"arg1", "arg2"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsFlattenedTokensStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option description");

        String[] inputArguments = {"arg1", "-a", "arg2"};
        String[] expectedOutput = {"arg1", "-a", "arg2"};

        String[] actualOutput = parser.flatten(options, inputArguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_StopAtNonOptionAndNonOptionPresent_ReturnsFlattenedTokensWithNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option description");

        String[] inputArguments = {"arg1", "-a", "nonOption", "arg2"};
        String[] expectedOutput = {"arg1", "-a", "--", "nonOption", "arg2"};

        String[] actualOutput = parser.flatten(options, inputArguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue_ReturnsFlattenedTokensWithOption() {
        Options options = new Options();
        options.addOption("a", false, "Option description");
        options.addOption("b", false, "Option description");

        String[] inputArguments = {"--a"};
        String[] expectedOutput = {"--a"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_LongOptionWithValue_ReturnsFlattenedTokensWithOptionAndValue() {
        Options options = new Options();
        options.addOption("a", true, "Option description");
        options.addOption("b", true, "Option description");

        String[] inputArguments = {"--a=value"};
        String[] expectedOutput = {"--a", "value"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_LongOptionWithValue_InvalidOption_ReturnsFlattenedTokensWithNonOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option description");

        String[] inputArguments = {"--invalid=value"};
        String[] expectedOutput = {"--", "invalid=value"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsFlattenedTokensWithHyphen() {
        Options options = new Options();

        String[] inputArguments = {"-"};
        String[] expectedOutput = {"-"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_ShortOption_ReturnsFlattenedTokensWithOption() {
        Options options = new Options();
        options.addOption("a", false, "Option description");
        options.addOption("b", false, "Option description");

        String[] inputArguments = {"-a"};
        String[] expectedOutput = {"-a"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_ShortOptionWithValue_ReturnsFlattenedTokensWithOptionAndValue() {
        Options options = new Options();
        options.addOption("a", true, "Option description");
        options.addOption("b", true, "Option description");

        String[] inputArguments = {"-a", "value"};
        String[] expectedOutput = {"-a", "value"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_BurstableShortOptionWithValue_ReturnsFlattenedTokensWithOptionAndValue() {
        Options options = new Options();
        options.addOption("a", true, "Option description");
        options.addOption("b", true, "Option description");

        String[] inputArguments = {"-abvalue"};
        String[] expectedOutput = {"-a", "bvalue"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_BurstableShortOptionWithValueAndRemainingTokens_ReturnsFlattenedTokensWithOptionAndValueAndRemainingTokens() {
        Options options = new Options();
        options.addOption("a", true, "Option description");
        options.addOption("b", true, "Option description");

        String[] inputArguments = {"-abvalue", "remaining"};
        String[] expectedOutput = {"-a", "bvalue", "remaining"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_BurstableShortOption_InvalidOption_ReturnsFlattenedTokensWithNonOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option description");

        String[] inputArguments = {"-invalid"};
        String[] expectedOutput = {"-invalid"};

        String[] actualOutput = parser.flatten(options, inputArguments, false);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_BurstableShortOption_InvalidOptionAndStopAtNonOption_ReturnsFlattenedTokensWithNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option description");

        String[] inputArguments = {"-invalid"};
        String[] expectedOutput = {"-invalid"};

        String[] actualOutput = parser.flatten(options, inputArguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFlatten_StopAtNonOptionAndNonOptionPresentWithOptionValue_ReturnsFlattenedTokensWithNonOptionAndOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option description");

        String[] inputArguments = {"arg1", "-a=value", "nonOption", "arg2"};
        String[] expectedOutput = {"arg1", "-a", "value", "--", "nonOption", "arg2"};

        String[] actualOutput = parser.flatten(options, inputArguments, true);

        assertArrayEquals(expectedOutput, actualOutput);
    }
}