package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "-b", "-c", "--longoption", "value1", "--longoption2=value2", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "-c", "--longoption", "value1", "--longoption2=value2", "arg1", "arg2"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "-b", "-c", "--longoption", "value1", "--longoption2=value2", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "-c"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithEqualSign() {
        String[] arguments = {"--longoption=value"};
        String[] expected = {"--longoption", "value"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_UnknownOption_KeepAll() {
        String[] arguments = {"-a", "-b", "-c", "-d"};
        String[] expected = {"-a", "-b", "-c", "-d"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption() {
        String[] arguments = {"-a", "-b", "-c", "-d"};
        String[] expected = {"-a", "-b", "-c"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ConsecutiveOptionsWithArg() {
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ConsecutiveOptionsWithoutArg() {
        String[] arguments = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_ConsecutiveOptionsWithArg_StopAtNonOption() {
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_UnrecognizedOptionInMiddle_StopAtNonOption() {
        String[] arguments = {"-a", "-b", "-c", "-d", "arg"};
        String[] expected = {"-a", "-b", "-c"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }
}