package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", "defaultValue", "Option D");
        options.addOption("e", "value", 1, "Option E");
        options.addOption("f", "value", true, "Option F");
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SingleValueOption() {
        String[] args = {"-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_MultiValueOption() {
        String[] args = {"-a", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "value1", "value2"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"-a", "-b", "-c=3", "-d", "-e", "value", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c", "3", "-d", "defaultValue", "-e", "value"}, result);
    }

    @Test
    public void testFlatten_MultiValueOptionWithargs() {
        String[] args = {"-a", "-b", "-c=3", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c", "3", "--", "arg1", "arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-z"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlatten_MixedShortAndLongOptions() {
        String[] args = {"-a", "--longOption", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "--longOption", "-b"}, result);
    }
}