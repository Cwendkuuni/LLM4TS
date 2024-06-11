package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions() {
        String[] args = new String[]{"java", "MyProgram"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "MyProgram");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = new String[]{"-a", "value"};
        Options options = new Options();
        Option optionA = new Option("a", true, "a help message");
        options.addOption(optionA);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = new String[]{"-a", "--no-b", "-c"};
        Options options = new Options();
        Option optionA = new Option("a", true, "a help message");
        Option optionNoB = new Option("b", false, "b help message");
        Option optionC = new Option("c", true, "c help message");
        options.addOption(optionA);
        options.addOption(optionNoB);
        options.addOption(optionC);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "--no-b", "-c");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithLongArg() {
        String[] args = new String[]{"-a", "long-option-argument"};
        Options options = new Options();
        Option optionA = new Option("a", true, "a help message");
        optionA.setArgName("long-option-argument");
        options.addOption(optionA);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "long-option-argument");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithShortArg() {
        String[] args = new String[]{"-a", "short-arg"};
        Options options = new Options();
        Option optionA = new Option("a", "s", true, "a help message");
        options.addOption(optionA);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "short-arg");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithValueAndShortArg() {
        String[] args = new String[]{"-a", "value", "short-arg"};
        Options options = new Options();
        Option optionA = new Option("a", "s", true, "a help message");
        optionA.setArgName("value");
        options.addOption(optionA);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "value", "short-arg");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = new String[]{"--long-option", "value"};
        Options options = new Options();
        Option optionLong = new Option("l", "long-option", true, "a help message");
        options.addOption(optionLong);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--long-option", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndArg() {
        String[] args = new String[]{"--long-option", "value", "arg"};
        Options options = new Options();
        Option optionLong = new Option("l", "long-option", true, "a help message");
        optionLong.setArgName("arg");
        options.addOption(optionLong);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--long-option", "value", "arg");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = new String[]{"-a", "value", "non-option"};
        Options options = new Options();
        Option optionA = new Option("a", "s", true, "a help message");
        optionA.setArgName("value");
        options.addOption(optionA);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("-a", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), Arrays.copyOfRange(result, 0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidToken() {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_EmptyToken() {
        String[] args = new String[]{"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_EmptyArgs() {
        String[] args = new String[]{};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}