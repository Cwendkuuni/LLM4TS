package org.junit.tests; // replace with your package name if necessary

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    

    @Test
    public void testFlatten_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        Options options = new Options();

        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1"};
        Options options = new Options();

        String[] expected = {"arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        Options options = new Options();

        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--"};
        Options options = new Options();

        String[] expected = {"--"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o"};
        Options options = new Options();
        options.addOption("o", true, "outputFile");

        String[] expected = {"-o"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "output.txt"};
        Options options = new Options();
        options.addOption("o", true, "outputFile");

        String[] expected = {"-o", "output.txt"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "output.txt", "-i", "input.txt"};
        Options options = new Options();
        options.addOption("o", true, "outputFile");
        options.addOption("i", true, "inputFile");

        String[] expected = {"-o", "output.txt", "-i", "input.txt"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--output-file", "output.txt"};
        Options options = new Options();
        options.addOption("outputFile", true, "outputFile");

        String[] expected = {"--output-file", "output.txt"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionsWithValues() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--output-file", "output.txt", "--input-file", "input.txt"};
        Options options = new Options();
        options.addOption("outputFile", true, "outputFile");
        options.addOption("inputFile", true, "inputFile");

        String[] expected = {"--output-file", "output.txt", "--input-file", "input.txt"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_IllegalArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--invalid"};
        Options options = new Options();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "--", "arg3"};
        Options options = new Options();

        String[] expected = {"arg1", "--", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "-o", "output.txt", "--", "arg3"};
        Options options = new Options();
        options.addOption("o", true, "outputFile");

        String[] expected = {"arg1", "-o", "output.txt", "--", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "-o", "output.txt", "--input-file", "input.txt", "--", "arg3"};
        Options options = new Options();
        options.addOption("outputFile", true, "outputFile");
        options.addOption("inputFile", true, "inputFile");

        String[] expected = {"arg1", "-o", "output.txt", "--input-file", "input.txt", "--"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    
}