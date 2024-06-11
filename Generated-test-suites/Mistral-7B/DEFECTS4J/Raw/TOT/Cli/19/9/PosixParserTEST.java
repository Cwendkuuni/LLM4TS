package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c", "--qux", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("qux", true, "x");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value", "-a", "-b", "-c", "--qux", "arg1", "arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalidOption", "--foo", "--bar=value", "-a", "-b", "-c", "--qux", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("qux", true, "x");
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=value", "-a", "--", "optionWithArg=value", "-c", "--qux", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("qux", true, "x");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value", "--", "optionWithArg=value"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        String[] args = {"--longOptionWithArg=value1", "--longOptionWithArg=value2", "--longOptionWithArg=value3"};
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "l");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOptionWithArg=value1", "--longOptionWithArg=value2", "--longOptionWithArg=value3"}, result);
    }

    @Test
    public void testFlattenMultipleOccurrences() {
        String[] args = {"--foo", "--foo", "--foo", "--foo"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "--foo", "--foo", "--foo"}, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenEmptyOptions() {
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }
}