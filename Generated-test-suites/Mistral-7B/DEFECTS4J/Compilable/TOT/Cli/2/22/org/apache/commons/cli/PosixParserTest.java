package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "bar", "-x", "-y", "--z=value"};
        Options options = new Options();
        options.addOption("foo", true, "Help for foo");
        options.addOption("x", false, "Help for x");
        options.addOption("z", true, "Help for z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "bar", "-x", "-y", "--z=value"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "bar", "-x", "-y", "--z=value", "arg"};
        Options options = new Options();
        options.addOption("foo", true, "Help for foo");
        options.addOption("x", false, "Help for x");
        options.addOption("z", true, "Help for z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "bar", "--", "arg"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // This test should fail with a ParseException
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] args = {"--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Help for longOption");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption=value"}, result);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", false, "Help for longOption");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenMultipleLongOptions() {
        String[] args = {"--longOption1", "--longOption2"};
        Options options = new Options();
        options.addOption("longOption1", false, "Help for longOption1");
        options.addOption("longOption2", false, "Help for longOption2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2"}, result);
    }

    @Test
    public void testFlattenShortOption() {
        String[] args = {"-x"};
        Options options = new Options();
        options.addOption("x", false, "Help for x");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-x"}, result);
    }

    @Test
    public void testFlattenShortOptionWithValue() {
        String[] args = {"-x=value"};
        Options options = new Options();
        options.addOption("x", true, "Help for x");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-x=value"}, result);
    }

    @Test
    public void testFlattenShortOptionWithMultipleValues() {
        String[] args = {"-x=value1", "-x=value2"};
        Options options = new Options();
        options.addOption("x", true, "Help for x");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-x=value1", "-x=value2"}, result);
    }
}