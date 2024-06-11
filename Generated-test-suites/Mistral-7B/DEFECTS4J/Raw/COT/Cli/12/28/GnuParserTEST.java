package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test the flatten method with a simple array of arguments.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenSimple() throws ParseException {
        String[] arguments = new String[]{"-a", "--optionB", "argC", "-Dproperty=value", "--", "argE", "argF"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList(
                "-a",
                "--optionB",
                "-D",
                "property",
                "value",
                "--",
                "argE",
                "argF"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method with an empty array of arguments.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenEmpty() throws ParseException {
        String[] arguments = new String[0];
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test the flatten method when an unknown option is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException {
        String[] arguments = new String[]{"-unknown", "-Dproperty=value"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    /**
     * Test the flatten method when a long option is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenLongOption() throws ParseException {
        String[] arguments = new String[]{"--longOption", "arg"};
        Options options = new Options();
        options.addOption("longOption", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList(
                "--longOption"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with an argument is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithArgument=value"};
        Options options = new Options();
        options.addOption("optionWithArgument", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList(
                "-optionWithArgument",
                "value"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with multiple arguments is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithMultipleArguments() throws ParseException {
        String[] arguments = new String[]{"-optionWithMultipleArguments=value1 value2"};
        Options options = new Options();
        options.addOption("optionWithMultipleArguments", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList(
                "-optionWithMultipleArguments",
                "value1",
                "value2"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with no value is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithNoValue() throws ParseException {
        String[] arguments = new String[]{"-optionWithNoValue"};
        Options options = new Options();
        options.addOption("optionWithNoValue", false, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithNoValue");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value and no argument is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueNoArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value"};
        Options options = new Options();
        options.addOption("optionWithValue", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value and an argument is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValueAndArgument=value arg"};
        Options options = new Options();
        options.addOption("optionWithValueAndArgument", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValueAndArgument", "value", "arg");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an empty option is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenEmptyOption() throws ParseException {
        String[] arguments = new String[]{"--"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value, an argument, and no value is encountered.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndArgumentAndNoValue() throws ParseException {
        String[] arguments = new String[]{"-optionWithValueAndArgumentAndNoValue=value arg"};
        Options options = new Options();
        options.addOption("optionWithValueAndArgumentAndNoValue", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValueAndArgumentAndNoValue", "value", "arg", "--");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with no value is followed by an option with a value.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithNoValueAndOptionWithValue() throws ParseException {
        String[] arguments = new String[]{"-optionWithNoValue", "-optionWithValue=value"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithNoValue", "-optionWithValue", "value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with no value.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithNoValue() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "-optionWithNoValue"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "-optionWithNoValue");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by a long option.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndLongOption() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "desc", "desc");
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "--longOption");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with no value is followed by an option with a value.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithNoValueAndOptionWithValue() throws ParseException {
        String[] arguments = new String[]{"-optionWithNoValue", "-optionWithValue=value"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithNoValue", "-optionWithValue", "value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with no value and no argument.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithNoValueNoArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "--"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "--");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with no value and an argument.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithNoValueArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "--arg"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "--arg");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with a value and no argument.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithValueNoArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "-optionWithValue2=value2"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "-optionWithValue2", "value2");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with a value, an argument, and no value.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithValueArgumentNoValue() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "-optionWithValue2=value2 arg"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "-optionWithValue2", "value2", "--");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by multiple options with no value and no argument.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndMultipleOptionsNoValueNoArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "--option1", "--option2", "--option3"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "--option1", "--option2", "--option3");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with a value, an argument, and an empty option.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithValueArgumentEmptyOption() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "-optionWithValue2=value2 arg", "--"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "-optionWithValue2", "value2", "--");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by an option with a value, an argument, and an option with no value and no argument.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndOptionWithValueArgumentOptionNoValueNoArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "-optionWithValue2=value2 arg", "--option3"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "-optionWithValue2", "value2", "--option3");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test the flatten method when an option with a value is followed by multiple options with no value and an argument.
     * @throws ParseException if there is an error parsing the options
     */
    @Test
    public void testFlattenOptionWithValueAndMultipleOptionsNoValueArgument() throws ParseException {
        String[] arguments = new String[]{"-optionWithValue=value", "--option1 arg", "--option2 arg"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-optionWithValue", "value", "--option1", "arg", "--option2", "arg");
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}