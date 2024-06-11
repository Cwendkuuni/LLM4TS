package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
    }

    /**
     * Tests the basic functionality of the flatten method.
     */
    @Test
    public void testFlattenBasic() {
        String[] args = {"--optionA", "-a", "--optionB=valueB", "-b", "valueC", "--optionC=value1 argument1", "-c", "value2"};
        String[] expected = {"--optionA", "-a", "--optionB", "valueB", "-b", "--optionC", "value1", "argument1", "-c", "value2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of long options with no arguments.
     */
    @Test
    public void testFlattenLongOptionsNoArg() {
        String[] args = {"--optionA", "--optionB"};
        String[] expected = {"--optionA", "--optionB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of long options with arguments.
     */
    @Test
    public void testFlattenLongOptionsWithArg() {
        String[] args = {"--optionB=valueB"};
        String[] expected = {"--optionB", "valueB"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of single character options.
     */
    @Test
    public void testFlattenSingleCharacterOptions() {
        String[] args = {"-a", "-b=valueB", "-c", "--optionC=value1 argument1", "-c"};
        String[] expected = {"-a", "-b", "--optionC", "value1", "argument1"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of single character options with arguments.
     */
    @Test
    public void testFlattenSingleCharacterOptionsWithArg() {
        String[] args = {"-a", "valueA", "-b=valueB", "-c", "--optionC=value1 argument1", "-c"};
        String[] expected = {"-a", "valueA", "-b", "--optionC", "value1", "argument1"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of long options with multiple arguments.
     */
    @Test
    public void testFlattenLongOptionsWithMultipleArgs() {
        String[] args = {"--optionC=value1 argument1 argument2"};
        String[] expected = {"--optionC", "value1", "argument1", "argument2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of long options with no arguments and stopAtNonOption set to true.
     */
    @Test
    public void testFlattenLongOptionsNoArgWithStopAtNonOption() {
        String[] args = {"--optionA", "--optionB", "otherArgument"};
        String[] expected = {"--optionA", "--optionB", "otherArgument"};

        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Tests the handling of long options with arguments and stopAtNonOption set to true.
     */
    @Test(expected = ParseException.class)
    public void testFlattenLongOptionsWithArgWithStopAtNonOption() {
        String[] args = {"--optionB=valueB", "otherArgument"};

        parser.flatten(options, args, true);
    }

    /**
     * Tests the handling of single character options with stopAtNonOption set to true.
     */
    @Test(expected = ParseException.class)
    public void testFlattenSingleCharacterOptionsWithStopAtNonOption() {
        String[] args = {"-a", "otherArgument"};

        parser.flatten(options, args, true);
    }

    /**
     * Tests the handling of single character options with arguments and stopAtNonOption set to true.
     */
    @Test(expected = ParseException.class)
    public void testFlattenSingleCharacterOptionsWithArgAndStopAtNonOption() {
        String[] args = {"-a", "valueA", "otherArgument"};

        parser.flatten(options, args, true);
    }

    /**
     * Tests the handling of long options with multiple arguments and stopAtNonOption set to true.
     */
    @Test(expected = ParseException.class)
    public void testFlattenLongOptionsWithMultipleArgsAndStopAtNonOption() {
        String[] args = {"--optionC=value1 argument1 argument2", "otherArgument"};

        parser.flatten(options, args, true);
    }
}