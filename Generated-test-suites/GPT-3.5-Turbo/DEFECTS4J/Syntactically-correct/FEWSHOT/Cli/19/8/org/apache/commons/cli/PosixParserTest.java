package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_withNoOptionsAndArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withNoOptionsAndArguments_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withSingleOptionAndNoArguments() {
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        String[] arguments = {};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withSingleOptionAndNoArguments_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        String[] arguments = {};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withSingleOptionAndSingleArgument() {
        Options options = new Options();
        options.addOption("a", true, "Test option a");
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withSingleOptionAndSingleArgument_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Test option a");
        String[] arguments = {"-a", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withSingleOptionAndMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "Test option a");
        String[] arguments = {"-a", "value1", "value2", "value3"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withSingleOptionAndMultipleArguments_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Test option a");
        String[] arguments = {"-a", "value1", "value2", "value3"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withInvalidOption_WithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        String[] arguments = {"-b"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withInvalidOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        String[] arguments = {"-b"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withMultipleOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        options.addOption("b", true, "Test option b");
        options.addOption("c", false, "Test option c");
        options.addOption("d", true, "Test option d");
        String[] arguments = {"-a", "-b", "value", "-c", "-d", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_withMultipleOptionsAndArguments_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Test option a");
        options.addOption("b", true, "Test option b");
        options.addOption("c", false, "Test option c");
        options.addOption("d", true, "Test option d");
        String[] arguments = {"-a", "-b", "value", "-c", "-d", "value"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }
}