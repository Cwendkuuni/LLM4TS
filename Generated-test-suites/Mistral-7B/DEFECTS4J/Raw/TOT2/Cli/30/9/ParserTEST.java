package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.util.ArgList;
import org.apache.commons.cli.util.ArgListBuilder;
import org.apache.commons.cli.util.BasicParser;
import org.apache.commons.cli.util.HelpFormatter;
import org.junit.Test;

/**
 * Test for Parser class.
 */
public class ParserTest
{
    /**
     * Test for parsing command line arguments using {@link Parser} with given options.
     */
    @Test
    public void testParse() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--b=2", "--c=3", "-d"};

        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        options.addOption("d", false, "option d");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(1, cmd.getOptionValue("a").intValue());
        assertEquals(2, cmd.getOptionValue("b").intValue());
        assertEquals(3, cmd.getOptionValue("c").intValue());
        assertFalse(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("d"));
        assertNull(cmd.getOptionValue("e"));
    }

    /**
     * Test parsing command line arguments with an invalid option.
     */
    @Test
    public void testParseInvalidOption()
    {
        String[] args = new String[]{"-x"};
        Options options = new Options();
        options.addOption("a", true, "option a");

        CommandLineParser parser = new BasicParser();

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    /**
     * Test parsing command line arguments with missing required option.
     */
    @Test
    public void testParseMissingOption()
    {
        String[] args = new String[]{"--c=3"};
        Options options = new Options();
        options.addOption("a", true, "option a", true);
        options.addOption("c", true, "option c");

        CommandLineParser parser = new BasicParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    /**
     * Test parsing command line arguments with missing required argument.
     */
    @Test
    public void testParseMissingArgument()
    {
        String[] args = new String[]{"--a=1"};
        Options options = new Options();
        options.addOption("a", true, "option a", true);

        CommandLineParser parser = new BasicParser();

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
    }

    /**
     * Test parsing command line arguments with an extra argument.
     */
    @Test
    public void testParseExtraArgument()
    {
        String[] args = new String[]{"-a", "1", "4"};
        Options options = new Options();
        options.addOption("a", true, "option a");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        ArgList argsList = cmd.getArgs();
        List<String> expectedArgs = Arrays.asList("4");

        assertEquals(expectedArgs, argsList);
    }

    /**
     * Test parsing command line arguments with an empty argument list.
     */
    @Test
    public void testParseEmptyArgumentList()
    {
        String[] args = new String[0];
        Options options = new Options();
        options.addOption("a", true, "option a");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test parsing command line arguments with default values.
     */
    @Test
    public void testParseDefaultValues()
    {
        String[] args = new String[]{"--a"};
        Options options = new Options();
        options.addOption("a", true, "option a");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    /**
     * Test parsing command line arguments with long option names.
     */
    @Test
    public void testParseLongOptionNames()
    {
        String[] args = new String[]{"--this-is-a-long-option-name"};
        Options options = new Options();
        options.addOption("this-is-a-long-option-name", true, "a long option");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("this-is-a-long-option-name"));
    }

    /**
     * Test parsing command line arguments with multiple options.
     */
    @Test
    public void testParseMultipleOptions()
    {
        String[] args = new String[]{"-a", "1", "-b", "2", "-c", "3"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertEquals(3, cmd.getArgs().size());
        assertEquals(1, cmd.getOptionValue("a").intValue());
        assertEquals(2, cmd.getOptionValue("b").intValue());
        assertEquals(3, cmd.getOptionValue("c").intValue());
    }

    /**
     * Test parsing command line arguments with options specified multiple times.
     */
    @Test
    public void testParseMultipleOptionInstances()
    {
        String[] args = new String[]{"-a", "1", "-a"};
        Options options = new Options();
        options.addOption("a", true, "option a");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        ArgList argsList = cmd.getArgs();
        List<String> expectedArgs = Arrays.asList();

        assertEquals(expectedArgs, argsList);
        assertEquals(2, cmd.getOptionCount("a"));
    }

    /**
     * Test parsing command line arguments with a group.
     */
    @Test
    public void testParseWithGroup()
    {
        String[] args = new String[]{"--group", "1", "--child=2"};
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("child", true, "Child option"));

        options.addOptionGroup(group);
        options.addOption("group", true, "Group option");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getOptions().isRequired());
        assertEquals(2, cmd.getOptionValue("child", new ArgListBuilder()).size());
    }

    /**
     * Test parsing command line arguments with help text.
     */
    @Test
    public void testParseWithHelpText()
    {
        String[] args = new String[]{"--help"};
        Options options = new Options();

        options.addOption("help", false, "Print this message.");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", "test", options, true);

        assertEquals(help, cmd.getHelp());
    }

    /**
     * Test parsing command line arguments with multiple options having the same long name.
     */
    @Test(expected = ParseException.class)
    public void testParseMultipleOptionsWithSameLongName()
    {
        String[] args = new String[]{"--option"};
        Options options = new Options();
        options.addOption("option", true, "option1");
        options.addOption("option", true, "option2");

        CommandLineParser parser = new BasicParser();

        parser.parse(options, args);
    }

    /**
     * Test parsing command line arguments with a long option name containing a dash.
     */
    @Test
    public void testParseWithLongOptionNameContainingDash()
    {
        String[] args = new String[]{"--this-is-an-option-with-a-dash"};
        Options options = new Options();
        options.addOption("this-is-an-option-with-a-dash", true, "option with a dash");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("this-is-an-option-with-a-dash"));
    }

    /**
     * Test parsing command line arguments with an option having a value.
     */
    @Test
    public void testParseWithOptionValue()
    {
        String[] args = new String[]{"--option=value"};
        Options options = new Options();
        options.addOption("option", true, "option1", "value");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("option"));
    }

    /**
     * Test parsing command line arguments with multiple options having the same short name.
     */
    @Test(expected = ParseException.class)
    public void testParseMultipleOptionsWithSameShortName()
    {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "option1");
        options.addOption("a", true, "option2");

        CommandLineParser parser = new BasicParser();

        parser.parse(options, args);
    }

    /**
     * Test parsing command line arguments with an option having a long name and a short name.
     */
    @Test
    public void testParseWithOptionShortNameAndLongName()
    {
        String[] args = new String[]{"-a", "--option"};
        Options options = new Options();
        options.addOption("a", true, "option1");
        options.addOption("option", true, "option2");

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("option"));
        assertEquals("option1", cmd.getOption("a").getLongOpt());
    }

    /**
     * Test parsing command line arguments with an option that accepts multiple values.
     */
    @Test
    public void testParseWithOptionMultipleValues()
    {
        String[] args = new String[]{"--option1=value1", "--option1=value2"};
        Options options = new Options();
        options.addOption("option1", true, "option1", new ArgListBuilder().build());

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        ArgList optionValues = cmd.getOptionValues("option1");
        List<String> expectedValues = Arrays.asList("value1", "value2");

        assertEquals(expectedValues, optionValues);
    }

    /**
     * Test parsing command line arguments with options having complementary arguments.
     */
    @Test
    public void testParseWithComplementaryArguments()
    {
        String[] args = new String[]{"--optionA", "valueA", "--optionB", "valueB"};
        Options options = new Options();
        Option optionA = new Option("optionA", true, "Option A", new ArgListBuilder().build());
        Option optionB = new Option("optionB", true, "Option B", new ArgListBuilder().build());
        optionA.setExclusive(true);
        optionB.setExclusive(true);
        optionA.setRequired(false);
        optionB.setRequired(false);
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);
        ArgList optionAValues = cmd.getOptionValues("optionA");
        ArgList optionBValues = cmd.getOptionValues("optionB");

        List<String> expectedOptionAValues = Arrays.asList("valueA");
        List<String> expectedOptionBValues = Arrays.asList("valueB");

        assertEquals(expectedOptionAValues, optionAValues);
        assertEquals(expectedOptionBValues, optionBValues);
        assertFalse(optionAValues.add(expectedOptionBValues.get(0)));
        assertFalse(optionBValues.add(expectedOptionAValues.get(0)));
    }
}