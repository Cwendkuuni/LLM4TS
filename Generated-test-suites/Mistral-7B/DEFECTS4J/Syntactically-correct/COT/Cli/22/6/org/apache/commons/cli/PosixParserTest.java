package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_Simple() {
        String[] args = {"--option1", "-o", "value1", "--option2", "-a"};
        Options options = new Options();
        options.addOption("option1", true, "o", "option with value");
        options.addOption("option2", true, "a", "option2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--option1",
                "-o",
                "value1",
                "--option2",
                "-a"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] args = {"--long-option", "--long-option=value"};
        Options options = new Options();
        options.addOption("longOption", true, "l", "long option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--long-option",
                "--long-option=value"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--option1", "-a", "--option2", "nonOption"};
        Options options = new Options();
        options.addOption("option1", true, "o", "option with value");
        options.addOption("option2", true, "a", "option2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--option1",
                "-a",
                "--option2",
                "nonOption"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidLongOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The test should fail with an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidShortOption() {
        String[] args = {"-x"}; // Invalid short option "x"
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The test should fail with an IllegalArgumentException
    }
}