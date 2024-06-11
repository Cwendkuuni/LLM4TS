package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOneArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1"};
        String[] expected = {"arg1"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "--option"};
        String[] expected = {"arg1", "--option"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleDashArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDashArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"--"};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOneOption() {
        Options options = new Options();
        options.addOption("option", false, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o"};
        String[] expected = {"option"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOneOptionWithArgument() {
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "arg"};
        String[] expected = {"option", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", false, "o2");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o1", "-o2"};
        String[] expected = {"option1", "option2"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArgumentAndOtherArguments() {
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "arg1", "arg2"};
        String[] expected = {"option", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("option", false, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "--nonOption", "arg"};
        String[] expected = {"option", "--nonOption", "arg"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstedOption() {
        Options options = new Options();
        options.addOption("option", false, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o--longOptionArgument"};
        String[] expected = {"option", "longOptionArgument"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstedOptionAndOtherArguments() {
        Options options = new Options();
        options.addOption("option", false, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o--longOptionArgument", "arg1", "arg2"};
        String[] expected = {"option", "longOptionArgument", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstedOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("option", false, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o--longOptionArgument", "--nonOption", "arg"};
        String[] expected = {"option", "--nonOption", "arg"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstedOptionAndStopAtNonOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("option", false, "o");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o--longOptionArgument1", "--nonOption", "arg1", "arg2"};
        String[] expected = {"option", "--nonOption", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }
}