package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg1"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option");
        String[] arguments = {"-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o"}, result);
        assertArrayEquals(new String[]{"value"}, parser.parse(options, result));
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option");
        options.addOption("b", "boolOpt", false, "A boolean option");
        String[] arguments = {"-o", "value1", "--", "-b", "true", "-o", "value2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "value1", "--", "-b", "", "-o", "value2"}, result);
        assertArrayEquals(new String[]{"value1"}, parser.parse(options, new String[]{"value1"}));
        assertArrayEquals(new String[]{"value2"}, parser.parse(options, new String[]{"value2"}));
        assertFalse(parser.parse(options, new String[0]).length > 0);
        assertArrayEquals(new String[]{"true"}, parser.parse(options, new String[]{"", "true"}));
    }

    @Test
    public void testFlatten_LongOptions() {
        Options options = new Options();
        options.addOption("l", "longOpt", true, "A long option");
        String[] arguments = {"--longOpt", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOpt"}, result);
        assertArrayEquals(new String[]{"value"}, parser.parse(options, new String[]{"value"}));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option");
        String[] arguments = {"-o", "value1", "arg1", "-o", "value2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-o", "value1", "arg1"}, result);
    }

    @Test
    public void testFlatten_TrailingHyphen() {
        Options options = new Options();
        String[] arguments = {"-o", "value1", "--", "-o", "value2-"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "value1", "--", "-o", "value2"}, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        Options options = new Options();
        String[] arguments = {"--", "-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }
}