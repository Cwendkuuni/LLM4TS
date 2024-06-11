package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private Options options;

    /**
     * Set up before each test.
     */
    @Before
    public void setUp() {
        options = new Options();
        options.addOption("i", "input", true, "input value");
        options.addOption("o", "output", true, "output value");
    }

    /**
     * Test flatten method with no arguments.
     */
    @Test
    public void testFlattenNoArguments() {
        String[] arguments = new String[0];
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test flatten method with single argument.
     */
    @Test
    public void testFlattenSingleArgument() {
        String[] arguments = {"arg1"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    /**
     * Test flatten method with -- argument.
     */
    @Test
    public void testFlattenDoubleDash() {
        String[] arguments = {"--"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    /**
     * Test flatten method with single hyphen argument.
     */
    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = {"-"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    /**
     * Test flatten method with option argument.
     */
    @Test
    public void testFlattenOptionArgument() {
        String[] arguments = {"-i", "inputValue"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-i", "inputValue"}, result);
    }

    /**
     * Test flatten method with option argument value.
     */
    @Test
    public void testFlattenOptionArgumentValue() {
        String[] arguments = {"-i", "inputValue", "--output", "outputValue"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-i", "inputValue", "--output", "outputValue"}, result);
    }

    /**
     * Test flatten method with burst token.
     */
    @Test
    public void testFlattenBurstToken() {
        String[] arguments = {"-io"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-i", "-o"}, result);

        String[] argumentsWithValue = {"-io", "value"};
        String[] resultWithValue = new PosixParser().flatten(options, argumentsWithValue, false);
        Assert.assertArrayEquals(new String[]{"-i", "-o", "value"}, resultWithValue);
    }

    /**
     * Test flatten method with stopAtNonOption.
     */
    @Test(expected = ParseException.class)
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"arg1", "-o", "arg3"};
        new PosixParser().flatten(options, arguments, true);
    }

    /**
     * Test flatten method with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions() {
        String[] arguments = {"-i", "inputValue1", "-o", "outputValue1", "-i", "inputValue2"};
        String[] result = new PosixParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-i", "inputValue1", "-o", "outputValue1", "-i", "inputValue2"}, result);
    }
}