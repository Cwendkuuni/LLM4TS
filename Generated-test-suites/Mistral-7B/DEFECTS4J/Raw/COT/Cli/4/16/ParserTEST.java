package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = {"test", "--option1", "value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", false, "o2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("test", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertFalse(cmd.hasOption("unknownOption"));
        Assert.assertEquals(1, cmd.getOptionValues("option1").length);
        Assert.assertEquals("value1", cmd.getOptionValues("option1")[0]);
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertNull(cmd.getOptionValues("option2")[0]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"test", "--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addRequiredOption("requiredOption", true, "r");

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"test", "--unknownOption", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testNoSuchOption() throws ParseException {
        String[] args = {"test", "--unknownOption"};
        Options options = new Options();
        options.addOption("option", true, "o");

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"test", "--unknownOption", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"test", "--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOption("property1", true, "p1");
        options.addOption("property2", true, "p2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, true);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("property1"));
        Assert.assertEquals("value1", cmd.getOptionValues("property1")[0]);
        Assert.assertTrue(cmd.hasOption("property2"));
        Assert.assertEquals("value2", cmd.getOptionValues("property2")[0]);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"test", "--option1", "value1", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, true);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(1));
        Assert.assertEquals("arg2", cmd.getArgs().get(2));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"test"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addRequiredOption("requiredOption", true, "r");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: requiredOption", e.getMessage());
        }
    }
}