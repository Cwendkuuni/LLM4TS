package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "An option with an argument");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SingleDoubleHyphen() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_TwoOptions() {
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        String[] args = {"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option with argument");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-z"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "arg1", "-", "arg2", "--", "arg3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"-a", "--", "arg2", "--", "arg3"}, result);
    }

    @Test
    public void testFlatten_DoubleDashAsSingleOption() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_DoubleDashAsSeparator() {
        String[] args = {"-a", "--", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "--", "-b"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgumentAndMultipleArguments() {
        String[] args = {"-a", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A with multiple arguments");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleArguments() {
        String[] args = {"--option", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, "Option with multiple arguments");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg1", "arg2"}, result);
    }
}