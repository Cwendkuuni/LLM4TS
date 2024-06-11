package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a long argument");
        options.addOption("d", "default", "Option D with a default value");
    }

    /**
     * Test for flatten method with no arguments.
     */
    @Test
    public void testFlattenNoArguments() {
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with single argument.
     */
    @Test
    public void testFlattenSingleArgument() {
        String[] arguments = new String[]{"arg"};
        String[] expected = new String[]{"arg"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with long option.
     */
    @Test
    public void testFlattenLongOption() {
        String[] arguments = new String[]{"--option-a"};
        String[] expected = new String[]{"--option-a"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with long option with value.
     */
    @Test
    public void testFlattenLongOptionWithValue() {
        String[] arguments = new String[]{"--option-a", "value"};
        String[] expected = new String[]{"--option-a", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with multiple long options.
     */
    @Test
    public void testFlattenMultipleLongOptions() {
        String[] arguments = new String[]{"--option-a", "--option-b"};
        String[] expected = new String[]{"--option-a", "--option-b"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with long option and value and other arguments.
     */
    @Test
    public void testFlattenLongOptionWithValueAndOtherArguments() {
        String[] arguments = new String[]{"--option-a", "value", "arg1", "arg2"};
        String[] expected = new String[]{"--option-a", "value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, Arrays.copyOf(result, 2));
    }

    /**
     * Test for flatten method with short option.
     */
    @Test
    public void testFlattenShortOption() {
        String[] arguments = new String[]{"-a"};
        String[] expected = new String[]{"-a"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with short option with argument.
     */
    @Test
    public void testFlattenShortOptionWithArgument() {
        String[] arguments = new String[]{"-a", "arg"};
        String[] expected = new String[]{"-a", "arg"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with multiple short options.
     */
    @Test
    public void testFlattenMultipleShortOptions() {
        String[] arguments = new String[]{"-a", "-b"};
        String[] expected = new String[]{"-a", "-b"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for flatten method with short option, long option, and other arguments.
     */
    @Test
    public void testFlattenShortOptionLongOptionAndOtherArguments() {
        String[] arguments = new String[]{"-a", "--option-b", "arg1", "arg2"};
        String[] expected = new String[]{"-a"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, Arrays.copyOf(result, 1));
    }

    /**
     * Test for flatten method with multiple short options and values.
     */
    @Test
    public void testFlattenMultipleShortOptionsWithValues() {
        String[] arguments = new String[]{"-a", "--option-b=value", "arg1", "arg2"};
        String[] expected = new String[]{"-a", "--option-b=value"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, Arrays.copyOf(result, 2));
    }

    /**
     * Test for flatten method with stopAtNonOption.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = new String[]{"arg1", "--option-a", "arg2"};
        String[] expected = new String[]{"arg1", "--option-a"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with short option.
     */
    @Test
    public void testBurstTokenShortOption() {
        String token = "-a";
        String[] expected = new String[]{"-a"};
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        String[] result = new String[tokens.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tokens.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with long option.
     */
    @Test
    public void testBurstTokenLongOption() {
        String token = "--option-a";
        String[] expected = new String[]{"--option-a"};
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        String[] result = new String[tokens.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tokens.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with short option and value.
     */
    @Test
    public void testBurstTokenShortOptionWithValue() {
        String token = "-a=value";
        String[] expected = new String[]{"-a", "value"};
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        String[] result = new String[tokens.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tokens.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with long option and value.
     */
    @Test
    public void testBurstTokenLongOptionWithValue() {
        String token = "--option-a=value";
        String[] expected = new String[]{"--option-a", "value"};
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        String[] result = new String[tokens.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tokens.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with short option and multiple characters.
     */
    @Test
    public void testBurstTokenShortOptionWithMultipleCharacters() {
        String token = "-ab";
        String[] expected = new String[]{"-a", "-b"};
        parser.burstToken(token, false);
        List tokens = parser.getTokens();
        String[] result = new String[tokens.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tokens.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for burstToken method with long option and multiple characters.
     */
    @Test(expected = ParseException.class)
    public void testBurstTokenLongOptionWithMultipleCharacters() {
        String token = "--option-ab";
        parser.burstToken(token, false);
    }

    /**
     * Test for burstToken method with stopAtNonOption.
     */
    @Test
    public void testBurstTokenStopAtNonOption() {
        String token = "--option-a --option-b arg";
        String[] expected = new String[]{"--option-a", "--option-b", "arg"};
        parser.burstToken(token, true);
        List tokens = parser.getTokens();
        String[] result = new String[tokens.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) tokens.get(i);
        }
        Assert.assertArrayEquals(expected, result);
    }
}