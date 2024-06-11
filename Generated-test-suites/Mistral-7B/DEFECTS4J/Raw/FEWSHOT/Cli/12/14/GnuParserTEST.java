package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] arguments = new String[] {"arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
        assertEquals("arg3", result[2]);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information.");

        String[] arguments = new String[] {"-D", "arg"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-D", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", "longOptionDescription", false, "Long option description.");

        String[] arguments = new String[] {"--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--longOption", result[0]);
    }

    @Test
    public void testFlattenSingleShortOption() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information.");

        String[] arguments = new String[] {"-D"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-D", result[0]);
    }

    @Test
    public void testFlattenSingleShortOptionWithValue() {
        Options options = new Options();
        options.addOption("D", "debug", true, "Print debug information.");

        String[] arguments = new String[] {"-D", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-D", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information.");
        options.addOption("V", "verbose", true, "Print verbose information.");

        String[] arguments = new String[] {"-D", "--V"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-D", result[0]);
        assertEquals("--V", result[1]);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", false, false, "Long option description.");
        options.addOption("longOptionValue", true, false, "Long option value description.");

        String[] arguments = new String[] {"--longOption=value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information.");

        String[] arguments = new String[] {"-D", "arg1", "nonOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-D", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();

        String[] arguments = new String[] {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();

        String[] arguments = new String[] {"--", "arg1", "--", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("arg2", result[3]);
    }
}