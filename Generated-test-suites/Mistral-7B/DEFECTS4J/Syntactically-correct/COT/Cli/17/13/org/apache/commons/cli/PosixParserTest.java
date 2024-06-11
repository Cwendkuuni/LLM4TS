package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options and no arguments.
     */
    @Test
    public void testEmptyInput() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        Options options = new Options();

        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[0], result);
        } catch (ParseException e) {
            // should not reach here
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options but with arguments.
     */
    @Test
    public void testInputWithArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result);
        } catch (ParseException e) {
            // should not reach here
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with options and arguments.
     */
    @Test
    public void testInputWithOptionsAndArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--option2=value1", "--option3", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o", "Option1");
        options.addOption("option2", true, "o", "Option2")
                .setValueSeparator('=')
                .requiredArg();
        options.addOption("option3", true, "o", "Option3");

        try {
            String[] result = parser.flatten(options, args, false);
            List expected = Arrays.asList("--option1", "--option2=value1", "--option3", "arg1", "arg2");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            // should not reach here
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with special -- and --options.
     */
    @Test
    public void testSpecialTokens() {
        PosixParser parser = new PosixParser();
        String[] args = {"--arg1", "--arg2=value1", "--", "--arg3", "--arg4=value2--"};
        Options options = new Options();

        try {
            String[] result = parser.flatten(options, args, false);
            List expected = Arrays.asList("--arg1", "--arg2=value1", "--", "--arg3", "--arg4=value2--");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            // should not reach here
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with single hyphen token.
     */
    @Test
    public void testSingleHyphenToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();

        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertEquals("-", result[0]);
        } catch (ParseException e) {
            // should not reach here
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption() {
       PosixParser parser = new PosixParser();
        String[] args = {"--invalid"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption set to true.
     */
    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--option2=value1", "nonOption", "--option3", "--option4=value2"};
        Options options = new Options();
        options.addOption("option1", true, "o", "Option1");
        options.addOption("option2", true, "o", "Option2")
                .setValueSeparator('=')
                .requiredArg();
        options.addOption("option3", true, "o", "Option3");
        options.addOption("option4", true, "o", "Option4");

        try {
            String[] result = parser.flatten(options, args, true);
            List expected = Arrays.asList("--option1", "--option2=value1", "nonOption", "--option3", "--option4=value2");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            // should not reach here
        }
    }
}