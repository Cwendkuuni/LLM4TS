import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        Option option = new Option("o", "output", true, "Output file path");
        Option helpOption = new Option("h", "help", false, "Print help message");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(option);
        options.addOption(helpOption);

        String[] args = new String[]{"-o", "output.txt", "help"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("help"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Option option1 = new Option("o", "option1", true, "Option 1 value");
        Option option2 = new Option("p", "option2", true, "Option 2 value");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(option1, option2);

        options.addOptions(Arrays.asList(option1, option2));
        options.setRequiredOptions(requiredOptions);

        String[] args = new String[]{"-o", "option1Value", "-p", "option2Value"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("option1Value", cmd.getOptionValue("option1"));
        Assert.assertEquals("option2Value", cmd.getOptionValue("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Option option = new Option("o", "option", true, "Option value");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = Arrays.asList(option);

        options.addOption(option);
        options.setRequiredOptions(requiredOptions);

        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail("MissingOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgumentForOption() throws ParseException {
        Option option = new Option("o", "option", true, "Option value");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(option);

        String[] args = new String[]{"-o"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail("MissingArgumentException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Option option1 = new Option("o", "option1", true, "Option 1 value");
        Option option2 = new Option("u", "unrecognizedOption", true, "Unrecognized option value");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[]{"-u", "unrecognizedOptionValue"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail("UnrecognizedOptionException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOptionValue() throws ParseException {
        Option option = new Option("o", "option", true, "Option value");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(option);

        String[] args = new String[]{"-o", "invalidValue"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail("ParseException expected");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Option option1 = new Option("o", "option1", true, "Option 1 value");
        Option option2 = new Option("p", "option2", true, "Option 2 value");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(option1);
        options.addOption(option2);

        Properties properties = new Properties();
        properties.setProperty("option1", "option1Value");
        properties.setProperty("option2", "option2Value");

        String[] args = new String[]{"--", "--option1", "--option2"};
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("option1Value", cmd.getOptionValue("option1"));
        Assert.assertEquals("option2Value", cmd.getOptionValue("option2"));
    }
}