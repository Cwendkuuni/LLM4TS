import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup() {
        this.parser = new DefaultParser();
        this.options = new Options();
        this.options.addRequiredOption("i", "input", true, "input file");
        this.options.addRequiredOption("o", "output", true, "output file");
        this.options.addOption("h", "help", false, "print this help message");
    }

    @Test
    public void testParseWithInputAndOutput() {
        String[] args = {"-i", "input.txt", "-o", "output.txt"};
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
            Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
            Assert.assertNull(cmd.getOptionValue("help"));
        } catch (ParseException e) {
            Assert.fail("ParseException: " + e.getMessage());
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredInput() {
        String[] args = {"-o", "output.txt"};
        try {
            parser.parse(options, args);
        } catch (ParseException e) {
            throw new MissingArgumentException("Missing argument for option: i");
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredOutput() {
        String[] args = {"-i", "input.txt"};
        try {
            parser.parse(options, args);
        } catch (ParseException e) {
            throw new MissingArgumentException("Missing argument for option: o");
        }
    }

    @Test(expected = ParseException.class)
    public void testParseUnknownOption() {
        String[] args = {"-x", "unknown"};
        try {
            parser.parse(options, args);
        } catch (MissingOptionException e) {
            throw new ParseException("Unrecognized option: -x");
        }
    }

    @Test
    public void testParseHelpOption() {
        String[] args = {"-h"};
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("help"));
        } catch (ParseException e) {
            Assert.fail("ParseException: " + e.getMessage());
        }
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseMissingOptionWithGroup() {
        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g", "group", true, "group option");
        this.options.addOptionGroup(group);

        String[] args = {"-i", "input.txt", "-g"};
        try {
            parser.parse(options, args);
        } catch (MissingOptionException e) {
            throw new NoSuchOptionException("No option 'g'");
        }
    }
}