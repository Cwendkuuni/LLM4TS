package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Given
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D with argument");
        options.addOption("e", 4, "Option E with required argument");

        String[] arguments = {"--a", "--b", "--c", "--d", "value", "--e", "arg"};

        // When
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Then
        List<String> expected = Arrays.asList(
                "--a",
                "-a",
                "--b",
                "-b",
                "--c",
                "-c",
                "--d",
                "-d",
                "value",
                "-d",
                "--e",
                "-e",
                "arg"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        // Given
        Options options = new Options();

        // When & Then
        PosixParser parser = new PosixParser();
        parser.flatten(options, new String[]{"--invalid"}, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        // Given
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");

        String[] arguments = {"--a", "-c", "--d", "value"};

        // When
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        // Then
        List<String> expected = Arrays.asList(
                "--a",
                "-c",
                "--d",
                "--"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidLongOption() {
        // Given
        Options options = new Options();

        // When & Then
        PosixParser parser = new PosixParser();
        parser.flatten(options, new String[]{"--invalid-option"}, false);
    }

    @Test
    public void testFlattenWithMixedOptionsAndArguments() {
        // Given
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D with argument");

        String[] arguments = {"--a", "--b=1", "--c=2", "--d", "value"};

        // When
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Then
        List<String> expected = Arrays.asList(
                "--a",
                "-a",
                "--b",
                "-b",
                "1",
                "--c",
                "-c",
                "2",
                "--d",
                "-d",
                "value"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}