package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest
{
    private PosixParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_EmptyArguments_NoOption()
    {
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_EmptyArguments_WithOption()
    {
        options.addOption("test", false, null, "Test Option");
        String[] arguments = new String[0];
        String[] expected = new String[]{"--test"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SimpleOption()
    {
        options.addOption("test", false, null, "Test Option");
        String[] arguments = new String[]{"--test"};
        String[] expected = new String[]{"--test"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOption()
    {
        options.addOption("longOption", false, null, "Long Option Test");
        String[] arguments = new String[]{"--longOption"};
        String[] expected = new String[]{"--longOption"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithValue()
    {
        options.addOption("longOptionValue", true, null, "Long Option Value Test");
        String[] arguments = new String[]{"--longOptionValue", "testValue"};
        String[] expected = new String[]{"--longOptionValue", "testValue"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_MultipleOptions()
    {
        options.addOption("test", false, null, "Test Option");
        options.addOption("longOption", false, null, "Long Option Test");
        String[] arguments = new String[]{"--test", "--longOption"};
        String[] expected = new String[]{"--test", "--longOption"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOptionWithValue()
    {
        options.addOption("longOptionValue", true, null, "Long Option Value Test");
        String[] arguments = new String[]{"--longOptionValue", "testValue1", "--longOptionValue", "testValue2"};
        String[] expected = new String[]{"--longOptionValue", "testValue1", "--longOptionValue", "testValue2"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption()
    {
        options.addOption("test", false, null, "Test Option");
        String[] arguments = new String[]{"--invalidOption"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidLongOption()
    {
        options.addOption("test", false, null, "Test Option");
        String[] arguments = new String[]{"--test--longOption"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MixedOptions()
    {
        options.addOption("test", false, null, "Test Option");
        String[] arguments = new String[]{"--test", "--longOption", "--test"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MixedLongOptions()
    {
        options.addOption("test", false, null, "Test Option");
        options.addOption("longOption", false, null, "Long Option Test");
        String[] arguments = new String[]{"--test", "--longOption--test"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_StopAtNonOption()
    {
        options.addOption("test", false, null, "Test Option");
        String[] arguments = new String[]{"--test", "testArg", "--nonOption"};
        parser.flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_StopAtNonOptionWithLongOption()
    {
        options.addOption("test", false, null, "Test Option");
        options.addOption("longOptionValue", true, null, "Long Option Value Test");
        String[] arguments = new String[]{"--test", "testArg", "--longOptionValue", "testValue"};
        parser.flatten(options, arguments, true);
    }
}