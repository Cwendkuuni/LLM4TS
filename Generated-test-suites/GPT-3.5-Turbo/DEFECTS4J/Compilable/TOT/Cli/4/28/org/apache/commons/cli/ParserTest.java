package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new ParserImpl();
    }

    @Test
    public void testParseWithoutOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        CommandLine result = parser.parse(options, args);

        assertEquals(args.length, result.getArgs().length);
        assertEquals(args[0], result.getArgs()[0]);
        assertEquals(args[1], result.getArgs()[1]);
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Option option = new Option("o", "option", true, "Option with argument");
        options.addOption(option);
        String[] args = {"-o", "value"};
        CommandLine result = parser.parse(options, args);

        assertEquals(args.length, result.getOptions().length);
        assertEquals(args[1], result.getOptionValue("o"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"-invalid"};
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Option option = new Option("o", "option", true, "Option with argument");
        option.setRequired(true);
        options.addOption(option);
        String[] args = {"-o"};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Option option = new Option("o", "option", true, "Option with argument");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        String[] args = {};
        CommandLine result = parser.parse(options, args, properties);

        assertEquals(properties.getProperty("option"), result.getOptionValue("o"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Option option = new Option("o", "option", true, "Option with argument");
        options.addOption(option);
        String[] args = {"-o", "value", "non-option"};
        CommandLine result = parser.parse(options, args, true);

        assertEquals(args.length - 1, result.getOptions().length);
        assertEquals(args[1], result.getOptionValue("o"));
        assertEquals(args[2], result.getArgs()[0]);
    }

    @Test
    public void testParseWithDoubleDash() throws ParseException {
        Option option = new Option("o", "option", true, "Option with argument");
        options.addOption(option);
        String[] args = {"-o", "value", "--", "non-option"};
        CommandLine result = parser.parse(options, args);

        assertEquals(args.length - 2, result.getOptions().length);
        assertEquals(args[1], result.getOptionValue("o"));
        assertEquals(args[3], result.getArgs()[0]);
    }

    @Test
    public void testParseWithSingleDash() throws ParseException {
        Option option = new Option("o", "option", true, "Option with argument");
        options.addOption(option);
        String[] args = {"-o", "value", "-"};
        CommandLine result = parser.parse(options, args);

        assertEquals(args.length - 1, result.getOptions().length);
        assertEquals(args[1], result.getOptionValue("o"));
        assertEquals(args[2], result.getArgs()[0]);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}