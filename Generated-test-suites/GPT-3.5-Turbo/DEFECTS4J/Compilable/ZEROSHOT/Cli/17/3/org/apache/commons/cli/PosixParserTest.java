package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption(new Option("a", "alpha", false, "Alpha option"));
        options.addOption(new Option("b", "beta", true, "Beta option"));
        options.addOption(new Option("c", "gamma", false, "Gamma option"));
    }

    @Test
    public void testFlatten_noArguments() {
        String[] arguments = {};
        String[] expected = {};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_singleOption_noValue() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_singleOption_withValue() {
        String[] arguments = {"-b", "value"};
        String[] expected = {"-b", "value"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_multipleOptions() {
        String[] arguments = {"-a", "-b", "value", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] arguments = {"-a", "value", "nonOption", "-b"};
        String[] expected = {"-a", "value", "--", "nonOption", "-b"};
        String[] actual = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_burstToken() {
        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_burstTokenStopAtNonOption() {
        String[] arguments = {"-abc", "nonOption"};
        String[] expected = {"-a", "-b", "-c", "--", "nonOption"};
        String[] actual = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_hyphenToken() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_specialToken() {
        String[] arguments = {"--option=value"};
        String[] expected = {"--option", "value"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_specialToken_equalSign() {
        String[] arguments = {"--option=value=value"};
        String[] expected = {"--option", "value=value"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_gobble() {
        String[] arguments = {"-a", "-b", "value", "nonOption", "-c"};
        String[] expected = {"-a", "-b", "value", "nonOption", "-c"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }
}