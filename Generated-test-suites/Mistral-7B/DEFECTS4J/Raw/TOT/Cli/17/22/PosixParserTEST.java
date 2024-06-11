package org.apache.commons.cli.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * Test suite for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flattening a simple command line with no options or arguments.
     */
    @Test
    public void testFlattenSimple() {
        String[] args = new String[]{"MyProgram"};
        Options options = new Options();
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"MyProgram"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with a single option.
     */
    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set the alpha flag");
        String[] args = new String[]{"-a"};
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false).getArgs();
        Assert.assertArrayEquals(new String[]{"--alpha"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with an option with an argument.
     */
    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", "alpha", true, "Set the alpha value");
        String[] args = new String[]{"-a", "value"};
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false).getArgs();
        Assert.assertArrayEquals(new String[]{"--alpha", "value"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with multiple options and arguments.
     */
    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set the alpha flag");
        options.addOption("b", "beta", true, "Set the beta value");
        String[] args = new String[]{"-a", "-b", "value1", "value2"};
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false).getArgs();
        Assert.assertArrayEquals(new String[]{"--alpha", "--beta", "value1", "value2"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with multiple options and a long option.
     */
    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set the alpha flag");
        options.addOption("l", "longOption", false, "Set the long option flag");
        String[] args = new String[]{"--longOption", "-a"};
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false).getArgs();
        Assert.assertArrayEquals(new String[]{"--longOption", "--alpha"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with multiple options and a long option with an argument.
     */
    @Test
    public void testFlattenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("l", "longOption", true, "Set the long option value");
        options.addOption("a", "alpha", false, "Set the alpha flag");
        String[] args = new String[]{"--longOption", "value", "-a"};
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false).getArgs();
        Assert.assertArrayEquals(new String[]{"--longOption", "value", "--alpha"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with multiple options, long options and arguments.
     */
    @Test
    public void testFlattenMultipleOptionsLongOptions() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set the alpha flag");
        options.addOption("l", "longOption", true, "Set the long option value");
        options.addOption("o", "outputFile", true, "Set the output file");
        String[] args = new String[]{"--outputFile", "output.txt", "--longOption", "value", "-a"};
        CommandLineParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false).getArgs();
        Assert.assertArrayEquals(new String[]{"--outputFile", "output.txt", "--longOption", "value", "--alpha"}, flattenedArgs);
    }

    /**
     * Test flattening a command line with an invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = new String[]{"-z"};
        CommandLineParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    /**
     * Test flattening a command line with a simple option and an invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenSimpleInvalidOption() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set the alpha flag");
        String[] args = new String[]{"-a", "-z"};
        CommandLineParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    /**
     * Test flattening a command line with a long option and an invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenLongOptionInvalidOption() {
        Options options = new Options();
        options.addOption("l", "longOption", false, "Set the long option flag");
        String[] args = new String[]{"--longOption", "-z"};
        CommandLineParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    /**
     * Test flattening a command line with multiple invalid options.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenMultipleInvalidOptions() {
        Options options = new Options();
        String[] args = new String[]{"-z", "-z"};
        CommandLineParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    /**
     * Test flattening a command line with multiple options and multiple invalid options.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenMultipleOptionsMultipleInvalidOptions() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set the alpha flag");
        String[] args = new String[]{"-a", "-z", "-z"};
        CommandLineParser parser = new PosixParser();
        parser.parse(options, args, false);
    }
}