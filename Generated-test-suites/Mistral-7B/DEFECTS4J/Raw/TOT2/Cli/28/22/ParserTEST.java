import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseSimpleOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "-b", "2", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals(true, cmd.hasOption("c"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParseLongOptions() throws ParseException
    {
        String[] arguments = new String[]{"--long-option-a", "1", "--long-option-b", "2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("longOptionA"));
        Assert.assertEquals(true, cmd.hasOption("longOptionB"));
        Assert.assertEquals("1", cmd.getOptionValue("longOptionA"));
        Assert.assertEquals("2", cmd.getOptionValue("longOptionB"));
    }

    @Test
    public void testParseRequiredOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, true, "Option A");
        options.addOption("b", true, true, "Option B");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, arguments);
            Assert.fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals(Arrays.asList("Option B", "Option A"), e.getMissingOptions());
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] arguments = new String[]{"--property1=value1", "--property2=value2"};
        Options options = new Options();
        options.addOptions(OptionBuilder.withLongOpts(Arrays.asList(
                OptionBuilder.option("property1").hasArg().isRequired().withValueVariableName("value1"),
                OptionBuilder.option("property2").hasArg().isRequired().withValueVariableName("value2"))));

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Properties properties = cmd.getParsedOptions();
        Assert.assertEquals("value1", properties.getProperty("property1"));
        Assert.assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        String[] arguments = new String[]{"-g", "-x", "-y"};
        Options options = new Options();

        OptionGroup group = new OptionGroup("Group X");
        group.setRequired(false);
        options.addOptionGroup(group);

        group.addOption(OptionBuilder.option("x").hasArg().isRequired().withLongOpt("group-x-x"));
        group.addOption(OptionBuilder.option("y").hasArg().isRequired().withLongOpt("group-x-y"));

        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(false, cmd.hasOption("x"));
        Assert.assertEquals(false, cmd.hasOption("y"));

        OptionGroup groupResult = cmd.getOptionGroup("Group X");
        Assert.assertNull(groupResult);
    }

    @Test
    public void testParseWithValueSeparator() throws ParseException
    {
        String[] arguments = new String[]{"--option1=value1,value2", "--option2=value3"};
        Options options = new Options();
        options.addOption("option1", true, true, "Option with value separator");
        options.addOption("option2", true, "Option without value separator");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Properties parsedOptions = cmd.getParsedOptions();
        Assert.assertEquals("value1,value2", parsedOptions.getProperty("option1"));
        Assert.assertEquals(null, parsedOptions.getProperty("option2"));
    }

    @Test
    public void testParseWithMultipleValuesForOneOption() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "-a", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(Arrays.asList("1", "2"), cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithIllegalOption() throws ParseException
    {
        String[] arguments = new String[]{"--illegal-option"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, arguments);
            Assert.fail("Expected UnrecognizedOptionException");
        }
        catch (UnrecognizedOptionException e)
        {
            Assert.assertEquals("Unrecognized option: illegal-option", e.getMessage());
            Assert.assertEquals("illegal-option", e.getOption());
        }
    }

    @Test
    public void testParseWithIllegalOptionValue() throws ParseException
    {
        String[] arguments = new String[]{"-a", "illegal-value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, arguments);
            Assert.fail("Expected MissingArgumentException");
        }
        catch (MissingArgumentException e)
        {
            Assert.assertEquals("Option A", e.getOption());
            Assert.assertEquals("illegal-value", e.getArgument());
        }
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] arguments = new String[]{"--", "non-option"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, arguments, true);
            Assert.assertEquals(Arrays.asList("non-option"), cmd.getArgs());
        }
        catch (ParseException e)
        {
            //Expected, not all arguments were parsed
        }
    }
}