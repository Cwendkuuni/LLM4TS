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
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("a", true, "Test option");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Test option");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] expected = {"-a", "value"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Test option 1");
        options.addOption("b", true, "Test option 2");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] expected = {"-a", "-b"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_MultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Test option 1");
        options.addOption("b", true, "Test option 2");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "Test long option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expected = {"--longOption"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "Test long option");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value"};
        String[] expected = {"--longOption", "value"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_MultipleLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", false, "Test long option 1");
        options.addOption("longOption2", false, "Test long option 2");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1", "--longOption2"};
        String[] expected = {"--longOption1", "--longOption2"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_LongOptionWithArgumentAndMultipleArguments() {
        Options options = new Options();
        options.addOption("longOption", true, "Test long option");
        options.addOption("longOption2", true, "Test long option 2");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value1", "--longOption2", "value2"};
        String[] expected = {"--longOption", "value1", "--longOption2", "value2"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_IgnoreNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-n", "value"};
        String[] expected = {};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, true));
        List tokens = Arrays.asList(args);
        Assert.assertEquals(tokens.toString(), Arrays.toString(parser.flatten(options, args.clone(), false)));
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        String[] expected = {"-"};
        Assert.assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-x"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_InvalidOptionWithArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-x", "value"};
        try {
            parser.flatten(options, args, false);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unrecognized option: -x", e.getMessage());
        }
    }
}