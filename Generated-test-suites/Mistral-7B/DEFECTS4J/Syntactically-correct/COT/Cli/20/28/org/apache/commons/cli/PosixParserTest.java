package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
     * with valid options.
     */
    @Test
    public void testFlattenWithValidOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D with argument");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "--c=123", "-d", "value"};
        String[] expected = {"-a", "-b", "-c", "-d", "value"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption option.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "--c", "nonOption"};
        String[] expected = {"-a", "-b", "--c", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options.
     */
    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", 1, "Long Option B");

        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOptionA", "--longOptionB=123"};
        String[] expected = {"--longOptionA", "--longOptionB", "123"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with bursting.
     */
    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-acd", "value"};
        String[] expected = {"-a", "-c", "-d", "value"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-x", "-c", "value"};

        parser.flatten(options, arguments, false);
    }

    
}