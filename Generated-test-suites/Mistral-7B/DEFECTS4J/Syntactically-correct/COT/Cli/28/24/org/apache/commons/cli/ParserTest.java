import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissngArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testSetOptions()
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);

        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "some option value");
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-o", "test"});

        assertEquals(1, cmd.getArgs().length);
        assertEquals("test", cmd.getArgs()[0]);
        assertEquals(option, cmd.getOption("o"));
        assertEquals("some option value", option.getValue());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "some option value");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);

        parser.parse(options, new String[0]);

        Assert.fail();
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 value");
        Option option2 = new Option("o2", "option2", true, "option2 value");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-o1", "arg1", "-o2", "arg2"});

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals("option1 value", option1.getValue());
        assertEquals(option2, cmd.getOption("o2"));
        assertEquals("option2 value", option2.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "some option value");
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);

        parser.parse(options, new String[]{"-x", "test"});

        Assert.fail();
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "some option value");
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);

        parser.parse(options, new String[]{"-o"});

        Assert.fail();
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 value");
        Option option2 = new Option("o2", "option2", true, "option2 value");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("o1", "arg1");
        properties.setProperty("o2", "arg2");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertEquals(0, cmd.getArgs().length);
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals("arg1", option1.getValue());
        assertEquals(option2, cmd.getOption("o2"));
        assertEquals("arg2", option2.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "some option value");
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"-o", "test"}, true);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("test", cmd.getArgs()[0]);
        assertEquals(option, cmd.getOption("o"));
        assertEquals("some option value", option.getValue());
    }
}

class StubParser extends Parser
{
    // empty implementation
}