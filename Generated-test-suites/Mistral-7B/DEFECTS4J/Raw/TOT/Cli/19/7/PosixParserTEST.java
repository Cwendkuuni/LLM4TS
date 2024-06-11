package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = new String[]{"-a", "--optionB=valueB", "-c", "--", "-d", "-e=valueE"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        options.addOption("e", true, "Option E");

        DefaultParser parser = new PosixParser();
        String[] result = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "-c", "--", "-d", "-e", "valueE"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = new String[]{"-a", "--optionB=valueB", "-c", "nonOption", "-d", "-e=valueE"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        options.addOption("e", true, "Option E");

        DefaultParser parser = new PosixParser();
        String[] result = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB", "nonOption", "-d", "-e", "valueE"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        String[] args = new String[]{"--longOption=longValue"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        DefaultParser parser = new PosixParser();
        String[] result = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
        Assert.assertArrayEquals(new String[]{"--longOption", "longValue"}, parser.parse(options, args, true).toString().split(" "));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = new String[]{"--invalidOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testHelp() {
        String[] args = new String[]{"--help"};
        Options options = new Options();

        DefaultParser parser = new PosixParser();
        String help = parser.parse(options, args, true).toString();

        HelpFormatter formatter = new HelpFormatter();
        String expectedHelp = "Parses a variety of command line options.\n" +
                "\n" +
                "usage: java org.apache.commons.cli.examples.CmdLineParser [options]...\n" +
                "\n" +
                "options:\n" +
                "\n" +
                "  -a, --optionA\n" +
                "      Option A\n" +
                "  -b valueB, --optionB valueB\n" +
                "      Option B with an argument value:\n" +
                "      valueB\n" +
                "  -c, --optionC\n" +
                "      Option C\n" +
                "  -d, --optionD\n" +
                "      Option D\n" +
                "  -e valueE, --optionE valueE\n" +
                "      Option E with an argument value:\n" +
                "      valueE\n" +
                "\n" +
                "  -h, --help\n" +
                "      prints this help message and exits the program";

        formatter.printHelp("java org.apache.commons.cli.examples.CmdLineParser", Arrays.asList(help.split("\n")), 80, true);
        Assert.assertEquals(expectedHelp, formatter.toText());
    }
}