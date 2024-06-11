package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[0];
        String[] expected = new String[0];

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption_ReturnsOptionArray() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithArgument_ReturnsOptionAndArgumentArray() {
        Options options = new Options();
        options.addOption(new Option("a", true, "option a"));
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsPartialArray() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        options.addOption(new Option("b", false, "option b"));
        String[] arguments = {"-a", "value", "nonOption"};
        String[] expected = {"-a", "value"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_ReturnsBurstTokensArray() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        options.addOption(new Option("b", false, "option b"));
        String[] arguments = {"-ab"};
        String[] expected = {"-a", "-b"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_HyphenToken_ReturnsHyphenArray() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        String[] arguments = {"-"};
        String[] expected = {"-"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenToken_ReturnsDoubleHyphenArray() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        String[] arguments = {"--"};
        String[] expected = {"--"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenWithValue_ReturnsDoubleHyphenAndValueArray() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        String[] arguments = {"--value"};
        String[] expected = {"--value"};

        String[] result = posixParser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

}