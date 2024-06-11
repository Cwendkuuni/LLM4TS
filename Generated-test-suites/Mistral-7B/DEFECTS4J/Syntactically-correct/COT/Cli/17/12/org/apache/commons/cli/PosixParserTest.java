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
    public void testFlatten_EmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];

        try {
            String[] result = parser.flatten(new Options(), arguments, false);
            Assert.assertArrayEquals(new String[0], result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg"};

        try {
            String[] result = parser.flatten(new Options(), arguments, false);
            Assert.assertArrayEquals(new String[]{"arg"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_TwoArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1", "arg2"};

        try {
            String[] result = parser.flatten(new Options(), arguments, false);
            Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleDash() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};

        try {
            String[] result = parser.flatten(new Options(), arguments, false);
            Assert.assertArrayEquals(new String[]{"-"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_DoubleDash() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--"};

        try {
            String[] result = parser.flatten(new Options(), arguments, false);
            Assert.assertArrayEquals(new String[]{"--"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_OptionArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("option", true, "o");

        String[] arguments = {"-o", "value"};

        try {
            String[] result = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"option", "value"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_OptionWithLongArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("long-option", true, "l");

        String[] arguments = {"-l", "long-value"};

        try {
            String[] result = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"long-option", "long-value"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("option1", true, "o1");
        options.addRequiredOption("option2", true, "o2");

        String[] arguments = {"-o1", "value1", "-o2", "value2"};

        try {
            String[] result = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"option1", "value1", "option2", "value2"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_OptionWithLongArgumentAndValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("long-option", true, "l");

        String[] arguments = {"-l", "long-option-value"};

        try {
            String[] result = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"long-option", "long-option-value"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("option", true, "o");

        String[] arguments = {"arg1", "-o", "value", "arg2"};

        try {
            String[] result = parser.flatten(options, arguments, true);
            Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MixedOptionsAndValues() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("option1", true, "o1");
        options.addRequiredOption("option2", true, "o2");

        String[] arguments = {"-o1", "val1", "--", "-o2", "val2"};

        try {
            String[] result = parser.flatten(options, arguments, false);
            Assert.assertArrayEquals(new String[]{"option1", "val1", "option2", "val2"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--unknown"};

        try {
            parser.flatten(new Options(), arguments, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnmatchedOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addRequiredOption("option", true, "o");

        String[] arguments = {"-oo", "value"};

        try {
            parser.flatten(options, arguments, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}