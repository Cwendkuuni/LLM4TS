package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     * @throws ParseException
     */
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--option", "-o", "value", "--anotherOption"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValue("option"));
        Assert.assertEquals("o", cmd.getOptionValue("o"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     * @throws ParseException
     */
    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--option", "-o", "value", "--anotherOption", "--property=value"};
        Properties properties = new Properties();
        properties.put("property", "value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValue("option"));
        Assert.assertEquals("o", cmd.getOptionValue("o"));
        Assert.assertEquals("value", cmd.getOptionValue("property"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--option", "-o", "value", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals(Arrays.asList("arg1", "arg2").toString(), cmd.getOptionValues("args").toString());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties)}.
     * @throws ParseException
     */
    @Test
    public void testParseWithStopAtNonOptionAndProperties() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--option", "-o", "value", "arg1", "arg2"};
        Properties properties = new Properties();
        properties.put("property", "value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true, properties);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals(Arrays.asList("arg1", "arg2").toString(), cmd.getOptionValues("args").toString());
        Assert.assertEquals("value", cmd.getOptionValue("property"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties)}.
     * @throws ParseException
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("requiredOption");
        String[] arguments = new String[]{"--anotherOption"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false, Collections.emptyProperties());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties)}.
     * @throws ParseException
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--unknownOption"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false, Collections.emptyProperties());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, Properties)}.
     * @throws ParseException
     */
    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] arguments = new String[]{"--option"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false, Collections.emptyProperties());
    }
}