package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testGnuParser_flatten_emptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_simpleOptions() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--", "-c"};
        String[] expected = {"-a", "-b", "--", "-c"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_longOptions() {
        Options options = new Options();
        String[] arguments = {"--longOption1", "--longOption2=value"};
        String[] expected = {"--longOption1", "--longOption2=value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_shortOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Short Option A");
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_singleShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Short Option A");
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_multipleShortOptionsWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Short Option A");
        options.addOption("b", true, "Option B", "Short Option B");
        String[] arguments = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_shortOptionWithLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Short Option A");
        options.addOption("longOptionA", true, "Long Option A", "Long Option A");
        String[] arguments = {"-a", "--longOptionA"};
        String[] expected = {"-a", "--longOptionA"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_longOptionWithShortOption() {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A", "Long Option A");
        options.addOption("a", true, "Option A", "Short Option A");
        String[] arguments = {"--longOptionA", "-a"};
        String[] expected = {"--longOptionA", "-a"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_shortOptionWithLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Short Option A");
        options.addOption("longOptionA", true, "Long Option A", "Long Option A");
        String[] arguments = {"-a", "--longOptionA=value"};
        String[] expected = {"-a", "--longOptionA=value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_longOptionWithShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A", "Long Option A");
        options.addOption("a", true, "Option A", "Short Option A");
        String[] arguments = {"--longOptionA", "-a=value"};
        String[] expected = {"--longOptionA", "-a=value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_shortOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Short Option A");
        String[] arguments = {"-a", "arg1", "arg2"};
        String[] expected = {"-a", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_longOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A", "Long Option A");
        String[] arguments = {"--longOptionA", "arg1", "arg2"};
        String[] expected = {"--longOptionA", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_doublyShortOption() {
        Options options = new Options();
        options.addOption("aa", true, "Option AA", "Short Option AA");
        String[] arguments = {"--", "-aa"};
        String[] expected = {"--", "-aa"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_doublyLongOption() {
        Options options = new Options();
        options.addOption("longLongOption", true, "Long Long Option", "Long Long Option");
        String[] arguments = {"--", "--longLongOption"};
        String[] expected = {"--", "--longLongOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_doublyShortLongOption() {
        Options options = new Options();
        options.addOption("ll", true, "Long Long Option", "Long Long Option");
        String[] arguments = {"--", "-ll"};
        String[] expected = {"--", "-ll"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_doublyLongShortOption() {
        Options options = new Options();
        options.addOption("l", true, "Option L", "Option L");
        options.addOption("ll", true, "Long Long Option", "Long Long Option");
        String[] arguments = {"--", "--ll"};
        String[] expected = {"--", "--ll"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_emptyOption() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_emptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_emptyArgumentsStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = true;
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_argumentsWithNonOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        boolean stopAtNonOption = false;
        String[] expected = {"arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGnuParser_flatten_argumentsWithNonOptionStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        boolean stopAtNonOption = true;
        String[] expected = {"arg1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }
}