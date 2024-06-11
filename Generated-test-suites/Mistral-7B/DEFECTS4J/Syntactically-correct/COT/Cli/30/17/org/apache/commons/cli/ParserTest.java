package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParseWithDefaultOptions() throws ParseException {
        String[] args = new String[]{"--test", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createTestOptions(), args, null);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = new String[]{"--test", "--optionA", "--optionB"};

        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.requireOption("optionA", true);
        options.requireOption("optionB", true);

        try {
            CommandLine cmd = parser.parse(options, args, null);
            fail("Expected ParseException with missing options.");
        } catch (ParseException e) {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(2, missingOptions.size());
            assertTrue(missingOptions.contains("optionA"));
            assertTrue(missingOptions.contains("optionB"));
        }
    }

    @Test
    public void testParseWithOptionalArguments() throws ParseException {
        String[] args = new String[]{"--test", "--optionA=valueA", "--optionB=valueB"};

        Parser parser = new DefaultParser();
        Options options = createTestOptions();

        Option optionA = options.getOption("optionA");
        optionA.setRequired(false);

        Option optionB = options.getOption("optionB");
        optionB.setRequired(false);

        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
        Assert.assertEquals("valueB", cmd.getOptionValue("optionB"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = new String[]{"--test", "--invalidOption"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createTestOptions(), args, null);

        fail("Expected ParseException with unrecognized option.");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingDoubleDash() throws ParseException {
        String[] args = new String[]{"--test", "optionA", "valueA", "optionB"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createTestOptions(), args, null);

        fail("Expected ParseException with missing double-dash.");
    }

    @Test(expected = ParseException.class)
    public void testParseWithTooManyArguments() throws ParseException {
        String[] args = new String[]{"--test", "arg1", "arg2", "arg3"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createTestOptions(), args, null);

        fail("Expected ParseException with too many arguments.");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        String[] args = new String[]{"--test"};

        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        options.requireOption("optionA", true);
        options.requireOption("optionB", true);

        try {
            CommandLine cmd = parser.parse(options, args, null);
            fail("Expected MissingOptionException with missing required options.");
        } catch (MissingOptionException e) {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(2, missingOptions.size());
            assertTrue(missingOptions.contains("optionA"));
            assertTrue(missingOptions.contains("optionB"));
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingRequiredArgument() throws ParseException {
        String[] args = new String[]{"--test", "--optionA"};

        Parser parser = new DefaultParser();
        Options options = createTestOptions();
        Option optionA = options.getOption("optionA");
        optionA.setRequired(true);

        try {
            CommandLine cmd = parser.parse(options, args, null);
            fail("Expected MissingArgumentException with missing required argument.");
        } catch (MissingArgumentException e) {
            assertEquals("optionA", e.getOption().getKey());
        }
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--test", "--unknownOption"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createTestOptions(), args, null);

        fail("Expected UnrecognizedOptionException with unrecognized option.");
    }

    private Options createTestOptions() {
        Options options = new Options();
        options.addOption("test", true, "t", "Test mode");
        options.addOption("optionA", true, null, "Option A");
        options.addOption("optionB", true, null, "Option B");

        return options;
    }
}