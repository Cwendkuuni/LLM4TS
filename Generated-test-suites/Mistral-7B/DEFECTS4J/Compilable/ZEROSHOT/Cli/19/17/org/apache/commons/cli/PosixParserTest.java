package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"java", "--foo", "bar", "--quux", "-z"};
        List<String> expected = Arrays.asList("java", "--foo", "bar", "--quux", "-z");

        Parser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"java", "--foo", "bar", "--quux", "-z", "baz"};
        List<String> expected = Arrays.asList("java", "--foo", "bar", "--quux", "-z", "baz");

        Parser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithOptionArg() {
        String[] args = {"java", "--foo=123"};
        List<String> expected = Arrays.asList("java", "--foo=123");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "A numeric value", true, "123");
        options.addOption(fooOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"java", "--long-option"};
        List<String> expected = Arrays.asList("java", "--long-option");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option longOption = new Option("long-option", "A long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithOptionArgValue() {
        String[] args = {"java", "--foo=value"};
        List<String> expected = Arrays.asList("java", "--foo", "value");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "A value", true, "value");
        options.addOption(fooOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithOptionNoArg() {
        String[] args = {"java", "--foo"};
        List<String> expected = Arrays.asList("java", "--foo");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "A flag");
        options.addOption(fooOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] args = {"java", "--foo=123", "--bar=456", "--baz"};
        List<String> expected = Arrays.asList("java", "--foo", "123", "--bar", "456", "--baz");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "A numeric value", true, "123");
        Option barOption = new Option("bar", "A numeric value", true, "456");
        Option bazOption = new Option("baz", "A flag");
        options.addOption(fooOption);
        options.addOption(barOption);
        options.addOption(bazOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithLongAndShortOptions() {
        String[] args = {"java", "--long-option", "-f"};
        List<String> expected = Arrays.asList("java", "--long-option", "-f");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option longOption = new Option("long-option", "A long option");
        Option shortOption = new Option("f", "A short option");
        options.addOption(longOption);
        options.addOption(shortOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithMixedArguments() {
        String[] args = {"java", "arg1", "--foo=bar", "arg2"};
        List<String> expected = Arrays.asList("java", "arg1", "--foo", "bar", "arg2");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "A value", true, "bar");
        options.addOption(fooOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithMixedArgumentsAndStopAtNonOption() {
        String[] args = {"java", "arg1", "--foo=bar", "arg2", "arg3"};
        List<String> expected = Arrays.asList("java", "arg1", "--foo", "arg2", "arg3");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "A value", true, "bar");
        options.addOption(fooOption);

        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithMultipleArgumentsForOption() {
        String[] args = {"java", "--foo=arg1,arg2,arg3"};
        List<String> expected = Arrays.asList("java", "--foo", "arg1,arg2,arg3");

        Parser parser = new PosixParser();
        Options options = new Options();
        Option fooOption = new Option("foo", "Arguments for foo", true, "arg1,arg2,arg3");
        options.addOption(fooOption);

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}