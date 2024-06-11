package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        Options options = new Options();
        String[] args = {"arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_TwoArguments() {
        Options options = new Options();
        options.addOption("optA", true, "Option A");
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "-optA"}, result);
    }

    @Test
    public void testFlatten_TwoArgumentsWithValues() {
        Options options = new Options();
        options.addOption("optA", true, "Option A");
        String[] args = {"arg1", "--optA=value1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "-optA", "value1", "arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption", "arg1"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_LongArgument() {
        Options options = new Options();
        String[] args = {"--longArgument=verylongargument"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("optA", true, "Option A");
        String[] args = {"arg1", "--", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"arg1", "--", "arg3"}, result);
    }
}