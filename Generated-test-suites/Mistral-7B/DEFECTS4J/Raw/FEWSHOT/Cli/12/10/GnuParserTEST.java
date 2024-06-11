package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg1"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options().addOption("o", false, "Option o");
        String[] arguments = {"-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o"}, result);
        assertSame(options.findOption("o"), result[0]);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Options options = new Options().addOption("o", true, "Option o");
        String[] arguments = {"-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "value"}, result);
        assertSame(options.findOption("o"), result[0]);
        assertEquals("value", options.getOptionValue("o"));
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options().addOption("o1", false, "Option o1")
                .addOption("o2", false, "Option o2");
        String[] arguments = {"-o1", "-o2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o1", "-o2"}, result);
        assertSame(options.findOption("o1"), result[0]);
        assertSame(options.findOption("o2"), result[1]);
    }

    @Test
    public void testFlattenSingleArgumentFollowedByOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"arg1", "-o"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenDoubleDashFollowedByOption() {
        Options options = new Options();
        String[] arguments = {"--", "-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-o"}, result);
    }

    @Test
    public void testFlattenDoubleDashFollowedByArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenEmptyOptionFollowedByArguments() {
        Options options = new Options();
        String[] arguments = {"-", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenEmptyOptionFollowedByOption() {
        Options options = new Options();
        String[] arguments = {"-", "-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-", "-o"}, result);
    }

    @Test
    public void testFlattenSingleHyphenOption() {
        Options options = new Options();
        String[] arguments = {"-"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenSingleHyphenOptionFollowedByArguments() {
        Options options = new Options();
        String[] arguments = {"-", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenDoubleHyphenOption() {
        Options options = new Options();
        String[] arguments = {"--"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenDoubleHyphenOptionFollowedByArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenDoubleHyphenOptionFollowedByOption() {
        Options options = new Options();
        String[] arguments = {"--", "-o"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-o"}, result);
    }

    @Test
    public void testFlattenDoubleHyphenOptionFollowedByOptionWithValue() {
        Options options = new Options().addOption("o", true, "Option o");
        String[] arguments = {"--", "-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-o", "value"}, result);
        assertSame(options.findOption("o"), result[1]);
        assertEquals("value", options.getOptionValue("o"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFlattenStopAtNonOptionSingleNonOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "-o"};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, true);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFlattenStopAtNonOptionMultipleNonOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "-o1", "-o2"};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlattenStopAtNonOptionReachesEnd() {
        Options options = new Options();
        String[] arguments = {"arg1", "-o", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "-o"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionReachesEndWithDoubleDash() {
        Options options = new Options();
        String[] arguments = {"arg1", "--", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "--"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionReachesEndWithEmptyOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "-", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"arg1", "-"}, result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFlattenDoubleDashFollowedByNonOptionAndValue() {
        Options options = new Options();
        String[] arguments = {"--", "arg1=value"};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options().addOption("o", true, "Option o");
        String[] arguments = {"-o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "value"}, result);
        assertSame(options.findOption("o"), result[0]);
        assertEquals("value", options.getOptionValue("o"));
    }

    @Test
    public void testFlattenOptionWithValueDoubleDash() {
        Options options = new Options().addOption("o", true, "Option o");
        String[] arguments = {"--o", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--o", "value"}, result);
        assertSame(options.findOption("o"), result[0]);
        assertEquals("value", options.getOptionValue("o"));
    }

    @Test
    public void testFlattenOptionWithValueEmptyOption() {
        Options options = new Options().addOption("o", true, "Option o");
        String[] arguments = {"-", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-", "value"}, result);
        assertSame(options.findOption("o"), result[0]);
        assertEquals("value", options.getOptionValue("o"));
    }
}