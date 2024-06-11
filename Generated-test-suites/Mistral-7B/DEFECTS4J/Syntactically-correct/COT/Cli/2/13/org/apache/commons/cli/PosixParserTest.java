package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} .
     */
    @Test
    public void testFlatten() {
        // given
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        String[] arguments = {"--a", "-b", "-c", "1", "--d=4", "e", "--"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        List<String> expected = new ArrayList<>(Arrays.asList(arguments));
        expected.remove(0); // remove the first '--'
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for handling long options with equals sign.
     */
    @Test
    public void testLongOptionWithEqualsSign() {
        // given
        Options options = new Options();
        options.addOption("longOption", true, "Long option with equals sign");

        String[] arguments = {"--longOption=value"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    /**
     * Test method for handling long options without equals sign.
     */
    @Test
    public void testLongOptionWithoutEqualsSign() {
        // given
        Options options = new Options();
        options.addOption("longOption", true, "Long option without equals sign");

        String[] arguments = {"--longOption", "value"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    /**
     * Test method for handling special double hyphen '--'.
     */
    @Test
    public void testDoubleHyphen() {
        // given
        Options options = new Options();

        String[] arguments = {"--", "--optionA", "value1", "--optionB", "value2"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("--optionA");
        expected.add("value1");
        expected.add("--optionB");
        expected.add("value2");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for handling single hyphen '-'.
     */
    @Test
    public void testSingleHyphen() {
        // given
        Options options = new Options();

        String[] arguments = {"-"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        Assert.assertEquals("--", result[0]);
        Assert.assertEquals("-", result[1]);
    }

    /**
     * Test method for handling stopAtNonOption = true.
     */
    @Test
    public void testStopAtNonOption() {
        // given
        Options options = new Options();

        String[] arguments = {"--", "option1", "-c", "valueC"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, true);

        // then
        Assert.assertArrayEquals(new String[]{"--", "option1", "-c"}, result);
    }

    /**
     * Test method for handling stopAtNonOption = true and long options.
     */
    @Test
    public void testStopAtNonOptionWithLongOptions() {
        // given
        Options options = new Options();

        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        String[] arguments = {"--", "--longOptionA", "valueA", "-"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, true);

        // then
        Assert.assertArrayEquals(new String[]{"--", "--longOptionA", "valueA"}, result);
    }

    /**
     * Test method for handling stopAtNonOption = true and short options.
     */
    @Test
    public void testStopAtNonOptionWithShortOptions() {
        // given
        Options options = new Options();

        options.addOption("a", false, "Option A");

        String[] arguments = {"--", "-a", "-b"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, true);

        // then
        Assert.assertArrayEquals(new String[]{"--", "-a"}, result);
    }

    /**
     * Test method for handling stopAtNonOption = false and long options.
     */
    @Test
    public void testStopAtNonOptionWithLongOptionsFalse() {
        // given
        Options options = new Options();

        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        String[] arguments = {"--", "--longOptionA", "valueA", "--longOptionB", "valueB"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("--longOptionA");
        expected.add("valueA");
        expected.add("--longOptionB");
        expected.add("valueB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for handling stopAtNonOption = false and short options.
     */
    @Test
    public void testStopAtNonOptionWithShortOptionsFalse() {
        // given
        Options options = new Options();

        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--", "-a", "-b", "valueB"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("-a");
        expected.add("--optionB");
        expected.add("valueB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for handling stopAtNonOption = false and mixed options.
     */
    @Test
    public void testStopAtNonOptionWithMixedOptions() {
        // given
        Options options = new Options();

        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--", "-a", "--longOptionA", "valueA", "-b", "valueB"};

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, arguments, false);

        // then
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("-a");
        expected.add("--longOptionA");
        expected.add("valueA");
        expected.add("--optionB");
        expected.add("valueB");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for handling '--' before short option.
     */
    @Test(expected = AssertionError.class)
    public void testDoubleHyphenBeforeShortOption() {
        // given
        Options options = new Options();

        options.addOption("a", false, "Option A");

        String[] arguments = {"--", "-a"};

        PosixParser parser = new PosixParser();

        // when
        parser.flatten(options, arguments, false);

        // then
        // AssertionError is expected as per the implementation of the Parser class
    }

    /**
     * Test method for handling '--' before long option.
     */
    @Test(expected = AssertionError.class)
    public void testDoubleHyphenBeforeLongOption() {
        // given
        Options options = new Options();

        options.addOption("longOption", true, "Long option");

        String[] arguments = {"--", "--longOption"};

        PosixParser parser = new PosixParser();

        // when
        parser.flatten(options, arguments, false);

        // then
        // AssertionError is expected as per the implementation of the Parser class
    }

    /**
     * Test method for handling '--' before short option with arguments.
     */
    @Test(expected = AssertionError.class)
    public void testDoubleHyphenBeforeShortOptionWithArguments() {
        // given
        Options options = new Options();

        options.addOption("a", true, "Option A");

        String[] arguments = {"--", "--a", "arg"};

        PosixParser parser = new PosixParser();

        // when
        parser.flatten(options, arguments, false);

        // then
        // AssertionError is expected as per the implementation of the Parser class
    }

    /**
     * Test method for handling '--' before long option with arguments.
     */
    @Test(expected = AssertionError.class)
    public void testDoubleHyphenBeforeLongOptionWithArguments() {
        // given
        Options options = new Options();

        options.addOption("longOption", true, "Long option");

        String[] arguments = {"--", "--longOption", "arg"};

        PosixParser parser = new PosixParser();

        // when
        parser.flatten(options, arguments, false);

        // then
        // AssertionError is expected as per the implementation of the Parser class
    }

    /**
     * Test method for handling '--' before multiple options.
     */
    @Test(expected = AssertionError.class)
    public void testDoubleHyphenBeforeMultipleOptions() {
        // given
        Options options = new Options();

        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"--", "-a", "--b"};

        PosixParser parser = new PosixParser();

        // when
        parser.flatten(options, arguments, false);

        // then
        // AssertionError is expected as per the implementation of the Parser class
    }
}