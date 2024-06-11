package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"java", "--foo=bar", "--option1", "--option2", "-o", "out.txt"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f")
                .setValueSeparator(new String[0])
                .longOpt("option1")
                .longOpt("option2")
                .option("o")
                .hasArg(true)
                .reqArg("OUTPUT_FILE");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--foo", "bar", "--option1", "--option2", "-o", "out.txt"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        String[] args = {"java", "--foo=bar", "--option1", "--option2", "-o", "out.txt", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f")
                .setValueSeparator(new String[0])
                .longOpt("option1")
                .longOpt("option2")
                .option("o")
                .hasArg(true)
                .reqArg("OUTPUT_FILE");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"java", "--foo", "bar", "--option1", "--option2", "-o", "out.txt", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"java", "--foo=bar", "--option1", "--option2", "-o", "out.txt", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f")
                .setValueSeparator(new String[0])
                .longOpt("option1")
                .longOpt("option2")
                .option("o")
                .hasArg(true)
                .reqArg("OUTPUT_FILE");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"java", "--foo=bar", "--option1", "--option2", "-o", "out.txt"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = {"java", "--invalid", "--option1", "--option2", "-o", "out.txt"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f")
                .setValueSeparator(new String[0])
                .longOpt("option1")
                .longOpt("option2")
                .option("o")
                .hasArg(true)
                .reqArg("OUTPUT_FILE");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testHelpFormat() {
        String[] args = {"java", "--help"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f")
                .setValueSeparator(new String[0])
                .longOpt("option1")
                .longOpt("option2")
                .option("o")
                .hasArg(true)
                .reqArg("OUTPUT_FILE");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java", "Java Application", options, true);
    }
}