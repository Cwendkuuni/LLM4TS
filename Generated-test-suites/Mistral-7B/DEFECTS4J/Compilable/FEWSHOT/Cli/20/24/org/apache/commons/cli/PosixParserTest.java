package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testPosixParser_flatten_empty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testPosixParser_flatten_simple() {
        Options options = new Options();
        String[] arguments = {"foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testPosixParser_flatten_longOption() {
        Options options = new Options();
        Option longOption = new Option("long", "longOption", false, "description");
        options.addOption(longOption);
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testPosixParser_flatten_longOptionWithValue() {
        Options options = new Options();
        Option longOption = new Option("long", "longOption", true, "description");
        options.addOption(longOption);
        String[] arguments = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testPosixParser_flatten_shortOption() {
        Options options = new Options();
        Option shortOption = new Option("s", "shortOption", false, "description");
        options.addOption(shortOption);
        String[] arguments = {"-s"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-s"}, result);
    }

    @Test
    public void testPosixParser_flatten_shortOptionWithValue() {
        Options options = new Options();
        Option shortOption = new Option("s", "shortOption", true, "description");
        options.addOption(shortOption);
        String[] arguments = {"-s", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-s", "value"}, result);
    }

    @Test
    public void testPosixParser_flatten_multipleShortOptions() {
        Options options = new Options();
        Option shortOption1 = new Option("s1", "shortOption1", false, "description1");
        Option shortOption2 = new Option("s2", "shortOption2", false, "description2");
        options.addOption(shortOption1);
        options.addOption(shortOption2);
        String[] arguments = {"-s1", "-s2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-s1", "-s2"}, result);
    }

    @Test
    public void testPosixParser_flatten_shortOptionWithLongOption() {
        Options options = new Options();
        Option shortOption = new Option("s", "shortOption", false, "description");
        Option longOption = new Option("l", "longOption", false, "description");
        options.addOption(shortOption);
        options.addOption(longOption);
        String[] arguments = {"-s", "--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-s", "--longOption"}, result);
    }

    @Test
    public void testPosixParser_flatten_shortOptionWithValueAndLongOption() {
        Options options = new Options();
        Option shortOption = new Option("s", "shortOption", true, "description");
        Option longOption = new Option("l", "longOption", false, "description");
        options.addOption(shortOption);
        options.addOption(longOption);
        String[] arguments = {"-s", "value", "--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-s", "value", "--longOption"}, result);
    }

    @Test
    public void testPosixParser_flatten_multipleShortOptionsWithValues() {
        Options options = new Options();
        Option shortOption1 = new Option("s1", "shortOption1", true, "description1");
        Option shortOption2 = new Option("s2", "shortOption2", true, "description2");
        options.addOption(shortOption1);
        options.addOption(shortOption2);
        String[] arguments = {"-s1", "value1", "-s2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-s1", "value1", "-s2", "value2"}, result);
    }

    @Test
    public void testPosixParser_flatten_stopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"foo", "-s", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"foo", "-s", "bar"}, result);
    }

    @Test
    public void testPosixParser_flatten_invalidOption() {
        Options options = new Options();
        String[] arguments = {"foo", "-x"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"foo", "-x"}, result);
    }

    @Test
    public void testPosixParser_flatten_multipleInvalidOptions() {
        Options options = new Options();
        String[] arguments = {"foo", "-x", "-y"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"foo", "-x", "-y"}, result);
    }

    @Test
    public void testPosixParser_flatten_burstToken() {
        Options options = new Options();
        String[] arguments = {"--long-option-with-value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--long-option-with-value"}, result);

        String[] arguments2 = {"--long-option"};
        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options, arguments2, false);
        assertEquals(new String[]{"--long-option"}, result2);

        String[] arguments3 = {"--long-option--"};
        PosixParser parser3 = new PosixParser();
        String[] result3 = parser3.flatten(options, arguments3, false);
        assertEquals(new String[]{"--long-option--"}, result3);
    }

    @Test
    public void testPosixParser_flatten_burstTokenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--long-option--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(new String[]{"--long-option--"}, result);

        String[] arguments2 = {"--long-option"};
        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options, arguments2, true);
        assertEquals(new String[]{"--long-option"}, result2);
    }
}