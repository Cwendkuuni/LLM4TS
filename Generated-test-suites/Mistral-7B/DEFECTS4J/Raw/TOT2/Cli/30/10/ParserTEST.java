package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParsingSimpleCommandLine() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a", "-b", "--", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(2, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParsingCommandLineWithOptionalArg() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a", "value1", "--", "-c", "value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(3, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParsingCommandLineWithMissingOptionalArg() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a", "--", "-c", "value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when & then
        parser.parse(options, arguments);
    }

    @Test
    public void testParsingCommandLineWithLongOptions() throws ParseException
    {
        // given
        String[] arguments = new String[]{"--long-option-a", "value1", "--long-option-b=value2", "--"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(0, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("longOptionA"));
        Assert.assertEquals("value1", commandLine.getOptionValue("longOptionA"));
        Assert.assertTrue(commandLine.hasOption("longOptionB"));
        Assert.assertEquals("value2", commandLine.getOptionValue("longOptionB"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParsingCommandLineWithUnrecognizedOption() throws ParseException
    {
        // given
        String[] arguments = new String[]{"--unknown-option", "--"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        // when & then
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParsingCommandLineWithMissingRequiredOption() throws ParseException
    {
        // given
        String[] arguments = new String[]{"--", "--required-option"};
        Options options = new Options();
        options.addOption("requiredOption", true, "Required option");
        options.setRequiredOption(options.getOption("requiredOption"), true);

        Parser parser = new DefaultParser();

        // when & then
        parser.parse(options, arguments);
    }

    @Test
    public void testParsingCommandLineWithMixedShortAndLongOptions() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a", "--long-option-b=value2", "-c", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOptionB", true, "Long option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("longOptionB"));
        Assert.assertEquals("value2", commandLine.getOptionValue("longOptionB"));
        Assert.assertTrue(commandLine.hasOption("c"));
        Assert.assertEquals("", commandLine.getOptionValue("c"));
    }

    @Test
    public void testParsingCommandLineWithMixedShortAndLongOptionsWithValues() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a=value1", "--long-option-b=value2", "-c", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOptionB", true, "Long option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(0, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("longOptionB"));
        Assert.assertEquals("value2", commandLine.getOptionValue("longOptionB"));
        Assert.assertTrue(commandLine.hasOption("c"));
        Assert.assertEquals("", commandLine.getOptionValue("c"));
    }

    @Test
    public void testParsingCommandLineWithMixedShortAndLongOptionsWithMandatoryShortOption() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a", "value1", "-b", "--", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("a", 1);

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("", commandLine.getOptionValue("b"));
        Assert.assertTrue(commandLine.hasOption("c"));
        Assert.assertEquals("", commandLine.getOptionValue("c"));
    }

    @Test
    public void testParsingCommandLineWithMixedShortAndLongOptionsWithMandatoryShortOptionAndValues() throws ParseException
    {
        // given
        String[] arguments = new String[]{"-a=value1", "value2", "-b", "--", "-c=value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("a", 1);

        Parser parser = new DefaultParser();

        // when
        CommandLine commandLine = parser.parse(options, arguments);

        // then
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("value2", commandLine.getOptionValue("b"));
        Assert.assertTrue(commandLine.hasOption("c"));
        Assert.assertEquals("value3", commandLine.getOptionValue("c"));
    }
}