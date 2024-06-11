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
    public void setUp() {
        options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithAllOptions() throws ParseException {
        String[] args = {"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] args = {"-i", "input.txt"};
        parser.parse(options, args, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {"--invalid"};
        parser.parse(options, args, true);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseUnknownOption() throws ParseException {
        String[] args = {"--unknown"};
        parser.parse(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOptionValue() throws ParseException {
        String[] args = {"-i", "input.txt", "--output", "output"};
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");
        properties.setProperty("output", "output.txt");

        CommandLine cmd = parser.parse(options, new String[0], properties, true);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidInput() throws ParseException {
        options.addOption("i", "input", true, "Input file path");
        String[] args = {"--input", "input"};
        parser.parse(options, args, true);
    }
}