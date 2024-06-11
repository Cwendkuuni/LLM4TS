package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultiValueOption() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "value2"};
        String[] expected = {"-a", "value1", "value2"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] expected = {"-a", "arg"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArgAndValue() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg", "value"};
        String[] expected = {"-a", "arg", "value"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithLongArgument() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--a", "long argument"};
        String[] expected = {"--a", "long argument"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithShortArgumentAndLongArgument() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        options.addOption("b", 1, "b description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "short arg", "--b", "long arg"};
        String[] expected = {"-a", "short arg", "--b", "long arg"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--a=value"};
        String[] expected = {"--a", "value"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndOtherOption() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        options.addOption("b", 1, "b description");
        PosixParser parser = new PosixParser();
        String[] args = {"--a=value", "-b", "other value"};
        String[] expected = {"--a", "value", "-b", "other value"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndMultipleArguments() {
        Options options = new Options();
        options.addOption("a", 1, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--a=value1 value2"};
        String[] expected = {"--a", "value1 value2"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        options.addOption("b", true, "b description");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMixedArgumentsAndOptions() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "-a", "arg1", "arg2", "--option2"};
        String[] expected = {"--option1", "-a", "arg1", "arg2", "--option2"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a description");
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "-a", "arg1", "arg2", "--option2"};
        String[] expected = {"--option1"};

        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }
}