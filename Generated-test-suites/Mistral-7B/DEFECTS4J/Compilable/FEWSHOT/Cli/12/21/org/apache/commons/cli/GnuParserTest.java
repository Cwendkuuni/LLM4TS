package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testGnuParserFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testGnuParserFlattenSingleOption() {
        Options options = new Options();
        options.addOption("D", true, "debug");
        String[] arguments = {"-D"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-D"}, result);
    }

    @Test
    public void testGnuParserFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testGnuParserFlattenOptionWithValue() {
        Options options = new Options();
        options.addOption("D", true, "debug");
        String[] arguments = {"-D", "true"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-D", "true"}, result);
    }

    @Test
    public void testGnuParserFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--"}, result);
    }

    @Test
    public void testGnuParserFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("D", true, "debug");
        options.addOption("V", false, "verbose");
        String[] arguments = {"-D", "-V"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-D", "-V"}, result);
    }

    @Test
    public void testGnuParserFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOption("A", true, "action");
        String[] arguments = {"-A", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-A", "value"}, result);
    }

    @Test
    public void testGnuParserFlattenOptionWithTwoArguments() {
        Options options = new Options();
        options.addOption("C", true, "config");
        String[] arguments = {"-C", "file1", "file2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-C", "file1", "file2"}, result);
    }

    @Test
    public void testGnuParserFlattenMultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("A", true, "action");
        options.addOption("C", true, "config");
        String[] arguments = {"-A", "value1", "-C", "file1", "-C", "file2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-A", "value1", "-C", "file1", "-C", "file2"}, result);
    }

    @Test
    public void testGnuParserFlattenSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-"}, result);
    }

    @Test
    public void testGnuParserFlattenMultipleSingleHyphens() {
        Options options = new Options();
        String[] arguments = {"-", "-"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-", "-"}, result);
    }

    @Test
    public void testGnuParserFlattenDoubleOption() {
        Options options = new Options();
        String[] arguments = {"--foo"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testGnuParserFlattenTripleOption() {
        Options options = new Options();
        String[] arguments = {"---foo"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"---foo"}, result);
    }

    @Test
    public void testGnuParserFlattenSingleArgumentWithDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--", "arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--", "arg"}, result);
    }

    @Test
    public void testGnuParserFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-D", "value", "nonOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"-D", "value"}, result);
    }

    @Test
    public void testGnuParserFlattenStopAtNonOptionWithOptions() {
        Options options = new Options();
        options.addOption("D", true, "debug");
        String[] arguments = {"-D", "value", "nonOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"-D", "value"}, result);
    }

    @Test
    public void testGnuParserFlattenStopAtNonOptionWithArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "nonOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"arg1", "arg2"}, result);
    }
}