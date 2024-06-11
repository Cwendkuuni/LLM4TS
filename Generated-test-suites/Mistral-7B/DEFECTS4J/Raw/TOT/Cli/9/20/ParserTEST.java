package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Parser implementation.
 */
public class ParserTest {

    /**
     * Test parsing command line arguments with no options.
     */
    @Test
    public void testParseNoOptions() throws ParseException {
        String[] arguments = new String[]{"App"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("App", cmd.getArgs().get(0));
    }

    /**
     * Test parsing command line arguments with single value option.
     */
    @Test
    public void testParseSingleValueOption() throws ParseException {
        String[] arguments = new String[]{"-a", "1"};
        Options options = new Options();
        Option option = new Option("a", "optA", true, "Option A");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("1", cmd.getOptionValue("optA"));
    }

    /**
     * Test parsing command line arguments with multiple value option.
     */
    @Test
    public void testParseMultipleValueOption() throws ParseException {
        String[] arguments = new String[]{"--num", "1", "2", "3"};
        Options options = new Options();
        Option option = new Option("num", "optNum", true, "Option for number");
        option.setArgName("num");
        option.setValuesSeparator(',');
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertArrayEquals(new int[]{1, 2, 3}, cmd.getOptionValues("optNum"));
    }

    /**
     * Test parsing command line arguments with boolean option.
     */
    @Test
    public void testParseBooleanOption() throws ParseException {
        String[] arguments = new String[]{"--verbose"};
        Options options = new Options();
        Option option = new Option("verbose", "v", false, "Run verbosely");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("v"));
    }

    /**
     * Test parsing command line arguments with multiple options.
     */
    @Test
    public void testParseMultipleOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "-b", "2"};
        Options options = new Options();
        Option optionA = new Option("a", "optA", true, "Option A");
        Option optionB = new Option("b", "optB", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("1", cmd.getOptionValue("optA"));
        Assert.assertEquals("2", cmd.getOptionValue("optB"));
    }

    /**
     * Test parsing command line arguments with an option and an argument.
     */
    @Test
    public void testParseOptionWithArgument() throws ParseException {
        String[] arguments = new String[]{"--path", "/usr/local/bin"};
        Options options = new Options();
        Option option = new Option("path", "optPath", true, "Option for path");
        option.hasArg(true);
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("/usr/local/bin", cmd.getOptionValue("optPath"));
    }

    /**
     * Test parsing command line arguments with an option that takes an optional argument.
     */
    @Test
    public void testParseOptionalArgument() throws ParseException {
        String[] arguments = new String[]{"--port"};
        Options options = new Options();
        Option option = new Option("port", "optPort", true, "Port number");
        option.hasArg(true);
        option.setRequired(false);
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValue("optPort"));
    }

    /**
     * Test parsing command line arguments with an option that takes a required argument.
     */
    @Test(expected = ParseException.class)
    public void testParseRequiredArgument() throws ParseException {
        String[] arguments = new String[]{"--port"};
        Options options = new Options();
        Option option = new Option("port", "optPort", true, "Port number");
        option.hasArg(true);
        option.setRequired(true);
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.fail("Expected ParseException");
    }

    /**
     * Test parsing command line arguments with multiple occurrences of the same option.
     */
    @Test
    public void testParseMultipleOccurrences() throws ParseException {
        String[] arguments = new String[]{"--num", "1", "--num", "2"};
        Options options = new Options();
        Option option = new Option("num", "optNum", true, "Option for number");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertArrayEquals(new int[]{1, 2}, cmd.getOptionValues("optNum"));
    }

    /**
     * Test parsing command line arguments with an option group.
     */
    @Test
    public void testParseOptionGroup() throws ParseException {
        String[] arguments = new String[]{"--type", "db"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        Option option1 = new Option("type", "optType", true, "Type");
        option1.setRequired(true);
        group.addOptionalOption(option1);
        options.addOption(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("db", cmd.getOptionValue("optType"));
    }

    /**
     * Test parsing command line arguments with an option group and selected option.
     */
    @Test
    public void testParseOptionGroupWithSelectedOption() throws ParseException {
        String[] arguments = new String[]{"--type", "db", "--dbtype", "mysql"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        Option option1 = new Option("type", "optType", true, "Type");
        option1.setRequired(true);
        group.addOptionalOption(option1);
        Option option2 = new Option("dbtype", "optDbtype", true, "Database type");
        group.setSelected(option2);
        options.addOption(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("mysql", cmd.getOptionValue("optDbtype"));
    }

    /**
     * Test parsing command line arguments with an option group and unselected option.
     */
    @Test
    public void testParseOptionGroupWithUnselectedOption() throws ParseException {
        String[] arguments = new String[]{"--type", "db"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        Option option1 = new Option("type", "optType", true, "Type");
        option1.setRequired(true);
        Option option2 = new Option("dbtype", "optDbtype", true, "Database type");
        group.addOptionalOption(option1);
        group.addOptionalOption(option2);
        options.addOption(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValue("optDbtype"));
    }

    /**
     * Test parsing command line arguments with a long option.
     */
    @Test
    public void testParseLongOption() throws ParseException {
        String[] arguments = new String[]{"--long-option", "value"};
        Options options = new Options();
        Option option = new Option("long-option", "longOpt", true, "Long option");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValue("longOpt"));
    }

}