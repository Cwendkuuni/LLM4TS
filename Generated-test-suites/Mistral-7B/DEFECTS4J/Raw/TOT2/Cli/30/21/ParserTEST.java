import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.util.ArgList;
import org.apache.commons.cli.util.HelpFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParseWithEmptyArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments, null, false);
        Assert.assertNull(cmd.getArgs());
        Assert.assertEquals(Collections.emptyList(), parser.getRequiredOptions());
    }

    @Test
    public void testParseWithValidArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "1", "--optionB", "2", "--optionC=3"};

        CommandLine cmd = parser.parse(options, arguments, null, false);

        List<String> expectedArgs = Arrays.asList("1", "2", "3");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new Option("a", "a", true, "a help message"),
                new Option("b", "b", false, "b help message"),
                new Option("c", "c", true, "c help message")
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a"};

        parser.parse(options, arguments, null, false);
        Assert.fail("Missing argument exception expected");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidArgument() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--invalidOption", "--a=invalid"};

        parser.parse(options, arguments, null, false);
        Assert.fail("ParseException expected");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--missingOption"};

        parser.parse(options, arguments, null, false);
        Assert.fail("MissingOptionException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--unrecognizedOption"};

        parser.parse(options, arguments, null, false);
        Assert.fail("UnrecognizedOptionException expected");
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseMultipleMissingOptions() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--missingOption1", "--missingOption2"};

        parser.parse(options, arguments, null, false);
        Assert.fail("NoSuchOptionException expected");
    }

    @Test
    public void testParseWithOptionalArgument() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "1", "--optionB", "2"};

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> expectedArgs = Arrays.asList("1", "2");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new Option("a", "a", true, "a help message"),
                new Option("b", "b", false, "b help message")
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("myOption", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        Option option = options.getOption("myOption");
        Assert.assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithRequiredOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.setRequiredOption("a", true);

        String[] arguments = new String[0];

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--a"};

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--a=1"};

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> expectedArgs = Arrays.asList("1");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new Option("a", "a", true, "a help message")
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }

    @Test
    public void testParseWithOptionGroup() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "group help message");
        group.setRequired(true);
        group.addOption(new Option("a", true, "a help message"));
        group.addOption(new Option("b", true, "b help message"));

        options.addOptionGroup(group);

        String[] arguments = new String[0];

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--a"};

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--b"};

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--group"};

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--group", "--a"};

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> expectedArgs = Arrays.asList("1");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new OptionGroup("group", "group help message"),
                new Option("a", "a", true, "a help message")
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }

    @Test
    public void testParseWithLongOptions() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("longOptionA", true, "long option A help message");
        options.addOption("longOptionB", false, "long option B help message");

        String[] arguments = {"--longOptionA", "1", "--longOptionB"};

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> expectedArgs = Arrays.asList("1");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new Option("longOptionA", "long option A help message", true, null),
                new Option("longOptionB", "long option B help message", false, null)
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }

    @Test
    public void testParseWithMultipleGroups() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "group1 help message");
        group1.setRequired(true);
        group1.addOption(new Option("option1", true, "group1 option1 help message"));

        OptionGroup group2 = new OptionGroup("group2", "group2 help message");
        group2.addOption(new Option("option2", true, "group2 option2 help message"));
        group2.addOption(new Option("option3", true, "group2 option3 help message"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        String[] arguments = new String[0];

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--option1"};

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--option2", "--option3"};

        try
        {
            parser.parse(options, arguments, null, false);
            Assert.fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        arguments = new String[]{"--group1", "--option1"};

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> expectedArgs = Arrays.asList("1");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new OptionGroup("group1", "group1 help message"),
                new Option("option1", "group1 option1 help message", true, null)
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());

        expectedOptions = Arrays.asList(
                new OptionGroup("group2", "group2 help message"),
                new Option("option2", "group2 option2 help message", true, null),
                new Option("option3", "group2 option3 help message", true, null)
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("a", true, "short option A help message");
        options.addOption("longOptionB", false, "long option B help message");
        options.addOption("longOptionC", true, "long option C help message");

        String[] arguments = {"--a", "--longOptionC=1", "-a"};

        CommandLine cmd = parser.parse(options, arguments, null, false);
        List<String> expectedArgs = Arrays.asList("1", "1");
        Assert.assertEquals(expectedArgs, Arrays.asList(cmd.getArgs()));

        List<Option> expectedOptions = Arrays.asList(
                new Option("a", "short option A help message", true, null),
                new Option("longOptionB", null, false, null),
                new Option("longOptionC", "long option C help message", true, null)
        );
        Assert.assertEquals(expectedOptions, parser.getOptions().getOptions());
    }
}