package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        String[] arguments = {};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleArgument() {
        String[] arguments = {"arg"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        String[] arguments = {"-o"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testFlattenWithSingleOptionValue() {
        String[] arguments = {"-o", "value"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] arguments = {"--long-option"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        String[] arguments = {"--long-option", "value"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = {"-o1", "-o2", "--long-option"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o1", "--o2", "--long-option"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsValues() {
        String[] arguments = {"-o1", "value1", "-o2", "value2", "--long-option", "value3"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o1", "value1", "-o2", "value2", "--long-option", "value3"}, result);
    }

    @Test
    public void testFlattenWithMultipleArguments() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testFlattenWithMixedArgumentsAndOptions() {
        String[] arguments = {"-o", "arg1", "-o2", "--long-option", "arg3", "--another-long-option", "arg4"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List expected = Arrays.asList("--o", "arg1", "-o2", "--long-option", "arg3", "--another-long-option", "arg4");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-o", "arg1", "--stop", "arg3", "--another-option", "arg4"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List expected = Arrays.asList("-o", "arg1", "--stop", "arg3", "--another-option", "arg4");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        String[] arguments = {"-invalid-option"};
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}