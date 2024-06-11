package org.apache.commons.cli;

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

    

    @Test
    public void testFlatten_BasicUsage() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-o", "out.txt", "--bar", "-b", "123", "--baz"};
        Options options = new Options();
        options.addRequiredOption("foo", "f", false, "foo option");
        options.addOption("o", "o", true, "output file");
        options.addOption("b", "b", true, "batch size");
        options.addOption("bar", "b", false, "bar option");
        options.addOption("baz", "b", false, "baz option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList(
                "--foo",
                "-o",
                "out.txt",
                "--bar",
                "-b",
                "123",
                "--baz"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-o", "out.txt", "--", "nonOption"};
        Options options = new Options();
        options.addRequiredOption("foo", "f", false, "foo option");
        options.addOption("o", "o", true, "output file");

        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList(
                "--foo",
                "--",
                "nonOption"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlatten_LongOptionsWithValues() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=value1", "--bar=value2", "--baz=value3"};
        Options options = new Options();
        options.addOption("foo", "f", false, "foo option");
        options.addOption("bar", "b", false, "bar option");
        options.addOption("baz", "b", false, "baz option");
        options.getOption("foo").setLongOpt("longFoo");
        options.getOption("bar").setLongOpt("longBar");
        options.getOption("baz").setLongOpt("longBaz");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList(
                "--longFoo=value1",
                "--longBar=value2",
                "--longBaz=value3"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknown"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=value1", "--"};
        Options options = new Options();
        options.addOption("foo", "f", false, "foo option");

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MixedOptionsAndValues() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=value1", "--bar", "-o", "out.txt"};
        Options options = new Options();
        options.addOption("foo", "f", false, "foo option");
        options.addOption("bar", "b", false, "bar option");
        options.addOption("o", "o", true, "output file");

        parser.flatten(options, args, false);
    }

    
}