package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with valid options and arguments.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenValidOptionsAndArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "argument1", "--", "-abc", "-def=value"};
        String[] expected = {"-D", "property", "value", "argument1", "--", "-abc", "-def"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with an empty arguments array.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with a single argument that is a valid option.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenSingleValidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty"};
        String[] expected = {"-D", "property"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with a single argument that is a valid option with value.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenSingleValidOptionWithValue() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value"};
        String[] expected = {"-D", "property", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with a single argument that is not a valid option.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test(expected = ParseException.class)
    public void testFlattenSingleInvalidArgument() throws ParseException {
        Options options = new Options();
        String[] arguments = {"invalidArg"};

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);

        // The test should throw a ParseException.
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain "--" to indicate the end of options.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenEndOfOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = {"argument1", "--"};
        String[] expected = {"argument1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain "--" followed by valid options.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenEndOfOptionsWithValidOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = {"argument1", "--", "-Dproperty=value", "-abc"};
        String[] expected = {"argument1", "--", "-D", "property", "value", "-abc"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain "--" followed by invalid options.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test(expected = ParseException.class)
    public void testFlattenEndOfOptionsWithInvalidOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = {"argument1", "--", "invalidOption"};

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);

        // The test should throw a ParseException.
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain "--" followed by an empty string.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenEndOfOptionsWithEmptyString() throws ParseException {
        Options options = new Options();
        String[] arguments = {"argument1", "--", ""};
        String[] expected = {"argument1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with an argument that starts with an option prefix but is not a valid option.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidShortOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-XinvalidOption"};

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);

        // The test should throw a ParseException.
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain multiple options with the same prefix.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenMultipleOptionsWithSamePrefix() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", true, "Option B");
        String[] arguments = {"-a", "-a"};
        String[] expected = {"-a", "-a"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain multiple options with the same prefix and values.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenMultipleOptionsWithSamePrefixAndValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", true, "Option B");
        String[] arguments = {"-a", "--value", "-a", "--value"};
        String[] expected = {"-a", "--value", "-a", "--value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain multiple options with the same prefix and different values.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenMultipleOptionsWithSamePrefixAndDifferentValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", true, "Option B");
        String[] arguments = {"-a", "--value1", "-a", "--value2"};
        String[] expected = {"-a", "--value1", "-a", "--value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain an option with a long name and a value.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenOptionWithLongNameAndValue() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption=value"};
        String[] expected = {"--longOption", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain multiple options with the same long name but different values.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenMultipleOptionsWithSameLongNameAndDifferentValues() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption=value1", "--longOption=value2"};
        String[] expected = {"--longOption", "value1", "--longOption", "value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain an option with a long name and no value.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenOptionWithLongNameAndNoValue() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        String[] arguments = {"--longOption"};
        String[] expected = {"--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with arguments that contain multiple options with the same long name but no value.
     *
     * @throws ParseException if the options or arguments are invalid.
     */
    @Test
    public void testFlattenMultipleOptionsWithSameLongNameAndNoValue() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        options.addOption("longOption", false, "Long option");
        String[] arguments = {"--longOption", "--longOption"};
        String[] expected = {"--longOption", "--longOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }
}