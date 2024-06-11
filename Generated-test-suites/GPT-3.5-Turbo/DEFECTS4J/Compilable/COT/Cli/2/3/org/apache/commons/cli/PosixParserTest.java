package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        // Test case for stopAtNonOption=true
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"arg1", "-a", "-b", "value", "arg2"};

        String[] result = parser.flatten(options, args, true);

        String[] expected = {"arg1", "-a", "-b", "value", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DontStopAtNonOption() {
        // Test case for stopAtNonOption=false
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"arg1", "-a", "-b", "value", "arg2"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"arg1", "-a", "-b", "value", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Hyphen() {
        // Test case for a hyphen token
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"-"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        // Test case for a long option with a value
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"--option=value"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        // Test case for a long option without a value
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"--option"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        // Test case for a short option with a value
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option a");

        String[] args = {"-avalue"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithoutValue() {
        // Test case for a short option without a value
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"-a"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }
}