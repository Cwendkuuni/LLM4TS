package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PosixParser} class.
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("i", "input", true, "input value");
        options.addOption("o", "output", true, "output value");
        options.addOption("h", "help", false, "print help message");
    }

    @Test
    public void testSimpleFlatten() throws ParseException {
        String[] args = {"--input", "inputValue", "--output", "outputValue", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--input", "inputValue", "--output", "outputValue"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testLongOptionWithValue() throws ParseException {
        String[] args = {"--input=inputValue", "--output=outputValue", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--input", "inputValue", "--output", "outputValue"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testShortOption() throws ParseException {
        String[] args = {"-i", "inputValue", "--output", "outputValue", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-i", "inputValue"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testShortOptionWithValue() throws ParseException {
        String[] args = {"-i", "inputValue", "--output", "outputValue", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-i", "inputValue"};
        assertArrayEquals(expected, result);

        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(3, parser.tokens.size());
    }

    @Test
    public void testShortOptionWithValueAndAnotherOption() throws ParseException {
        String[] args = {"-i", "inputValue", "--output", "outputValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-i", "inputValue", "--output", "outputValue"};
        assertArrayEquals(expected, result);

        assertSame(options.findOption("i"), parser.currentOption);
        assertTrue(parser.eatTheRest);
        assertEquals(3, parser.tokens.size());
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalidOption", "--output", "outputValue"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testInvalidOptionStopAtNonOption() throws ParseException {
        String[] args = {"--invalidOption", "--output", "outputValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--invalidOption", "--output", "outputValue"};
        assertArrayEquals(expected, result);

        assertEquals(4, parser.tokens.size());
    }

    @Test
    public void testLongOptionWithTwoValues() throws ParseException {
        String[] args = {"--input=inputValue1", "--input=inputValue2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--input", "inputValue1", "--input", "inputValue2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstableToken() throws ParseException {
        String[] args = {"-io", "inputValue", "--output", "outputValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-i", "o", "inputValue", "--output", "outputValue"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstableTokenStopAtNonOption() throws ParseException {
        String[] args = {"-io", "inputValue", "--output", "outputValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-io", "inputValue", "--output", "outputValue"};
        assertArrayEquals(expected, result);

        assertEquals(4, parser.tokens.size());
    }

    @Test
    public void testHelpOption() throws ParseException {
        String[] args = {"--help"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--help"}, result);

        assertTrue(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertNull(parser.currentOption);
    }
}