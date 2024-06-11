package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    

    @Test
    public void testFlatten_Basic() {
        String[] args = {"--test", "-a", "-b=1", "-c", "-d", "--e=f"};
        Options options = new Options();
        options.addOption("test", true, "test option");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        options.addOption("d", true, "option d");
        options.addOption("e", true, "option e");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--test",
                "-a",
                "-b",
                "1",
                "-c",
                "-d",
                "--e",
                "f"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--test", "-a", "-b=1", "-c", "-d", "--e=f", "arg"};
        Options options = new Options();
        options.addOption("test", true, "test option");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        options.addOption("d", true, "option d");
        options.addOption("e", true, "option e");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--test",
                "-a",
                "-b=1",
                "--e=f",
                "arg"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = {"--test", "--test", "arg"};
        Options options = new Options();
        options.addOption("test", true, "test option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--test",
                "--test",
                "arg"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"--test", "-x", "-b=1", "-c", "-d", "--e=f"};
        Options options = new Options();
        options.addOption("test", true, "test option");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        options.addOption("d", true, "option d");
        options.addOption("e", true, "option e");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The test should throw a ParseException
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOptionWithArg() {
        String[] args = {"--test", "-x=y", "-b=1", "-c", "-d", "--e=f"};
        Options options = new Options();
        options.addOption("test", true, "test option");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        options.addOption("d", true, "option d");
        options.addOption("e", true, "option e");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The test should throw a ParseException
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        String[] args = {"--long-option-with-arg=value"};
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "long option with arg");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--long-option-with-arg", "value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_OptionWithArgList() {
        String[] args = {"--option-with-arg-list=value1,value2"};
        Options options = new Options();
        options.addOption("optionWithArgList", true, "option with arg list");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--option-with-arg-list",
                "value1,value2"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    
}