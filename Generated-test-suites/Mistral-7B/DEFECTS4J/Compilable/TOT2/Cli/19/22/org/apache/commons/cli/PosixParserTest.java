package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenBasicUsage() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA", "-b", "--optionB=valueB", "--"};
        String[] expected = {"-a", "--optionA", "valueA", "-b", "--optionB", "valueB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA", "-b", "--", "--optionC", "valueC"};
        String[] expected = {"-a", "--optionA", "valueA", "--optionC", "valueC"};

        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--optionA", "--"};
        String[] expected = {"--optionA"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption", "--"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-ab", "--"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithValue() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-ab=value", "--"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA"};
        String[] expected = {"-a", "--optionA", "valueA"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA1", "--optionA=valueA2"};
        String[] expected = {"-a", "--optionA", "valueA1", "--optionA", "valueA2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithLongArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "l");
        PosixParser parser = new PosixParser();
        String[] args = {"-l", "--longOption=longValue"};
        String[] expected = {"-l", "--longOption", "longValue"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithLongArgumentAndMultipleValues() {
        Options options = new Options();
        options.addOption("longOption", true, "l");
        PosixParser parser = new PosixParser();
        String[] args = {"-l", "--longOption=longValue1", "--longOption=longValue2"};
        String[] expected = {"-l", "--longOption", "longValue1", "--longOption", "longValue2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithArgument() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        PosixParser parser = new PosixParser();
        String[] args = {"-a=", "--"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        PosixParser parser = new PosixParser();
        String[] args = {"-a=valueA1=valueA2", "--"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithMultipleArgumentsAndLongArgument() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        PosixParser parser = new PosixParser();
        String[] args = {"-a=valueA1=valueA2=longValue", "--"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA", "-b", "--optionB=valueB"};
        String[] expected = {"-a", "--optionA", "valueA", "-b", "--optionB", "valueB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithLongArgument() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=longValueA", "-b", "--optionB=longValueB"};
        String[] expected = {"-a", "--optionA", "longValueA", "-b", "--optionB", "longValueB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithLongArgumentAndValues() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=longValueA1,longValueA2", "-b", "--optionB=longValueB1,longValueB2"};
        String[] expected = {"-a", "--optionA", "longValueA1", "longValueA2", "-b", "--optionB", "longValueB1", "longValueB2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithValuesAndNoEqualsSign() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "longValueA1,longValueA2", "-b", "longValueB1,longValueB2"};
        String[] expected = {"-a", "--optionA", "longValueA1", "longValueA2", "-b", "--optionB", "longValueB1", "longValueB2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA", "-b", "--optionB=valueB"};
        String[] expected = {"-a", "--optionA", "valueA", "-b", "--optionB", "valueB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgumentsAndLongArgument() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=longValueA", "-b", "--optionB=longValueB"};
        String[] expected = {"-a", "--optionA", "longValueA", "-b", "--optionB", "longValueB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgumentsAndValues() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=valueA1,valueA2", "-b", "--optionB=valueB1,valueB2"};
        String[] expected = {"-a", "--optionA", "valueA1", "valueA2", "-b", "--optionB", "valueB1", "valueB2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgumentsAndNoEqualsSign() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "valueA1,valueA2", "-b", "valueB1,valueB2"};
        String[] expected = {"-a", "--optionA", "valueA1", "valueA2", "-b", "--optionB", "valueB1", "valueB2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArgumentsAndValuesAndLongArgument() {
        Options options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA=longValueA1,longValueA2", "-b", "--optionB=longValueB1,longValueB2"};
        String[] expected = {"-a", "--optionA", "longValueA1", "longValueA2", "-b", "--optionB", "longValueB1", "longValueB2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }
}