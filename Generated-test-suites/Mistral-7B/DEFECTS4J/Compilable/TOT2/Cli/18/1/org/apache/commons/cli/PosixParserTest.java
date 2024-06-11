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
    public void testFlatten_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList();
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("arg1");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SpecialTokens() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--arg1", "--arg2=value"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--arg1", "--arg2=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionToken() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionA", true, "Option A");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "--optionA");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x"};
        Options options = new Options();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_InvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("-x", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]) , result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--optionB", "-b", "--optionC=value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("optionB", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("optionC", true, "Option C");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "--optionB", "-b", "--optionC=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongString() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOption=longValue"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--longOption=longValue");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithLongArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--optionB=longValue"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-a", "--optionB=longValue");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongStringWithArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--optionB=longValueArg1 arg1 arg2"};
        Options options = new Options();
        options.addOption("optionB", true, "Option B");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--optionB=longValueArg1 arg1 arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}