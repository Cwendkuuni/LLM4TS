package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for GnuParser class
 */
public class GnuParserTest {

    /**
     * Test flatten method with no options and no arguments
     */
    @Test
    public void testFlattenNoOptionsOrArguments() {
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        String[] expected = {};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with only options
     */
    @Test
    public void testFlattenOnlyOptions() {
        String[] arguments = {"--foo", "--bar", "--baz"};
        GnuParser parser = new GnuParser();
        String[] expected = {"--foo", "--bar", "--baz"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with only arguments
     */
    @Test
    public void testFlattenOnlyArguments() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with an option and an argument
     */
    @Test
    public void testFlattenOptionAndArgument() {
        String[] arguments = {"-Dproperty=value", "arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] expected = {"-D", "property", "value", "arg1", "arg2"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with an option and multiple arguments
     */
    @Test
    public void testFlattenOptionAndMultipleArguments() {
        String[] arguments = {"-Dproperty=value1", "arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        String[] expected = {"-D", "property", "value1", "arg1", "arg2", "arg3"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with an option and no arguments
     */
    @Test
    public void testFlattenOptionNoArguments() {
        String[] arguments = {"-Dproperty"};
        GnuParser parser = new GnuParser();
        String[] expected = {"-D", "property"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with an option and an option with an argument
     */
    @Test
    public void testFlattenOptionWithOptionAndArgument() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", value -> System.out.println("bar value: " + value), "b");

        String[] arguments = {"--foo", "--bar=123"};
        GnuParser parser = new GnuParser();
        try {
            String[] expected = {"--foo", "--bar", "123"};
            String[] result = parser.flatten(options, arguments, false);
            assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test flatten method with an option and an argument, and then a non-option
     */
    @Test(expected = ParseException.class)
    public void testFlattenOptionWithArgumentAndNonOption() {
        String[] arguments = {"--foo", "arg"};
        GnuParser parser = new GnuParser();
        try {
            parser.flatten(new Options(), arguments, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Test flatten method with an option and an argument, and then multiple non-options
     */
    @Test(expected = ParseException.class)
    public void testFlattenOptionWithArgumentAndMultipleNonOptions() {
        String[] arguments = {"--foo", "arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        try {
            parser.flatten(new Options(), arguments, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Test flatten method with an option with an argument, and then an option without an argument
     */
    @Test
    public void testFlattenOptionWithArgumentAndOptionNoArgument() {
        String[] arguments = {"--foo=value1", "--bar"};
        GnuParser parser = new GnuParser();
        String[] expected = {"--foo", "value1", "--bar"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with an option with an argument, and then multiple options without arguments
     */
    @Test
    public void testFlattenOptionWithArgumentAndMultipleOptionsNoArgument() {
        String[] arguments = {"--foo=value1", "--bar", "--baz"};
        GnuParser parser = new GnuParser();
        String[] expected = {"--foo", "value1", "--bar", "--baz"};
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption set to true
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"--foo", "arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        try {
            String[] expected = {"--foo"};
            String[] result = parser.flatten(new Options(), arguments, true);
            assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test flatten method with an empty Options object
     */
    @Test
    public void testFlattenWithEmptyOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}