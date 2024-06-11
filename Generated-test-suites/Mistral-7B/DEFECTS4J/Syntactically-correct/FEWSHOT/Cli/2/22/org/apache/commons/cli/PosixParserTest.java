package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSingleArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expected = {"arg1"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSpecialArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--arg"};
        String[] expected = {"--arg"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenSingleHyphenArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options().addOption("a", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] expected = {"-a", "arg"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithMultipleArgs() {
        Options options = new Options().addOption("a", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1", "arg2"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithLongArg() {
        Options options = new Options().addOption("a", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "long argument"};
        String[] expected = {"-a", "long argument"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenOptionWithLongArgAndMultipleTokens() {
        Options options = new Options().addOption("a", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "long", "argument"};
        String[] expected = {"-a", "long", "argument"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenDoubleHyphenArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        String[] expected = {"--"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "arg2"};
        String[] expected = {"arg1", "-a"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithLongArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--long-arg", "arg2"};
        String[] expected = {"arg1", "--long-arg"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithOptionAndLongArg() {
        Options options = new Options().addOption("a", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "long argument"};
        String[] expected = {"arg1", "-a"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithMultipleOptions() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "-b", "arg2"};
        String[] expected = {"arg1", "-a", "-b"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithLongOptions() {
        Options options = new Options().addOption("long-option", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--long-option", "arg2"};
        String[] expected = {"arg1", "--long-option"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenStopAtNonOptionWithLongOptionsAndArg() {
        Options options = new Options().addOption("long-option", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--long-option=arg"};
        String[] expected = {"arg1", "--long-option=arg"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test(expected = AssertionError.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-i", "arg2"}; // i is not a valid option

        initParser(parser, options);
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenMultipleOptionsWithArg() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "argA", "-b", "argB"};
        String[] expected = {"arg1", "-a", "argA", "-b", "argB"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenMultipleOptionsWithArgAndLongArg() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--a=argA", "-b", "--long-option=longArg"};
        String[] expected = {"arg1", "--a=argA", "-b", "--long-option=longArg"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenMultipleOptionsWithArgAndLongArgAndStopAtNonOption() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--a=argA", "-b", "--long-option=longArg", "arg3"};
        String[] expected = {"arg1", "--a=argA", "-b"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenMultipleOptionsWithArgAndStopAtNonOption() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "argA", "-b", "argB", "arg3"};
        String[] expected = {"arg1", "-a", "argA"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenOptionsWithArgumentsAndStopAtNonOption() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help")
                                       .addOption("c", value -> "Value: " + value, "Value");
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "argA", "-b", "argB", "-c", "argC"};
        String[] expected = {"arg1", "-a", "argA"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenComplexArguments() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help")
                                       .addOption("c", value -> "Value: " + value, "Value");
        PosixParser parser = new PosixParser();
        String[] args = {"--arg1=arg1A", "--arg2=arg2A", "--arg3=arg3A", "-a",
                         "--arg3=arg3B", "-b", "--arg2=arg2B", "argC"};
        String[] expected = {"--arg1=arg1A", "--arg2=arg2A", "--arg3=arg3A", "-a",
                             "--arg3=arg3B", "-b", "--arg2=arg2B"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenComplexArgumentsWithStopAtNonOption() {
        Options options = new Options().addOption("a", true, "Help")
                                       .addOption("b", true, "Help")
                                       .addOption("c", value -> "Value: " + value, "Value");
        PosixParser parser = new PosixParser();
        String[] args = {"--arg1=arg1A", "--arg2=arg2A", "--arg3=arg3A", "-a",
                         "--arg3=arg3B", "-b", "--arg2=arg2B", "--arg1=arg1B", "argC"};
        String[] expected = {"--arg1=arg1A", "--arg1=arg1B"};

        initParser(parser, options);
        assertArrayEquals(expected, parser.flatten(options, args, true));
    }

    private void initParser(PosixParser parser, Options options) {
        parser.init();
        parser.options = options;
    }
}