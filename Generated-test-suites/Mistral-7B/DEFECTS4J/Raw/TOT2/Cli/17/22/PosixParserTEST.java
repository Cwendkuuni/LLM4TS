package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link PosixParser} class.
 */
public class PosixParserTest {

    /**
     * Test case for {@link PosixParser#flatten(Options, String[], boolean)} method with valid inputs.
     */
    @Test
    public void testFlattenWithValidInputs() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", "default", "Option D with default value");
        options.addOption("e", 3, "Option E with argument");
        options.addOption("f", "value", "Option F with argument value");

        PosixParser parser = new PosixParser();
        String[] args = {"--a", "-b", "-c", "d", "--d=newValue", "--e=value2", "f"};
        String[] expectedArgs = {"--a", "-b", "-c", "--d=newValue", "--e=value2", "f"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expectedArgs, flattenedArgs);
    }

    /**
     * Test case for {@link PosixParser#flatten(Options, String[], boolean)} method with special tokens.
     */
    @Test
    public void testFlattenWithSpecialTokens() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar", "--", "arg1", "--arg2=value", "--arg3=value2"};
        String[] expectedArgs = {"--foo=bar", "--", "arg1", "--arg2=value", "--arg3=value2"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expectedArgs, flattenedArgs);
    }

    /**
     * Test case for {@link PosixParser#flatten(Options, String[], boolean)} method with long options.
     */
    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionWithArgument", true, "Long option with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOptionWithArgument=value"};
        String[] expectedArgs = {"--longOptionWithArgument=value"};
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expectedArgs, flattenedArgs);
    }

    /**
     * Test case for {@link PosixParser#flatten(Options, String[], boolean)} method with invalid inputs.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidInputs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"invalidArg"};
        parser.flatten(options, args, false);
    }

    /**
     * Test case for {@link PosixParser#flatten(Options, String[], boolean)} method with stopAtNonOption.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "--a", "arg2"};
        String[] expectedArgs = {"arg1", "--a"};
        String[] flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expectedArgs, flattenedArgs);
    }
}