import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

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
    public void testGetOptions() throws ParseException
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        List<String> expected = Arrays.asList("o");
        List<String> actual = parser.getRequiredOptions();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNullOptions() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = new String[0];
        parser.flatten(null, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithEmptyArguments() throws ParseException
    {
        Options options = new Options();
        Parser parser = new StubParser();
        String[] arguments = new String[0];
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithNullOptions() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = new String[0];
        parser.parse(null, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithNullArguments() throws ParseException
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.parse(options, null);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        String[] arguments = new String[]{"--option"};
        CommandLine commandLine = parser.parse(options, arguments);
        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertFalse(commandLine.hasOption("unknown"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnknownOption() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        String[] arguments = new String[]{"--unknown"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        String[] arguments = new String[]{"--"};
        try
        {
            parser.parse(options, arguments);
        }
        catch (MissingOptionException e)
        {
            // expected
            List<String> expected = Arrays.asList("o");
            List<String> actual = e.getMissingOptions();
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("o", "value");
        CommandLine commandLine = parser.parse(options,new String[0],properties,false);
        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertEquals("value", commandLine.getOptionValue("o"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithExtraArguments() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        String[] arguments = new String[]{"--option","--extra"};
        try
        {
            parser.parse(options, arguments);
        }
        catch (ParseException e)
        {
            // expected
            Assert.assertEquals("Unrecognized option: --extra", e.getMessage());
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        Parser parser = new StubParser();
        parser.setOptions(options);
        parser.parse(options, new String[0]);
        parser.checkRequiredOptions();
    }
}

class StubParser extends Parser
{
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
    {
        return arguments;
    }
}