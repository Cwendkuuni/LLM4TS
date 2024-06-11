package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {
    
    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions_ReturnsSameArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithValidOption_ReturnsOptionAndArguments() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] arguments = {"-f", "input.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-f", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlatten_WithValidMultipleOptions_ReturnsOptionsAndArguments() {
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", true, "input directory");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-f", "input.txt", "-d", "folder"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-f", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("folder", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsOptionsAndArgumentsUntilNonOption() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] arguments = {"-f", "input.txt", "nonoption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-f", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("nonoption", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithMultipleOptions_ReturnsOptionsAndArgumentsUntilNonOption() {
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", true, "input directory");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-f", "input.txt", "-d", "folder", "nonoption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-f", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("folder", result[3]);
        assertEquals("nonoption", result[4]);
    }

    @Test
    public void testFlatten_WithLongOptionWithValue_ReturnsOptionAndValue() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] arguments = {"--file=input.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--file", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlatten_WithLongOptionWithoutValue_ReturnsOptionOnly() {
        Options options = new Options();
        Option option = new Option("f", "file", false, "input file");
        options.addOption(option);
        String[] arguments = {"--file"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--file", result[0]);
    }

    @Test
    public void testFlatten_WithMultipleLongOptions_ReturnsOptionsAndValues() {
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", true, "input directory");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--file=input.txt", "--directory=folder"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--file", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("--directory", result[2]);
        assertEquals("folder", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithLongOptions_ReturnsOptionsAndValuesUntilNonOption() {
        Options options = new Options();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", true, "input directory");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--file=input.txt", "--directory=folder", "nonoption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("--file", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("--directory", result[2]);
        assertEquals("folder", result[3]);
        assertEquals("nonoption", result[4]);
    }

    @Test
    public void testFlatten_WithOptionWithoutArgumentAndArguments_ReturnsOptionAndArguments() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "enable debug mode");
        options.addOption(option);
        String[] arguments = {"--debug", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--debug", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_WithOptionWithoutArgumentAndNoArguments_ReturnsOptionOnly() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "enable debug mode");
        options.addOption(option);
        String[] arguments = {"--debug"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--debug", result[0]);
    }

}