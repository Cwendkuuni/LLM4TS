package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test cases for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new DefaultParser();
        options = new Options();
    }

    

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a", "--optionB", "123", "--optionC=value"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("Option a is not present", 1, cmd.getArgs().length);
        Assert.assertEquals("Option B is not present", 1, cmd.getOptions().size());
        Option optB = cmd.getOption("optionB");
        Assert.assertNull("Option B value should be null", optB.getValue());
        Assert.assertEquals("Option C value is not correct", "value", cmd.getOptionValue("optionC"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");
        String[] args = {"-a", "--optionB"};
        CommandLine cmd = parser.parse(options, args, properties);

        Option optB = cmd.getOption("optionB");
        Assert.assertNotNull("Option B should not be null", optB);
        Assert.assertEquals("Option B value is not correct", "valueB", optB.getValue()[0]);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"-a", "--optionB", "-n", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("Number of args is not correct", 2, cmd.getArgs().length);
        Assert.assertEquals("Number of options is not correct", 1, cmd.getOptions().size());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Options)}.
     */
    @Test
    public void testParseWithOptions() throws ParseException {
        Options optionsWithOptionB = new Options();
        optionsWithOptionB.addOption("b", "optionB", false, "Show option B");
        String[] args = {"-a", "--optionB"};
        CommandLine cmd = parser.parse(optionsWithOptionB, args);

        Option optB = cmd.getOption("optionB");
        Assert.assertNotNull("Option B should not be null", optB);
        Assert.assertFalse("Option B should be false", optB.isSelected());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");
        String[] args = {"-a", "--optionB", "-n", "nonOption"};
        CommandLine cmd = parser.parse(options, args, properties, true);

        Assert.assertEquals("Number of args is not correct", 3, cmd.getArgs().length);
        Assert.assertEquals("Number of options is not correct", 1, cmd.getOptions().size());
        Option optB = cmd.getOption("optionB");
        Assert.assertNull("Option B value should be null", optB.getValue());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties)}.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");
        String[] args = {"-a", "--optionB", "-n", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true, properties);

        Assert.assertEquals("Number of args is not correct", 3, cmd.getArgs().length);
        Assert.assertEquals("Number of options is not correct", 1, cmd.getOptions().size());
        Option optB = cmd.getOption("optionB");
        Assert.assertNull("Option B value should be null", optB.getValue());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties, List)}.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOptionWithList() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");
        String[] args = {"-a", "--optionB", "-n", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true, properties, Arrays.asList("arg1", "arg2"));

        Assert.assertEquals("Number of args is not correct", 3, cmd.getArgs().length);
        Assert.assertEquals("Number of options is not correct", 1, cmd.getOptions().size());
        Option optB = cmd.getOption("optionB");
        Assert.assertNull("Option B value should be null", optB.getValue());
    }

    /**
     * Test method for {@link Parser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        String[] args = {"--foo", "-b", "-c=valueC", "-d", "--optionE", "arg1", "-e", "--", "-f"};
        String[] expected = {"--foo", "-b", "--", "-d", "--optionE", "arg1", "-e", "--", "-f"};

        List<String> result = parser.flatten(options, args, true);
        Assert.assertArrayEquals("Flattened arguments are not correct", expected, result.toArray(new String[0]));

        String[] argsWithNonOption = {"--foo", "-b", "nonOption", "--optionE", "arg1", "-e", "--", "-f"};
        String[] expectedWithNonOption = {"--foo", "-b", "nonOption", "--optionE", "arg1", "-e", "--", "-f"};

        List<String> resultWithNonOption = parser.flatten(options, argsWithNonOption, true);
        Assert.assertArrayEquals("Flattened arguments with non-option are not correct", expectedWithNonOption, resultWithNonOption.toArray(new String[0]));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties, List, boolean)}.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOptionWithListAndKeepUnknown() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");
        String[] args = {"--foo", "-b", "nonOption", "--optionE", "arg1", "-e", "--", "-f", "--unknown"};
        CommandLine cmd = parser.parse(options, args, true, properties, Arrays.asList("arg1", "arg2"), true);

        Assert.assertEquals("Number of args is not correct", 6, cmd.getArgs().length);
        Assert.assertEquals("Number of options is not correct", 3, cmd.getOptions().size());
        Option optB = cmd.getOption("optionB");
        Assert.assertNull("Option B value should be null", optB.getValue());
        Option optUnknown = cmd.getOption("unknown");
        Assert.assertNotNull("Unknown option should not be null", optUnknown);
        Assert.assertTrue("Unknown option should be ignored", optUnknown.isUnrecognized());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties, List, boolean)}.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown"};
        parser.parse(options, args, true, null, Arrays.asList(), true);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties, List, boolean)}.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Option opt = options.addOption("optionA", true, "Optional argument");
        opt.setRequired(false);
        String[] args = {"--optionA"};
        parser.parse(options, args, true, null, Arrays.asList(), true);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties, List, boolean)}.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"-a"};
        parser.parse(options, args, true, null, Arrays.asList(), false);
    }

    
}