package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = new String[0];
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"arg"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = new String[]{"-a"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = new String[]{"--longOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = new String[]{"-a", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlattenSingleLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = new String[]{"--longOption", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenMultipleArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenOptionsInBetweenArguments() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = new String[]{"arg1", "-a", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "-a", "arg2"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = new String[]{"arg1", "-a", "arg2", "nonOption"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "-a"}, result);
    }

    @Test
    public void testFlattenDuplicateOptions() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = new String[]{"-a", "-a"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "-a"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = new String[]{"--longOption=value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--longOption=value"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndArguments() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = new String[]{"--longOption=value", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption=value", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndNoArguments() {
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] arguments = new String[]{"--longOption=value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--longOption=value"}, result);
    }

    @Test
    public void testFlattenMultipleLongOptionsWithValues() {
        Options options = new Options();
        options.addOption("longOption1", true, "long option 1");
        options.addOption("longOption2", true, "long option 2");
        String[] arguments = new String[]{"--longOption1=value1", "--longOption2=value2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption1=value1", "--longOption2=value2"}, result);
    }

    @Test
    public void testFlattenMultipleLongOptionsWithValuesAndArguments() {
        Options options = new Options();
        options.addOption("longOption1", true, "long option 1");
        options.addOption("longOption2", true, "long option 2");
        String[] arguments = new String[]{"--longOption1=value1", "--longOption2=value2", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption1=value1", "--longOption2=value2", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenSingleDashFollowedByOption() {
        Options options = new Options();
        options.addOption("a", true, "a option");
        String[] arguments = new String[]{"-", "-a"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-", "-a"}, result);
    }

    @Test
    public void testFlattenSingleDashFollowedByArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"-", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenMultipleDashesFollowedByArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }
}