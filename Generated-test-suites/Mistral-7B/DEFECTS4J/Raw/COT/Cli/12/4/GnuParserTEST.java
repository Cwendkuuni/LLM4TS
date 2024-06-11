package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.apache.commons.cli.Utils.stripLeadingHyphens;

/**
 * Test class for GnuParser methods
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when all arguments are valid options
     */
    @Test
    public void testFlattenWhenAllArgumentsAreValidOptions() {
        String[] arguments = {"-a", "--optionB", "-c", "--optionD=value", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", true, "Option D");

        String[] expected = {"-a", "--optionB", "-c", "--optionD", "--"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when the first argument is a long option
     */
    @Test
    public void testFlattenWhenFirstArgumentIsALongOption() {
        String[] arguments = {"--optionA", "valueA", "-b", "--optionC"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("c", false, "Option C");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"--optionA", "valueA", "-b"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when the first argument is not an option
     */
    @Test(expected = ParseException.class)
    public void testFlattenWhenFirstArgumentIsNotAnOption() {
        String[] arguments = {"valueA", "-b", "--optionC"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("c", false, "Option C");

        new GnuParser().flatten(options, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when all arguments are not options
     */
    @Test
    public void testFlattenWhenAllArgumentsAreNotOptions() {
        String[] arguments = {"valueA", "valueB", "valueC"};
        Options options = new Options();

        List<String> expected = new ArrayList<>();
        expected.add("valueA");
        expected.add("valueB");
        expected.add("valueC");

        List<String> actual = new ArrayList<>();
        String[] tokenizedArguments = new GnuParser().flatten(options, arguments, false);

        for (String arg : tokenizedArguments) {
            actual.add(arg);
        }

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when the last argument is a long option
     */
    @Test
    public void testFlattenWhenLastArgumentIsALongOption() {
        String[] arguments = {"-a", "-b", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("c", false, "Option C");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"-a", "-b", "--optionC"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when an option has a value and the value contains spaces
     */
    @Test
    public void testFlattenWhenOptionValueContainsSpaces() {
        String[] arguments = {"-a", "--optionB=\"value with spaces\""};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"-a", "--optionB=value with spaces"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when an option has a value and the value starts with a hyphen
     */
    @Test
    public void testFlattenWhenOptionValueStartsWithHyphen() {
        String[] arguments = {"-a", "--optionB=-value"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"-a", "--optionB=-value"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when an option has a value and the value is empty
     */
    @Test
    public void testFlattenWhenOptionValueIsEmpty() {
        String[] arguments = {"-a", "--optionB="};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"-a", "--optionB"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when an option has no value
     */
    @Test
    public void testFlattenWhenOptionHasNoValue() {
        String[] arguments = {"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"-a", "--optionB"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when the -- argument is present
     */
    @Test
    public void testFlattenWhenArgumentIsPresent() {
        String[] arguments = {"-a", "--", "valueA"};
        Options options = new Options();
        options.addOption("a", false, "Option A");

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--");
        expected.add("valueA");

        List<String> actual = new ArrayList<>();
        String[] tokenizedArguments = new GnuParser().flatten(options, arguments, false);

        for (String arg : tokenizedArguments) {
            actual.add(arg);
        }

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when the stopAtNonOption flag is set to true
     */
    @Test
    public void testFlattenWhenStopAtNonOptionIsTrue() {
        String[] arguments = {"-a", "--", "valueA", "valueB"};
        Options options = new Options();
        options.addOption("a", false, "Option A");

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--");
        expected.add("valueA");

        List<String> actual = new ArrayList<>();
        String[] tokenizedArguments = new GnuParser().flatten(options, arguments, true);

        for (String arg : tokenizedArguments) {
            actual.add(arg);
        }

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when the last argument is a long option with no value
     */
    @Test
    public void testFlattenWhenLastArgumentIsALongOptionWithNoValue() {
        String[] arguments = {"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--optionB");

        List<String> actual = new ArrayList<>();
        String[] tokenizedArguments = new GnuParser().flatten(options, arguments, false);

        for (String arg : tokenizedArguments) {
            actual.add(arg);
        }

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * when an option is followed by its value with no space between them
     */
    @Test
    public void testFlattenWhenOptionIsFollowedByItsValueWithNoSpaceBetweenThem() {
        String[] arguments = {"-a", "--optionBvalue"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        StringJoiner joiner = new StringJoiner(" ", "[\"]");
        String[] expected = {"-a", "--optionBvalue"};
        String[] actual = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(joiner.merge(expected).split(" "), joiner.merge(actual).split(" "));
    }
}