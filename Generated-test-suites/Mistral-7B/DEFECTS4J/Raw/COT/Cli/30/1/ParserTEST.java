import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSetOptions()
    {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        assertNotNull(parser.getOptions());
    }

    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);

        List requiredOptions = parser.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());

        Option requiredOption = new Option("o", "option", true, "option description");
        options.addOption(requiredOption);
        parser.setOptions(options);

        requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(requiredOption.getKey(), requiredOptions.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlattenNullOptions()
    {
        Parser parser = new TestParser();
        parser.flatten(null, new String[0], false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenNoOptions() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = new String[0];

        try
        {
            parser.flatten(options, arguments, false);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testFlattenBasicUsage() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();

        String[] arguments = {"--option", "value"};
        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, false));
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();

        String[] arguments = {"--option", "value", "--no-option"};
        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test(expected = ParseException.class)
    public void testParseNullOptions() throws ParseException
    {
        Parser parser = new TestParser();
        String[] arguments = new String[0];

        try
        {
            parser.parse(null, arguments, null, false);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testParseEmptyArgs() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseBasicUsage() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "option description");
        options.addOption(option);

        String[] arguments = {"--option", "value"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertEquals("value", cmd.getOptionValue("option"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "option description");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("option", "value");

        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        assertEquals("value", cmd.getOptionValue("option"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();

        String[] arguments = {"unknown"};
        try
        {
            parser.parse(options, arguments, null, false);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseUnrecognizedOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option unknownOption = new Option("u", "unknown", false, "unknown description");
        options.addOption(unknownOption);

        String[] arguments = {"-u"};
        try
        {
            parser.parse(options, arguments, null, false);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "option description");
        options.addOption(option);

        String[] arguments = {};
        try
        {
            parser.parse(options, arguments, null, true);
            fail();
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "option description");
        options.addOption(option);

        String[] arguments = {"--option"};
        try
        {
            parser.parse(options, arguments, null, false);
            fail();
        }
        catch (MissingArgumentException e)
        {
            // expected
        }
    }

    @Test
    public void testProcessPropertiesBasicUsage() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "option description");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("option", "value");

        parser.processProperties(properties);
        assertEquals("value", parser.cmd.getOptionValue("option"));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException, MissingOptionException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 description");
        Option option2 = new Option("o2", "option2", true, "option2 description");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"--o2"};
        try
        {
            parser.parse(options, arguments, null, false);
            parser.checkRequiredOptions();
            fail();
        }
        catch (MissingOptionException e)
        {
            // expected
        }

        String[] argumentsWithBothOptions = {"--o1", "--o2"};
        try
        {
            parser.parse(options, argumentsWithBothOptions, null, false);
            parser.checkRequiredOptions();
            // expected no exception
        }
        catch (Exception e)
        {
            fail();
        }
    }
}

class TestParser extends Parser
{
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
    {
        return super.flatten(opts, arguments, stopAtNonOption);
    }
}