import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    private Options options;
    private Parser parser;
    private String[] args;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithInputOption() throws ParseException {
        args = new String[]{"-i", "/path/to/input/file.txt"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("/path/to/input/file.txt", cmd.getOptionValue("input"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingInputOption() throws ParseException {
        args = new String[]{"-a", "value"};
        parser.parse(options, args, false);
        Assert.fail("ParseException expected but not thrown");
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        args = new String[]{"-i", "/path/to/input/file1.txt", "-o", "/path/to/output/file.out"};
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("/path/to/input/file1.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output/file.out", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        args = new String[]{"-x", "value"};
        parser.parse(options, args, false);
        Assert.fail("ParseException expected but not thrown");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties props = new Properties();
        props.setProperty("option1", "value1");
        props.setProperty("option2", "value2");

        args = new String[]{"--", "--property-filename", "properties.txt"};
        CommandLine cmd = parser.parse(options, args, props, false);

        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidProperties() throws ParseException {
        Properties props = new Properties();
        props.setProperty("invalidOption", "value");

        args = new String[]{"--", "--property-filename", "properties.txt"};
        parser.parse(options, args, props, false);
        Assert.fail("ParseException expected but not thrown");
    }
}