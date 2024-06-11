package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        String[] arguments = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenWithSingleValueOption() {
        String[] arguments = {"-a", "Foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-a", "Foo"}, result);
    }

    @Test
    public void testFlattenWithMultipleValueOption() {
        String[] arguments = {"-a", "Foo", "Bar"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-a", "Foo", "Bar"}, result);
    }

    @Test
    public void testFlattenWithOptionalValueOption() {
        String[] arguments = {"-b", "Foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-b", "Foo"}, result);

        String[] argumentsWithNoValue = {"-b"};
        String[] resultWithNoValue = parser.flatten(options, argumentsWithNoValue, false);
        assertEquals(new String[]{"-b"}, resultWithNoValue);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = {"-a", "-b", "-c"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    @Test
    public void testFlattenWithOptionAndValueAndOtherArguments() {
        String[] arguments = {"-a", "Foo", "-b", "Bar", "Baz"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-a", "Foo", "-b", "Bar"}, result);

        String[] remainingArguments = {"Baz"};
        String[] flattenedArguments = parser.flatten(options, remainingArguments, false);
        assertEquals(new String[]{"Baz"}, flattenedArguments);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] arguments = {"--longOption"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOption"}, result);

        String[] argumentsWithValue = {"--longOption", "Foo"};
        String[] resultWithValue = parser.flatten(options, argumentsWithValue, false);
        assertEquals(new String[]{"--longOption", "Foo"}, resultWithValue);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-a", "-c", "-x"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"-a", "-c", "-x"}, result);

        String[] tokens = parser.flatten(options, arguments, true);
        assertEquals(3, tokens.length);
    }

    @Test
    public void testBurstsSingleOptionToken() {
        String[] arguments = {"--ab"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--ab"}, result);

        String[] burstedTokens = parser.burstToken("--ab", false);
        assertEquals(new String[]{"--a", "--b"}, burstedTokens);
    }

    @Test
    public void testBurstsTwoCharacterOptionToken() {
        String[] arguments = {"--ac"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--ac"}, result);

        String[] burstedTokens = parser.burstToken("--ac", false);
        assertEquals(new String[]{"--a", "--c"}, burstedTokens);
    }

    @Test
    public void testBurstsThreeCharacterOptionTokenWithValue() {
        String[] arguments = {"--abc", "Foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--abc", "Foo"}, result);

        String[] burstedTokens = parser.burstToken("--abc", false);
        assertEquals(new String[]{"--a", "--b", "--c"}, burstedTokens);

        String[] flattenedTokens = parser.burstToken("--abc", false);
        String[] expectedTokens = new String[]{"--a", "--b", "--c", "Foo"};
        assertArrayEquals(expectedTokens, flattenedTokens);
    }

    @Test
    public void testBurstsThreeCharacterOptionTokenWithoutValue() {
        String[] arguments = {"--abc"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--abc"}, result);

        String[] burstedTokens = parser.burstToken("--abc", false);
        assertEquals(new String[]{"--a", "--b", "--c"}, burstedTokens);
    }

    @Test
    public void testBurstsThreeCharacterOptionTokenWithValueAndStopAtNonOption() {
        String[] arguments = {"--abc", "Foo", "-x"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"--abc", "-x"}, result);

        String[] burstedTokens = parser.burstToken("--abc", true);
        assertEquals(new String[]{"--a", "--b"}, burstedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithValue() {
        String[] arguments = {"--longOptionValue", "Foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOptionValue", "Foo"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", false);
        assertEquals(new String[]{"--longOption", "Value"}, burstedTokens);

        String[] flattenedTokens = parser.burstToken("--longOptionValue", false);
        String[] expectedTokens = new String[]{"--longOption", "Value", "Foo"};
        assertArrayEquals(expectedTokens, flattenedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithoutValue() {
        String[] arguments = {"--longOptionValue"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOptionValue"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", false);
        assertEquals(new String[]{"--longOption", "Value"}, burstedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithValueAndStopAtNonOption() {
        String[] arguments = {"--longOptionValue", "Foo", "-x"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"--longOptionValue", "-x"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", true);
        assertEquals(new String[]{"--longOption"}, burstedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithMultipleValues() {
        String[] arguments = {"--longOptionValue", "Foo", "Bar", "Baz"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOptionValue", "Foo", "Bar", "Baz"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", false);
        String[] expectedTokens = new String[]{"--longOption", "Value", "Foo", "Bar", "Baz"};
        assertArrayEquals(expectedTokens, burstedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithMultipleValuesAndStopAtNonOption() {
        String[] arguments = {"--longOptionValue", "Foo", "Bar", "-x"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"--longOptionValue", "-x"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", true);
        String[] expectedTokens = new String[]{"--longOption"};
        assertArrayEquals(expectedTokens, burstedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithNoValue() {
        String[] arguments = {"--longOptionValue"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOptionValue"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", false);
        String[] expectedTokens = new String[]{"--longOption", "Value"};
        assertArrayEquals(expectedTokens, burstedTokens);
    }

    @Test
    public void testBurstsMultiCharacterOptionTokenWithNoValueAndStopAtNonOption() {
        String[] arguments = {"--longOptionValue", "-x"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"--longOptionValue"}, result);

        String[] burstedTokens = parser.burstToken("--longOptionValue", true);
        String[] expectedTokens = new String[]{"--longOption"};
        assertArrayEquals(expectedTokens, burstedTokens);
    }

    @Test
    public void testBurstsSingleDash() {
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testBurstsDoubleDash() {
        String[] arguments = {"--"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--"}, result);
    }

    @Test
    public void testBurstsEmptyString() {
        String[] arguments = {"--"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testBurstsStopAtNonOptionEmptyString() {
        String[] arguments = {"--"};
        String[] result = parser.burstToken("--", true);
        assertArrayEquals(new String[0], result);
    }
}