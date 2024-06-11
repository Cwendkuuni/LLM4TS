package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "action", true, "Action to perform");
        options.addOption("b", "boolean", false, "Boolean flag");
        options.addOption("c", "count", true, "Count of something");
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] arguments = new String[]{"arg1"};
        String[] expected = new String[]{"arg1"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] arguments = new String[]{"--"};
        String[] expected = new String[]{"--"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOption() {
        String[] arguments = new String[]{"-a", "valueA"};
        String[] expected = new String[]{"-a", "valueA"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidOptionWithArgument() {
        String[] arguments = new String[]{"-a", "valueA", "valueB"};
        String[] expected = new String[]{"-a", "valueA", "valueB"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] arguments = new String[]{"-x", "valueA"};
        new PosixParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_StopAtNonOption() {
        String[] arguments = new String[]{"arg1", "-b", "arg3"};
        new PosixParser().flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_StopAtNonOption_InvalidOption() {
        String[] arguments = new String[]{"arg1", "-x", "arg3"};
        new PosixParser().flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_StopAtNonOption_MultipleInvalidOptions() {
        String[] arguments = new String[]{"arg1", "-x", "-y", "arg3"};
        new PosixParser().flatten(options, arguments, true);
    }

    @Test
    public void testFlatten_Bursting() {
        String[] arguments = new String[]{"-abc"};
        String[] expected = new String[]{"-a", "-c"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_Bursting_InvalidOption() {
        String[] arguments = new String[]{"-xabc"};
        new PosixParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_Bursting_MultipleOptions() {
        String[] arguments = new String[]{"-abcde"};
        String[] expected = new String[]{"-a", "-c", "-d", "-e"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_Bursting_MixedValidAndInvalidOptions() {
        String[] arguments = new String[]{"-a-x-c"};
        new PosixParser().flatten(options, arguments, false);
    }
}