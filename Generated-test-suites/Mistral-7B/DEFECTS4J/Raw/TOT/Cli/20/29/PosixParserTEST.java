package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "--bar=baz", "-abc", "-def"};
        Options options = new Options();
        options.addOption("foo", false, null, "A simple option");
        options.addOption("bar", true, "B", "An option with an argument");
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=baz", "-abc", "-def"}, flattened);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=baz", "-abc", "nonOption", "-def"};
        Options options = new Options();
        options.addOption("foo", false, null, "A simple option");
        options.addOption("bar", true, "B", "An option with an argument");
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=baz", "nonOption", "-def"}, flattened);
    }

    @Test
    public void testFlattenWithLongArguments() {
        String[] args = {"--longOption=value1", "--longOption2=value2", "longOption3=value3", "longOption4=value4"};
        Options options = new Options();
        options.addOption("longOption", true, null, "A long option with an argument");
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption=value1", "--longOption2=value2", "longOption3=value3", "longOption4=value4"}, flattened);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        String[] args = {"--invalidOption", "-abc", "-def"};
        Options options = new Options();
        options.addOption("foo", false, null, "A simple option");
        options.addOption("bar", true, "B", "An option with an argument");
        PosixParser parser = new PosixParser();

        try {
            parser.flatten(options, args, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFlattenWithMultipleInvalidOptions() {
        String[] args = {"--invalidOption1", "--invalidOption2", "-abc", "-def"};
        Options options = new Options();
        options.addOption("foo", false, null, "A simple option");
        options.addOption("bar", true, "B", "An option with an argument");
        PosixParser parser = new PosixParser();

        try {
            parser.flatten(options, args, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}