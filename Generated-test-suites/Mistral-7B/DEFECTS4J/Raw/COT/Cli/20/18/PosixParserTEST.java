package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", 1, "Option D with short argument");
        options.addOption("e", null, "Option E with no argument");
    }

    @Test
    public void testPosixParser_BasicUsage() {
        String[] args = {"--a", "--b", "-c", "value", "--d"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "-c",
                "value",
                "--d"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testPosixParser_LongOptionWithValue() {
        String[] args = {"--a", "--b=value", "-c", "value2", "--d"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a",
                "--b=value",
                "-c",
                "value2",
                "--d"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testPosixParser_UnknownOption() {
        String[] args = {"--unknown", "--a", "--b"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testPosixParser_StopAtNonOption() {
        String[] args = {"--a", "--b", "--", "non-option", "value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);
    }

    @Test
    public void testPosixParser_Bursting() {
        String[] args = {"-abcd"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "-b",
                "-c",
                "-d"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testPosixParser_BurstingWithValue() {
        String[] args = {"-acd"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "--",
                "c",
                "d"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testPosixParser_BurstingWithInvalidOption() {
        String[] args = {"-invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}