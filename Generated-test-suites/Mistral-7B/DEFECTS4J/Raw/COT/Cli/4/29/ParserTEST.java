package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
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
 * Test class for Parser.
 */
public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        };
        options = new Options();
    }

    /**
     * Test parse method with no arguments.
     */
    @Test
    public void testParseNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    /**
     * Test parse method with arguments.
     */
    @Test
    public void testParseArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("arg1", args.get(0));
        Assert.assertEquals("arg2", args.get(1));
    }

    /**
     * Test parse method with options and arguments.
     */
    @Test
    public void testParseOptionsAndArguments() throws ParseException {
        String[] arguments = {"--option1", "arg1", "--option2=value"};
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "o2");
        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();

        Assert.assertEquals(2, args.size());
        Assert.assertEquals("arg1", args.get(0));
        Assert.assertEquals(1, args.size() + optionsList.size());

        Option option1 = optionsList.get(0);
        Assert.assertTrue(option1.isSet());
        Assert.assertEquals("o", option1.getOpt());

        Option option2 = optionsList.get(1);
        Assert.assertTrue(option2.isSet());
        Assert.assertEquals("o2", option2.getOpt());
        Assert.assertEquals("value", option2.getValue());
    }

    /**
     * Test parse method with required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        String[] arguments = {"--option1"};
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "o2");
        options.addOptions(Arrays.asList(new Option[]{"option3", true, "o3"}));
        requiredOptions.add("option2");
        requiredOptions.add("option3");
        CommandLine cmd = parser.parse(options, arguments);
        Assert.fail("ParseException expected");
    }

    /**
     * Test parse method with unrecognized option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = {"--unknown"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.fail("UnrecognizedOptionException expected");
    }

    /**
     * Test parse method with properties.
     */
    @Test
    public void testParseProperties() throws ParseException {
        String[] arguments = {};
        Properties properties = new Properties();
        properties.put("option1", "value1");
        properties.put("option2", "value2");

        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        CommandLine cmd = parser.parse(options, arguments, properties);
        List<String> args = cmd.getArgs();
        List<Option> optionsList = cmd.getOptions();

        Assert.assertEquals(0, args.size());
        Assert.assertEquals(2, args.size() + optionsList.size());

        Option option1 = optionsList.get(0);
        Assert.assertTrue(option1.isSet());
        Assert.assertEquals("o1", option1.getOpt());
        Assert.assertEquals("value1", option1.getValue());

        Option option2 = optionsList.get(1);
        Assert.assertTrue(option2.isSet());
        Assert.assertEquals("o2", option2.getOpt());
        Assert.assertEquals("value2", option2.getValue());
    }
}