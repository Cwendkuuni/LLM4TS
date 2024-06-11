package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.test.ParserTest;

/**
 * Test class for Parser class
 */
public class ParserTest
{
    @org.junit.Test
    public void testParser() throws ParseException
    {
        // Given
        String[] args = new String[]{"-a", "1", "--optionB", "valueB", "--optionC", "valueC", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");
        options.addOption("c", true, "option C");
        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, false);

        // Then
        assertNotNull(cmd);
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++)
        {
            assertEquals(args[i], cmd.getArgs().get(i));
        }
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("valueC", cmd.getOptionValue("c"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        // Given
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "option A").required(true);
        Parser parser = new DefaultParser();

        // When
        parser.parse(options, args, false);

        // Then
        fail("ParseException expected but not thrown");
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        // Given
        String[] args = new String[]{"--unrecognizedOption"};
        Options options = new Options();
        Parser parser = new DefaultParser();

        // When
        parser.parse(options, args, false);

        // Then
        fail("UnrecognizedOptionException expected but not thrown");
    }

    @org.junit.Test
    public void testParseWithExtraArguments() throws ParseException
    {
        // Given
        String[] args = new String[]{"-a", "1", "--optionB", "valueB", "--optionC", "valueC", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");
        options.addOption("c", true, "option C");
        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, false);

        // Then
        assertNotNull(cmd);
        assertEquals(args.length - 2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @org.junit.Test
    public void testParseWithOptionGroup() throws ParseException
    {
        // Given
        String[] args = new String[]{"-g", "--groupOption", "value1", "--optionA", "valueA1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "Option A"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, false);

        // Then
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("groupOption"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("value1", cmd.getOptionValue("groupOption"));
        assertEquals("valueA1", cmd.getOptionValue("a"));
    }

    @org.junit.Test
    public void testParseWithProperties() throws ParseException
    {
        // Given
        String[] args = new String[]{"--optionA=valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");
        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, properties, false);

        // Then
        assertNotNull(cmd);
        assertEquals(args.length, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @org.junit.Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        // Given
        String[] args = new String[]{"--optionA", "arg1", "-x"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, false);

        // Then
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertTrue(cmd.hasOption("x"));

        // Given
        args = new String[]{"--optionA", "arg1", "arg2", "-x"};
        // When
        cmd = parser.parse(options, args, true);

        // Then
        assertEquals(3, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertTrue(cmd.hasOption("x"));
    }
}