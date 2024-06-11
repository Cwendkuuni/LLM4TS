package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test case for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    

    /**
     * Test for parse method with no arguments.
     */
    @Test
    public void testParseNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    /**
     * Test for parse method with simple arguments.
     */
    @Test
    public void testParseSimpleArguments() throws ParseException {
        String[] arguments = {"option1", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArg(0));
        Assert.assertEquals("arg2", cmd.getArg(1));
    }

    /**
     * Test for parse method with stopAtNonOption flag.
     */
    @Test
    public void testParseStopAtNonOption() throws ParseException {
        String[] arguments = {"--", "-a", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArg(0));
        Assert.assertEquals("arg2", cmd.getArg(1));
    }

    /**
     * Test for parse method with properties.
     */
    @Test
    public void testParseProperties() throws ParseException {
        String[] arguments = {"--foo=bar1", "--foo=bar2"};
        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, arguments, properties);
        Assert.assertEquals(0, cmd.getArgs().size());
        List<Object> values = options.getOption("foo").getValues();
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("bar1", values.get(0));
        Assert.assertEquals("bar2", values.get(1));
    }

    /**
     * Test for parse method with stopAtNonOption flag and properties.
     */
    @Test
    public void testParsePropertiesStopAtNonOption() throws ParseException {
        String[] arguments = {"--", "--foo=bar1", "--foo=bar2", "arg1", "arg2"};
        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, arguments, properties, true);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArg(0));
        Assert.assertEquals("arg2", cmd.getArg(1));
        List<Object> values = options.getOption("foo").getValues();
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("bar1", values.get(0));
        Assert.assertEquals("bar2", values.get(1));
    }

    /**
     * Test for parse method with required options.
     */
    @Test
    public void testParseRequiredOptions() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        options.addRequiredOption("opt1");
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingOptionException e) {
            List<String> requiredOptions = options.getRequiredOptions();
            Assert.assertEquals(1, requiredOptions.size());
            Assert.assertEquals("opt1", requiredOptions.get(0));
        }
    }

    /**
     * Test for parse method with unrecognized option.
     */
    @Test
    public void testParseUnrecognizedOption() {
        String[] arguments = {"--unknown"};
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: unknown", e.getMessage());
        }
    }

    
}