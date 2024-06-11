package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = new String[0];

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenWithSingleDashAndSingleCharacterOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Print debug information.");
        options.addOption(option);

        String[] arguments = {"--", "-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-d"}, result);
    }

    @Test
    public void testFlattenWithSingleDashAndMultiCharacterOption() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", false, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"--", "-db"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-db"}, result);
    }

    @Test
    public void testFlattenWithSingleCharacterOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Print debug information.");
        options.addOption(option);

        String[] arguments = {"-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-d", result[0]);

        options.addOption(new Option("e", "encode", false, "Encode the output."));

        String[] arguments2 = {"-d", "-e"};

        String[] result2 = parser.flatten(options, arguments2, false);
        assertArrayEquals(new String[]{"-d", "-e"}, result2);
    }

    @Test
    public void testFlattenWithSingleCharacterOptionWithValue() {
        Options options = new Options();
        Option option = new Option("d", "debug", true, "Print debug information.");
        options.addOption(option);

        String[] arguments = {"-d", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-d", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithSingleCharacterOptionAndStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Print debug information.");
        options.addOption(option);

        String[] arguments = {"-d", "value", "-e"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-d", "value"}, result);
    }

    @Test
    public void testFlattenWithMultiCharacterOption() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", false, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"-db"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-db", result[0]);
    }

    @Test
    public void testFlattenWithMultiCharacterOptionWithValue() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", true, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"-db", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("-db", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithMultiCharacterOptionAndStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", false, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"-db", "value", "-e"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-db", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        Option option = new Option("longOption", "long opt", false, "A long option.");
        options.addOption(option);

        String[] arguments = {"--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--longOption", result[0]);
    }

    @Test
    public void testFlattenWithLongOptionAndValue() {
        Options options = new Options();
        Option option = new Option("longOption", "long opt", true, "A long option.");
        options.addOption(option);

        String[] arguments = {"--longOption", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithLongOptionAndStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("longOption", "long opt", false, "A long option.");
        options.addOption(option);

        String[] arguments = {"--longOption", "value", "-e"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlattenWithDoubleDashAndArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithEmptyString() {
        Options options = new Options();
        String[] arguments = {"", ""};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithEmptyStringAndArguments() {
        Options options = new Options();
        String[] arguments = {"", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        String[] arguments = null;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithNullArgumentsAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = null;

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertNull(result);
    }

    @Test
    public void testFlattenWithOnlyDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlattenWithOnlyDoubleDashAndArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.addAll(Arrays.asList("arg1", "arg2", "arg3"));
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithDoubleDashAndSingleDash() {
        Options options = new Options();
        String[] arguments = {"--", "-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("-d", result[1]);
    }

    @Test
    public void testFlattenWithDoubleDashAndSingleCharacterOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Print debug information.");
        options.addOption(option);

        String[] arguments = {"--", "-d"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("-d", result[1]);
    }

    @Test
    public void testFlattenWithDoubleDashAndMultiCharacterOption() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", false, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"--", "-db"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("-db", result[1]);
    }

    @Test
    public void testFlattenWithDoubleDashAndSingleCharacterOptionWithValue() {
        Options options = new Options();
        Option option = new Option("d", "debug", true, "Print debug information.");
        options.addOption(option);

        String[] arguments = {"--", "-d", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenWithDoubleDashAndMultiCharacterOptionWithValue() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", true, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"--", "-db", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("-db", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenWithDoubleDashAndLongOption() {
        Options options = new Options();
        Option option = new Option("longOption", "long opt", false, "A long option.");
        options.addOption(option);

        String[] arguments = {"--", "--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("--longOption", result[1]);
    }

    @Test
    public void testFlattenWithDoubleDashAndLongOptionWithValue() {
        Options options = new Options();
        Option option = new Option("longOption", "long opt", true, "A long option.");
        options.addOption(option);

        String[] arguments = {"--", "--longOption", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("--longOption", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenWithDoubleDashAndMultiCharacterOptionWithValue() {
        Options options = new Options();
        Option option = new Option("db", "debug-binary", true, "Print debug binary information.");
        options.addOption(option);

        String[] arguments = {"--", "-db", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("-db", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenWithDoubleDashAndLongOptionWithValueAndStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("longOption", "long opt", true, "A long option.");
        options.addOption(option);

        String[] arguments = {"--", "--longOption", "value", "-e"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals("--", result[0]);
        assertEquals("--longOption", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenWithDoubleDashAndOnlyDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--", "--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--", result[0]);
        assertEquals("--", result[1]);
    }

    @Test
    public void testFlattenWithDoubleDashAndOnlyDoubleDashAndArguments() {
        Options options = new Options();
        String[] arguments = {"--", "--", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("--");
        expected.addAll(Arrays.asList("arg1", "arg2", "arg3"));
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}