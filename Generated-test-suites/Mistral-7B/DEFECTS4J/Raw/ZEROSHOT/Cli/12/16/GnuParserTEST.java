package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for {@link GnuParser}.
 */
public class GnuParserTest {

    /**
     * Test flatten method with simple options.
     */
    @Test
    public void testFlattenWithSimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "argA", "-b", "argB"};
        String[] expected = {"-a", "argA", "-b", "argB"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with long options.
     */
    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        String[] arguments = {"--longOptionA", "argA", "--longOptionB", "argB"};
        String[] expected = {"--longOptionA", "argA", "--longOptionB", "argB"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with options and values.
     */
    @Test
    public void testFlattenWithOptionsAndValues() {
        Options options = new Options();
        options.addOption("d", true, "Option D with value");

        String[] arguments = {"-Dproperty=value1", "-Dproperty=value2"};
        String[] expected = {"-D", "property=value1", "-D", "property=value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "argA", "-b", "argB", "nonOption"};
        String[] expected = {"-a", "argA"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with multiple non-options.
     */
    @Test
    public void testFlattenWithMultipleNonOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "argA", "nonOption1", "nonOption2"};
        String[] expected = {"-a", "argA"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with double-hyphen.
     */
    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--", "-a", "argA"};
        String[] expected = {"--", "-a", "argA"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with no arguments.
     */
    @Test
    public void testFlattenWithNoArguments() {
        Options options = new Options();

        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with empty argument.
     */
    @Test
    public void testFlattenWithEmptyArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", ""};
        String[] expected = {"-a"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }
}