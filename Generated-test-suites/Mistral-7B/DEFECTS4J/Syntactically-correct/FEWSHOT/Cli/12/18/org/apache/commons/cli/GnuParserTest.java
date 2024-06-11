package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenWithNullOptions() {
        String[] arguments = new String[]{"--foo", "-Dprop=val", "-a", "--", "-b", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(null, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = new String[] {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenEmptyOptions() {
        String[] arguments = new String[]{"--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = new String[]{"--foo", "-Dprop=val", "-a", "--", "-b", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, true);
        assertArrayEquals(new String[]{"--foo", "-Dprop=val", "-a"}, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        String[] arguments = new String[]{"arg1"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        String[] arguments = new String[]{"-a"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleShortOption() {
        String[] arguments = new String[]{"-a"};
        GnuParser parser = new GnuParser();
        String[] options = new Options();
        options.addOption("a", true, "Short Option a");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        String[] arguments = new String[]{"--longOption"};
        GnuParser parser = new GnuParser();
        String[] options = new Options();
        options.addOption("longOption", true, "Long Option longOption");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        String[] arguments = new String[]{"--foo=value1"};
        GnuParser parser = new GnuParser();
        String[] options = new Options();
        options.addOption("foo", true, "Option foo");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "value1"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] arguments = new String[]{"--foo", "--bar", "-Dprop=val", "-a"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "-Dprop=val", "-a"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] arguments = new String[]{"--longOption=value1"};
        GnuParser parser = new GnuParser();
        String[] options = new Options();
        options.addOption("longOption", true, "Long Option longOption");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "value1"}, result);
    }

    @Test
    public void testFlattenDoubleShortOption() {
        String[] arguments = new String[]{"--", "-aa"};
        GnuParser parser = new GnuParser();
        String[] options = new Options();
        options.addOption("a", true, "Short Option a");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-a", "-a"}, result);
    }

    @Test
    public void testFlattenDoubleShortOptionWithValue() {
        String[] arguments = new String[]{"--", "-aa=value"};
        GnuParser parser = new GnuParser();
        String[] options = new Options();
        options.addOption("a", true, "Short Option a");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-a", "value"}, result);
    }

    @Test
    public void testFlattenDashAsOption() {
        String[] arguments = new String[]{"--", "-"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--", "-"}, result);
    }

    @Test
    public void testFlattenDoubleDashAsOption() {
        String[] arguments = new String[]{"--", "--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--", "--"}, result);
    }

    @Test
    public void testFlattenTrailingArguments() {
        String[] arguments = new String[]{"--foo", "-Dprop=val", "-a", "--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--foo", "-Dprop=val", "-a", "--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenTrailingArgumentsStopAtNonOption() {
        String[] arguments = new String[]{"--foo", "-Dprop=val", "-a", "--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, true);
        assertArrayEquals(new String[]{"--foo", "-Dprop=val", "-a"}, result);
    }

    @Test
    public void testFlattenMissingOption() {
        String[] arguments = new String[]{"--missingOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(new String[]{"--missingOption"}, result);
    }

    @Test
    public void testFlattenMissingOptionStopAtNonOption() {
        String[] arguments = new String[]{"--missingOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, true);
        assertArrayEquals(new String[]{}, result);
    }
}