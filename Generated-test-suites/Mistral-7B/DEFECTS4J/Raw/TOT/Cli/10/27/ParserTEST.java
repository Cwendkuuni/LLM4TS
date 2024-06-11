package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for {@link Parser}.
 */
public class ParserTest {

    /**
     * Test parsing with valid options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseValidOptions() throws ParseException {
        String[] args = {"-a", "-b", "--foo=bar", "--", "-c"};
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption("a", true);
        expectedCmd.addOption("b", true);
        expectedCmd.addOption("foo", "bar");
        expectedCmd.addArg("");
        expectedCmd.addArg("c");

        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        options.addOption("foo", true, "f");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        Assert.assertEquals(expectedCmd, cmd);
    }

    /**
     * Test parsing with invalid options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidOptions() throws ParseException {
        String[] args = {"--invalid", "-x", "--foo=bar", "--", "-c"};
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        options.addOption("foo", true, "f");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args);
    }

    /**
     * Test parsing with required options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        String[] args = {"-a", "--", "-c"};
        Options options = new Options();
        options.addOption("a", true, "a")
                .required(true);
        options.addOption("b", true, "b");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args);
    }

    /**
     * Test parsing with optional options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseOptionalOptions() throws ParseException {
        String[] args = {"-a", "--foo=bar", "--", "-c"};
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption("a", true);
        expectedCmd.addOption("foo", "bar");
        expectedCmd.addArg("");
        expectedCmd.addArg("c");

        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("foo", true, "f");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        Assert.assertEquals(expectedCmd, cmd);
    }

    /**
     * Test parsing with properties.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseProperties() throws ParseException {
        String[] args = {"--myOption=value1", "--foo=value2", "--", "-c"};
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption("myOption", true, "m")
                .setValue("value1");
        expectedCmd.addOption("foo", true, "f")
                .setValue("value2");
        expectedCmd.addArg("");
        expectedCmd.addArg("c");

        Options options = new Options();
        options.addOption("myOption", true, "m");
        options.addOption("foo", true, "f");

        Properties properties = new Properties();
        properties.setProperty("myOption", "value1");
        properties.setProperty("foo", "value2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, properties);

        Assert.assertEquals(expectedCmd, cmd);
    }

    /**
     * Test parsing with stopAtNonOption.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseStopAtNonOption() throws ParseException {
        String[] args = {"--", "-a", "-b", "-c"};
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addArg("");
        expectedCmd.addArg("a");
        expectedCmd.addArg("b");
        expectedCmd.addArg("c");

        Options options = new Options();
        options.addOption("a", true);
        options.addOption("b", true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, null, true);

        Assert.assertEquals(expectedCmd, parser.getLastParsedCommandLine());
    }
}