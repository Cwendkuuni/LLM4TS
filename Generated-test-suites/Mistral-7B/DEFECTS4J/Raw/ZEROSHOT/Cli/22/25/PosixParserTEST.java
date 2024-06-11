package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for the PosixParser class.
 */
public class PosixParserTest
{
    /**
     * Test case for flattening a simple command line argument list
     * with no options.
     */
    @Test
    public void testSimpleFlattening()
    {
        String[] args = new String[]{"java", "MyMainClass"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(args, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with single option.
     */
    @Test
    public void testSingleOptionFlattening()
    {
        String[] args = new String[]{"java", "-a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "-a", "value"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with multiple options.
     */
    @Test
    public void testMultipleOptionsFlattening()
    {
        String[] args = new String[]{"java", "-a", "1", "-b", "2", "-c", "3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "-a", "1", "--", "-b", "2", "--", "-c", "3"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with long options.
     */
    @Test
    public void testLongOptionsFlattening()
    {
        String[] args = new String[]{"java", "--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "--long-option", "value"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with long options and values.
     */
    @Test
    public void testLongOptionsWithValuesFlattening()
    {
        String[] args = new String[]{"java", "--long-option1", "value1", "--long-option2=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "--long-option1", "value1", "--", "--long-option2", "value2"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with mixed short and long options.
     */
    @Test
    public void testMixedOptionsFlattening()
    {
        String[] args = new String[]{"java", "-a", "1", "--long-option", "value", "-b", "2", "-c", "3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "-a", "1", "--", "--long-option", "value", "--", "-b", "2", "--", "-c", "3"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with multiple values for an option.
     */
    @Test
    public void testMultipleValuesForOptionFlattening()
    {
        String[] args = new String[]{"java", "-a", "value1", "value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "-a", "value1", "--", "-a", "value2"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with a single non-option token.
     */
    @Test
    public void testSingleNonOptionFlattening()
    {
        String[] args = new String[]{"java", "myfile.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "myfile.txt"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with multiple non-option tokens.
     */
    @Test
    public void testMultipleNonOptionsFlattening()
    {
        String[] args = new String[]{"java", "myfile1.txt", "myfile2.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--", "myfile1.txt", "--", "myfile2.txt"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with an empty argument.
     */
    @Test
    public void testEmptyArgumentFlattening()
    {
        String[] args = new String[]{"java", ""};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--"}, result);
    }

    /**
     * Test case for flattening a command line argument list
     * with a command line option that has no corresponding option.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnknownOptionFlattening()
    {
        String[] args = new String[]{"java", "-x", "value"};
        PosixParser parser = new PosixParser();
        parser.flatten(null, args, false);
    }
}