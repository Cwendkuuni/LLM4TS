package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options and no arguments.
     */
    @Test
    public void testEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options and one argument.
     */
    @Test
    public void testArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"test"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"test"}, result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--");
        expectedTokens.add("test");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and no arguments.
     */
    @Test
    public void testOption() {
        Options options = new Options();
        options.addOption("test", false, false, "Test option");
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--test"}, result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--test");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and one argument.
     */
    @Test
    public void testOptionArgument() {
        Options options = new Options();
        options.addOption("test", true, false, "Test option");
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"value"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--test", "value"}, result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--test");
        expectedTokens.add("value");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options and arguments.
     */
    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, false, "Option 1");
        options.addOption("option2", true, true, "Option 2 with argument");
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option1", "arg1", "--option2", "arg2"}, result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--option1");
        expectedTokens.add("arg1");
        expectedTokens.add("--option2");
        expectedTokens.add("arg2");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options.
     */
    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, false, "Long option 1");
        options.addOption("longOption2", true, true, "Long option 2 with argument");
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--longOption1", "arg1", "--longOption2", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "arg1", "--longOption2", "arg2"}, result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--longOption1");
        expectedTokens.add("arg1");
        expectedTokens.add("--longOption2");
        expectedTokens.add("arg2");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple long options.
     */
    @Test
    public void testMultipleLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, false, "Long option 1");
        options.addOption("longOption2", true, true, "Long option 2 with argument");
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--longOption1", "--longOption2", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2", "arg2"}, result);

        List expectedTokens = new ArrayList();
        expectedTokens.add("--longOption1");
        expectedTokens.add("--longOption2");
        expectedTokens.add("arg2");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with a non-option argument.
     */
    @Test
    public void testNonOptionArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"arg"};

        try {
            parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        List expectedTokens = new ArrayList();
        expectedTokens.add("--");
        expectedTokens.add("arg");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with a non-option argument after an option.
     */
    @Test
    public void testNonOptionArgumentAfterOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option", "arg"};

        try {
            parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        List expectedTokens = new ArrayList();
        expectedTokens.add("--option");
        expectedTokens.add("--");
        expectedTokens.add("arg");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with a non-option argument after multiple options.
     */
    @Test
    public void testNonOptionArgumentAfterMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, false, "Option 1");
        options.addOption("option2", true, true, "Option 2 with argument");
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option1", "--option2", "arg"};

        try {
            parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        List expectedTokens = new ArrayList();
        expectedTokens.add("--option1");
        expectedTokens.add("--option2");
        expectedTokens.add("--");
        expectedTokens.add("arg");
        parser.init();
        parser.flatten(options, arguments, false);
        Assert.assertEquals(expectedTokens, parser.tokens);
    }
}