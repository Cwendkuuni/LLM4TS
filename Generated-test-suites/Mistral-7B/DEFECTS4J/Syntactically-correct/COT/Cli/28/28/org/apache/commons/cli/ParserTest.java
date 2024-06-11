package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.test.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the Parser class.
 */
public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testSetAndGetOptions()
    {
        options.addOption("o", "option", false, "Description");
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithNoArguments() throws ParseException
    {
        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException
    {
        String[] args = {"arg"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException
    {
        options.addOption("o", "option", false, "Description");
        String[] args = {"-o"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals(1, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleOptionAndArgument() throws ParseException
    {
        options.addOption("o", "option", true, "Description");
        String[] args = {"-o", "arg"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals("arg", cmd.getOptionValue("option"));
        assertEquals(1, cmd.getOptions().size());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException
    {
        options.addOption("o1", "option1", false, "Description1");
        options.addOption("o2", "option2", false, "Description2");
        String[] args = {"-o1", "-o2"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals(2, cmd.getOptions().size());
    }

    @Test
    public void testParseWithMultipleOptionsAndArguments() throws ParseException
    {
        options.addOption("o1", "option1", true, "Description1");
        options.addOption("o2", "option2", true, "Description2");
        String[] args = {"-o1", "arg1", "-o2", "arg2"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertEquals("arg1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("arg2", cmd.getOptionValue("option2"));
        assertEquals(2, cmd.getOptions().size());
    }

    @Test
    public void testParseWithMultipleOptionsAndDuplicateOption() throws ParseException
    {
        options.addOption("o", "option", true, "Description");
        String[] args = {"-o", "arg1", "--option", "arg2"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals("arg1", cmd.getOptionValue("option"));
        assertEquals(2, cmd.getOptions().size());
        assertSame(cmd.getOption("option"), cmd.getOption("option"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException
    {
        options.addOption("o", "option", true, "Description");
        options.getOption("o").setRequired(true);
        String[] args = {"--arg"};
        try
        {
            parser.parse(options, args, null);
            fail();
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException
    {
        options.addOption("o", "option", true, "Description");
        String[] args = {"-o"};
        try
        {
            parser.parse(options, args, null);
            fail();
        }
        catch (MissingArgumentException e)
        {
            // expected
        }
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException
    {
        options.addOption("o1", "option1", true, "Description1");
        options.addOption("o2", "option2", false, "Description2");
        String[] args = {"-o1", "arg1", "-o2"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertEquals("arg1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals(2, cmd.getOptions().size());
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        options.addOption("o", "option", false, "Description");
        String[] args = {"-x"};
        try
        {
            parser.parse(options, args, null);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        options.addOption("o", "option", false, "Description");
        Properties props = new Properties();
        props.put("option", "arg");
        CommandLine cmd = parser.parse(options, new String[0], props);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals("arg", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException
    {
        OptionGroup group = new OptionGroup("group", "group description", false);
        group.setRequired(true);
        group.addOption(new Option("o1", "option1", true, "Description1"));
        group.addOption(new Option("o2", "option2", true, "Description2"));
        options.addOption("g", "group", false, "Description", group);
        String[] args = {"-g"};
        try
        {
            parser.parse(options, args, null);
            fail();
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    @Test
    public void testParseWithOptionGroupAndOptions() throws ParseException
    {
        OptionGroup group = new OptionGroup("group", "group description", false);
        group.setRequired(true);
        group.addOption(new Option("o1", "option1", true, "Description1"));
        group.addOption(new Option("o2", "option2", true, "Description2"));
        options.addOption("g", "group", false, "Description", group);
        String[] args = {"-g", "--o1", "arg1", "--o2", "arg2"};
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOption("option1"));
        assertEquals("arg1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("arg2", cmd.getOptionValue("option2"));
        assertEquals(2, cmd.getOptions().size());
        Option groupOption = cmd.getOption("group");
        Option option1 = cmd.getOption("option1");
        Option option2 = cmd.getOption("option2");
        assertSame(groupOption, group.findOption("option1"));
        assertSame(groupOption, groupOption.getParent());
        assertSame(option1, groupOption.getOption("option1"));
        assertSame(option2, groupOption.getOption("option2"));
        assertSame(group, option1.getParent());
        assertSame(group, option2.getParent());
    }
}