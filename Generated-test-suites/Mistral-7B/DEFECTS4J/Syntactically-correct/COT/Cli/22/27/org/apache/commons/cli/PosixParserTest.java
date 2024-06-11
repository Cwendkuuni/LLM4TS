package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for PosixParser
 */
public class PosixParserTest
{
    @Test
    public void testFlatten_SimpleOptions()
    {
        String[] args = {"java", "--option1", "--option2=value2", "--option3=value3"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", true, "o3");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--option1", "true", "--option2", "value2", "--option3", "value3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptions()
    {
        String[] args = {"java", "arg1", "arg2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption()
    {
        String[] args = {"java", "--option1", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"java", "--option1", "true", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption()
    {
        String[] args = {"java", "--invalidOption"};
        Options options = new Options();
        Options invalidOptions = new Options();
        invalidOptions.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        parser.flatten(invalidOptions, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_OptionWithNoArg_EmptyValue()
    {
        String[] args = {"java", "--option1", ""};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_OptionWithArg_EmptyValue()
    {
        String[] args = {"java", "--option1=", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1").required(false).argName("value");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--option1", "", "--option2", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg_ValuePresent()
    {
        String[] args = {"java", "--option1=value1", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1").required(false).argName("value");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--option1", "value1", "--option2", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg()
    {
        String[] args = {"java", "--long-option=value1", "--long-option2=value2"};
        Options options = new Options();
        options.addOption("longOption", true, "li").required(false).argName("value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--long-option", "value1", "--long-option", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_LongOptionWithArg()
    {
        String[] args = {"java", "--long-option=value1", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOption", true, "li").required(false).argName("value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"java", "--long-option", "value1", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleLongOptionsWithArg()
    {
        String[] args = {"java", "--long-option1=value1", "--long-option2=value2", "--long-option3=value3"};
        Options options = new Options();
        options.addOption("longOption1", true, "li1").required(false).argName("value");
        options.addOption("longOption2", true, "li2").required(false).argName("value");
        options.addOption("longOption3", true, "li3").required(false).argName("value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--long-option1", "value1", "--long-option2", "value2", "--long-option3", "value3"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutArg()
    {
        String[] args = {"java", "--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "li").required(false);

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--long-option"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg_EmptyValue()
    {
        String[] args = {"java", "--long-option="};
        Options options = new Options();
        options.addOption("longOption", true, "li").required(false).argName("value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--long-option"};
        assertArrayEquals(expected, result);
    }
}