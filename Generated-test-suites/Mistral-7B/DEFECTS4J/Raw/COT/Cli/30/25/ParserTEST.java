import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest
{
    @Test
    public void testSetOptions() throws ParseException
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);
        Assert.assertNotNull(parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() throws ParseException
    {
        Options options = new Options();
        List<String> expectedOptions = new ArrayList<>();
        expectedOptions.add("option1");
        expectedOptions.add("option2");
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        List<String> actualOptions = parser.getRequiredOptions();
        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithoutOptions() throws ParseException
    {
        String[] arguments = new String[0];
        Parser parser = new StubParser();
        parser.parse(new Options(), arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException
    {
        String[] arguments = {"--invalid", "option1", "--test", "value"};
        Options options = new Options();
        Parser parser = new StubParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        String[] arguments = {"--option1", "-o", "value1", "--option2"};
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertNull(cmd.getOptionValue("option2"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] arguments = {"--option1=value1", "--option2=value2"};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] arguments = {"--option1", "--nonOption", "-o", "value"};
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("n", "nonOption", false, "Non-Option");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValue("option1"));
        Assert.assertEquals("--nonOption", cmd.getOptionName(1));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissing() throws ParseException
    {
        String[] arguments = {"--option1"};
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        options.addOption(option1);
        Parser parser = new StubParser();
        parser.setOptions(options);
        List<String> requiredOptions = parser.getRequiredOptions();
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() throws ParseException
    {
        String[] arguments = {"--option1=value1", "--option2=value2"};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        parser.processProperties(properties);
        Assert.assertEquals(2, parser.cmd.getOptions().size());
        Assert.assertEquals(2, parser.cmd.getArgs().size());
        Assert.assertEquals("value1", parser.cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", parser.cmd.getOptionValue("option2"));
    }

    private static class StubParser extends Parser
    {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
        {
            return new String[0];
        }
    }
}