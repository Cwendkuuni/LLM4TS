package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test parse method with valid arguments and no properties.
     */
    @Test
    public void testParseValidArguments() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "valueB",
                "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "valueB");
        options.addOption("c", true, "valueC");

        CommandLine commandLine = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        }.parse(options, arguments);

        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertEquals("valueB", commandLine.getOptionValue("b"));
        Assert.assertEquals("valueC", commandLine.getOptionValue("c"));
        Assert.assertNull(commandLine.getOptionValue("unknownOption"));
    }

    /**
     * Test parse method with valid arguments and properties.
     */
    @Test
    public void testParseValidArgumentsWithProperties() throws ParseException {
        String[] arguments = new String[]{"--optionB", "valueB", "-a", "1",
                "--optionC=valueC"};
        Properties properties = new Properties();
        properties.setProperty("optionA", "1");

        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "valueB");
        options.addOption("c", true, "valueC");

        CommandLine commandLine = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            public CommandLine parse(Options options, String[] arguments,
                                      Properties properties,
                                      boolean stopAtNonOption)
                    throws ParseException {
                return super.parse(options, arguments, properties, stopAtNonOption);
            }
        }.parse(options, arguments, properties);

        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertEquals("valueB", commandLine.getOptionValue("b"));
        Assert.assertEquals("valueC", commandLine.getOptionValue("c"));
        Assert.assertEquals("1", commandLine.getOptionValue("optionA"));
        Assert.assertNull(commandLine.getOptionValue("unknownOption"));
    }

    /**
     * Test parse method with invalid arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidArguments() throws ParseException {
        String[] arguments = new String[]{"--invalidOption", "value", "-a"};
        Options options = new Options();
        options.addOption("a", true);

        new Parser().parse(options, arguments);
    }

    /**
     * Test parse method with missing required option.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"-a", "1"};
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "valueB");
        options.addRequiredOption("c");

        new Parser().parse(options, arguments);
    }

    /**
     * Test parse method with too many arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseTooManyArguments() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "valueB", "valueC"};
        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true, "valueB");

        new Parser().parse(options, arguments);
    }

    /**
     * Test parse method with unrecognized option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"--unknownOption", "value"};
        Options options = new Options();
        options.addOption("a", true);

        new Parser().parse(options, arguments);
    }

    /**
     * Test parse method with option value contains space.
     */
    @Test
    public void testParseOptionValueWithSpace() throws ParseException {
        String[] arguments = new String[]{"--optionB", "value with space"};
        Options options = new Options();
        options.addOption("b", true, "value with space");

        CommandLine commandLine = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        }.parse(options, arguments);

        Assert.assertEquals("value with space", commandLine.getOptionValue("b"));
    }
}