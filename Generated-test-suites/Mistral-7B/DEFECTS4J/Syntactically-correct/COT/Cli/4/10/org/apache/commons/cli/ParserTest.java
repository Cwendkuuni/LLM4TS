package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test cases for Parser class
 */
public class ParserTest {

    @Test
    public void testParserAbstractClass() {
        Assert.assertNotNull(Parser.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodFlatten() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            parser.parse(new Options(), new String[0]);
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParse() {
        String[] args = new String[]{"-a", "--optionB=valueB", "-o", "output.txt"};
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "option description for A");
        Option optionB = new Option("b", "optionB", true, "option description for B");
        optionB.setLongOpt("longOptionB");
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            Assert.assertArrayEquals(new String[]{"optionA", "longOptionB=valueB", "output.txt"}, cmd.getArgs());
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertTrue(cmd.hasOption("b"));
            Assert.assertEquals("valueB", cmd.getOptionValue("b"));
            Assert.assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() {
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        Option optionA = new Option("a", "optionA", true, "option description for A");
        Option optionB = new Option("b", "optionB", false, "option description for B");
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
            Assert.assertEquals("Missing required option 'optionB'", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() {
        String[] args = new String[]{"--optionA", "valueA"};
        Options options = new Options();
        Option optionA = new Option("a", "optionA", true, "option description for A");
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            Assert.fail();
        } catch (ParseException e) {
            // expected
            Assert.assertEquals("Unrecognized option: valueA", e.getMessage());
        }
    }

    @Test
    public void testParseWithProperties() {
        String[] args = new String[]{"--optionA", "--optionB=valueB", "-o"};
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");

        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "option description for A");
        Option optionB = new Option("b", "optionB", true, "option description for B");
        optionB.setLongOpt("longOptionB");
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, properties, false);
            Assert.assertArrayEquals(new String[]{"optionA", "longOptionB=valueB"}, cmd.getArgs());
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertTrue(cmd.hasOption("b"));
            Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testParseWithStopAtNonOption() {
        String[] args = new String[]{"--optionA", "valueA", "nonOption"};
        Options options = new Options();
        Option optionA = new Option("a", "optionA", true, "option description for A");
        Option optionB = new Option("b", "optionB", false, "option description for B");
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, null, true);
            Assert.assertArrayEquals(new String[]{"optionA", "nonOption"}, cmd.getArgs());
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertFalse(cmd.hasOption("b"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}