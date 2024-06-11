package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test for {@link Parser} class.
 *
 * @author John Doe (john.doe@example.com)
 */
public class ParserTest
{
    /**
     * Test parsing arguments with no options.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseNoOptions() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments, false);
        assertNull(cmd.getOptions());
        List<String> args = cmd.getArgs();
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("myprogram", args.get(2));
    }

    /**
     * Test parsing arguments with required options.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseRequiredOptions() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--option1", "--option2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        option1.setRequired(true);
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertEquals(option1.getLongOpt(), optionsList.get(0).getLongOpt());
        assertTrue(optionsList.get(1) instanceof Option);
        assertEquals(option2.getLongOpt(), optionsList.get(1).getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    /**
     * Test parsing arguments with optional options.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseOptionalOptions() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--option"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(false);
        options.addOption(option);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertEquals(option.getLongOpt(), optionsList.get(0).getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    /**
     * Test parsing arguments with a boolean option.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseBooleanOption() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--boolean"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("b", "boolean", false, "Boolean Option");
        options.addOption(option);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertEquals(option.getLongOpt(), optionsList.get(0).getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
        assertTrue(cmd.hasOption("boolean"));
    }

    /**
     * Test parsing arguments with an option that takes an argument.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseOptionWithArgument() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--option=arg"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setArgName("arg");
        options.addOption(option);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertEquals(option.getLongOpt(), optionsList.get(0).getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg", args.get(0));
    }

    /**
     * Test parsing arguments with a group of options.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseOptionGroup() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--group=option"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option option = new Option("o", "option", true, "Option");
        group.addOption(option);
        options.addOptionGroup(group);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0) instanceof OptionGroup);
        OptionGroup groupObj = (OptionGroup) optionsList.get(0);
        assertEquals("group", groupObj.getName());
        assertEquals("Group", groupObj.getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    /**
     * Test parsing arguments with a required option that takes an argument.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseRequiredOptionWithArgument() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--option=arg"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setArgName("arg");
        option.setRequired(true);
        options.addOption(option);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertEquals(option.getLongOpt(), optionsList.get(0).getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("arg", args.get(0));
    }

    /**
     * Test parsing arguments with invalid options.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseInvalidOptions()
    {
        String[] arguments = new String[]{"myprogram", "--invalid", "--option", "--", "--invalid-group"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, false));
    }

    /**
     * Test parsing arguments with missing arguments.
     *
     * @throws ParseException if there is a problem parsing the arguments
     */
    @Test
    public void testParseMissingArguments() throws ParseException
    {
        String[] arguments = new String[]{"myprogram", "--option"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setArgName("arg");
        options.addOption(option);
        CommandLine cmd = parser.parse(options, arguments, false);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        assertTrue(optionsList.get(0) instanceof Option);
        assertEquals(option.getLongOpt(), optionsList.get(0).getLongOpt());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
        assertThrows(MissingArgumentException.class, () -> cmd.getStringOptionValue("option"));
    }
}