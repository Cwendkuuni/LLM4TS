import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser Test Class
 */
public class ParserTest {

    /**
     * Test for the parse method with valid options and arguments
     */
    @Test
    public void testParseValidOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("i", "input", true, "input file");

        String[] arguments = {"-o", "/path/to/output", "-i", "/path/to/input"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments, false);

        Assert.assertEquals("/path/to/output", commandLine.getOptionValue("output"));
        Assert.assertEquals("/path/to/input", commandLine.getOptionValue("input"));
        Assert.assertNull(commandLine.getArgs());
    }

    /**
     * Test for the parse method with required options
     */
    @Test
    public void testParseRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("i", "input", true, "input file");
        options.addRequiredOption("r", "requiredOption", false, "required option");

        String[] arguments = {"-o", "/path/to/output", "-i", "/path/to/input"};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, arguments, false);
            Assert.fail("MissingRequiredOptionException should be thrown");
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: requiredOption", e.getMessage());
        }

        // Test with valid required options
        arguments = arguments.clone();
        arguments[arguments.length - 1] = "-r";

        commandLine = parser.parse(options, arguments, false);
        Assert.assertNull(commandLine.getOptionValue("requiredOption"));
    }

    /**
     * Test for the parse method with multiple values for an option
     */
    @Test
    public void testParseMultipleValuesForOption() throws ParseException {
        Options options = new Options();
        options.addOption("l", "logs", true, "list of log files");

        String[] arguments = {"-l", "log1.log,log2.log,log3.log"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments, false);

        List<String> logFiles = (List<String>) commandLine.getOptionValues("logs");

        Assert.assertEquals(3, logFiles.size());
        Assert.assertEquals("log1.log", logFiles.get(0));
        Assert.assertEquals("log2.log", logFiles.get(1));
        Assert.assertEquals("log3.log", logFiles.get(2));
    }

    /**
     * Test for the parse method with an unrecognized option
     */
    @Test
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        String[] arguments = {"-x", "value"};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, arguments, false);
            Assert.fail("UnrecognizedOptionException should be thrown");
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    /**
     * Test for the parse method with missing arguments for an option that requires an argument
     */
    @Test
    public void testParseMissingArgumentForOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        String[] arguments = {"-o"};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, arguments, false);
            Assert.fail("MissingArgumentException should be thrown for option output");
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: output", e.getMessage());
        }
    }

    /**
     * Test for the parse method with multiple instances of the same option
     */
    @Test
    public void testParseMultipleInstancesOfTheSameOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        String[] arguments = {"-o", "output1", "-o", "output2"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments, false);

        List<String> outputDirectories = (List<String>) commandLine.getAllValues("output");
        Assert.assertEquals(2, outputDirectories.size());
        Assert.assertEquals("output1", outputDirectories.get(0));
        Assert.assertEquals("output2", outputDirectories.get(1));
    }

    /**
     * Test for the parse method with stopAtNonOption flag set to true
     */
    @Test
    public void testParseWithStopAtNonOptionFlagSetToTrue() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        String[] arguments = {"-h", "-o", "output"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments, true);

        Assert.assertNull(commandLine.getOptionValue("output"));
        Assert.assertEquals(1, commandLine.getArgList().size());
        Assert.assertEquals("-h", commandLine.getArgList().get(0));
    }

    /**
     * Test for the parse method with properties
     */
    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        Properties properties = new Properties();
        properties.setProperty("output", "/path/to/output");

        String[] arguments = {"--properties-file", "path/to/properties"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments, properties, false);

        Assert.assertEquals("/path/to/output", commandLine.getOptionValue("output"));
    }

    /**
     * Test for the parse method with options in option groups
     */
    @Test
    public void testParseWithOptionsInOptionGroups() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group for options");
        group.addOption("g", "groupOption", false, "Group Option");

        options.addOption("o", "output", true, "output directory");
        options.addOption("i", "input", true, "input file");
        options.addOptionGroup(group);

        String[] arguments = {"-g", "-o", "output", "-i", "input"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments, false);

        Assert.assertEquals("output", commandLine.getOptionValue("output"));
        Assert.assertEquals("input", commandLine.getOptionValue("input"));
        Assert.assertTrue(commandLine.hasOption("groupOption"));
    }
}