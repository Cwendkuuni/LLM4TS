package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArgs() {
        Options options = new Options();
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleArg() {
        Options options = new Options();
        String[] args = {"arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenWithTwoArgs() {
        Options options = new Options();
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--option1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1"}, result);
    }

    @Test
    public void testFlattenWithSingleOptionValue() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--option1=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] args = {"--option1", "--option2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsValues() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] args = {"--option1=value1", "--option2=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value1", "--option2", "value2"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, "lo1");
        options.addOption("longOption2", true, "lo2");
        String[] args = {"--longOption1", "--longOption2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2"}, result);
    }

    @Test
    public void testFlattenWithLongOptionsValues() {
        Options options = new Options();
        options.addOption("longOption1", true, "lo1");
        options.addOption("longOption2", true, "lo2");
        String[] args = {"--longOption1=value1", "--longOption2=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "value1", "--longOption2", "value2"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] args = {"--option1", "arg1", "arg2", "--", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option1", "arg1", "--", "nonOption"}, result);
    }
}