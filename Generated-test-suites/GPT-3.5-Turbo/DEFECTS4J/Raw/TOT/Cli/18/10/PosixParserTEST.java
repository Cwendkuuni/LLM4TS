
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    // Test flatten method

    @Test
    public void testFlatten_StopAtNonOption_ReturnsExpectedResult() {
        String[] arguments = {"-a", "value1", "--", "-abc"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "value1", "--", "-abc"}, result);
    }

    @Test
    public void testFlatten_NonOptionArguments_ReturnsExpectedResult() {
        String[] arguments = {"-a", "value1", "--", "-abc"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value1", "--", "-abc"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsExpectedResult() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue_ReturnsExpectedResult() {
        String[] arguments = {"--long-option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithEquals_ReturnsExpectedResult() {
        String[] arguments = {"--long-option=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_ShortOption_ReturnsExpectedResult() {
        String[] arguments = {"-s"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-s"}, result);
    }

    @Test
    public void testFlatten_UnrecognizedShortOption_StopAtNonOption_ReturnsExpectedResult() {
        String[] arguments = {"-x"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--", "-x"}, result);
    }

    @Test
    public void testFlatten_UnrecognizedShortOption_ContinueParsing_ReturnsExpectedResult() {
        String[] arguments = {"-x"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-x"}, result);
    }

    @Test
    public void testFlatten_UnrecognizedOptionWithValue_StopAtNonOption_ReturnsExpectedResult() {
        String[] arguments = {"--unrecognized=value", "-abc"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--", "--unrecognized", "value", "-abc"}, result);
    }

    @Test
    public void testFlatten_UnrecognizedOptionWithValue_ContinueParsing_ReturnsExpectedResult() {
        String[] arguments = {"--unrecognized=value", "-abc"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--unrecognized", "value", "-abc"}, result);
    }

    // Test process method

    @Test
    public void testProcess_CurrentOptionHasArg_ReturnsExpectedResult() {
        String value = "value";

        parser.process(value);

        assertTrue(parser.tokens.contains(value));
    }

    @Test
    public void testProcess_CurrentOptionHasArgs_ReturnsExpectedResult() {
        String value = "value";

        parser.process(value);

        assertTrue(parser.tokens.contains(value));
    }

    @Test
    public void testProcess_NoCurrentOption_StopAtNonOption_ReturnsExpectedResult() {
        String value = "value";

        parser.process(value);

        assertTrue(parser.tokens.contains("--"));
        assertTrue(parser.tokens.contains(value));
    }

    // Test processSingleHyphen method

    @Test
    public void testProcessSingleHyphen_ReturnsExpectedResult() {
        String hyphen = "-";

        parser.processSingleHyphen(hyphen);

        assertTrue(parser.tokens.contains(hyphen));
    }

    // Test processOptionToken method

    @Test
    public void testProcessOptionToken_OptionExists_ReturnsExpectedResult() {
        String token = "-a";

        Option option = new Option("a", "option a");

        options.addOption(option);

        parser.processOptionToken(token, false);

        assertTrue(parser.tokens.contains(token));
    }

    @Test
    public void testProcessOptionToken_OptionDoesNotExist_StopAtNonOption_ReturnsExpectedResult() {
        String token = "-x";

        parser.processOptionToken(token, true);

        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessOptionToken_OptionDoesNotExist_ContinueParsing_ReturnsExpectedResult() {
        String token = "-x";

        parser.processOptionToken(token, false);

        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.contains(token));
    }

    // Test burstToken method

    @Test
    public void testBurstToken_OptionExistsWithArg_ReturnsExpectedResult() {
        String token = "-abc";

        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        Option optionC = new Option("c", "option c");

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        parser.burstToken(token, false);

        assertTrue(parser.tokens.contains("-a"));
        assertTrue(parser.tokens.contains("-b"));
        assertTrue(parser.tokens.contains("-c"));
    }

    @Test
    public void testBurstToken_OptionDoesNotExist_StopAtNonOption_ReturnsExpectedResult() {
        String token = "-xy";

        parser.burstToken(token, true);

        assertTrue(parser.tokens.contains("--"));
        assertTrue(parser.tokens.contains(token));
    }

    @Test
    public void testBurstToken_OptionDoesNotExist_ContinueParsing_ReturnsExpectedResult() {
        String token = "-xy";

        parser.burstToken(token, false);

        assertTrue(parser.tokens.contains(token));
    }

}

