import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        String[] args = new String[]{"-a", "--optionB", "valueB", "--optionC=valueC", "--optionD"};
        parser = new DefaultParser();
        parser.setOptions(options);
        try
        {
            parser.parse(options, args, (Properties) null, false);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseWithOptions() throws ParseException
    {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--optionB", "valueB", "--optionC=valueC", "--optionD"}, (Properties) null, false);

        assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "--optionC=valueC", "--optionD"}, cmd.getArgs());

        Option optionA = options.getOption("a");
        Option optionB = options.getOption("optionB");
        Option optionC = options.getOption("optionC");

        assertTrue(optionA.isSet());
        assertTrue(optionB.isSet());
        assertTrue(optionC.isSet());

        assertSame(optionB, cmd.getOption("optionB"));
        assertSame(optionC, cmd.getOption("optionC"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        CommandLine cmd = parser.parse(options, new String[]{"-x", "--optionB", "valueB", "--optionC=valueC", "--optionD"}, (Properties) null, false);

        assertArrayEquals(new String[]{"-x"}, cmd.getArgs());

        Option optionA = options.getOption("a");
        Option optionB = options.getOption("optionB");
        Option optionC = options.getOption("optionC");

        assertSame(optionA, cmd.getOption("a"));
        assertNull(cmd.getOption("optionB"));
        assertNull(cmd.getOption("optionC"));

        throw new AssertionError("Expected UnrecognizedOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        options.addOption(new Option("o", "optionO", true, "Option O"));
        CommandLine cmd = parser.parse(options, new String[]{"-o"}, (Properties) null, false);

        assertArrayEquals(new String[]{"-o"}, cmd.getArgs());

        Option optionO = options.getOption("optionO");

        assertFalse(optionO.isSet());
        assertNull(cmd.getOption("optionO"));

        throw new AssertionError("Expected MissingArgumentException");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        options.addOption(new Option("p", "property", true, "Property"));
        Properties properties = new Properties();
        properties.setProperty("property", "value");

        CommandLine cmd = parser.parse(options, new String[]{"--"}, (Properties) properties, false);

        assertArrayEquals(new String[0], cmd.getArgs());

        Option optionP = options.getOption("property");

        assertTrue(optionP.isSet());
        assertSame(optionP, cmd.getOption("property"));
        assertEquals("value", optionP.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullOptions()
    {
        try
        {
            parser.parse((Options) null, new String[0], (Properties) null, false);
        }
        catch (ParseException e)
        {
            throw new AssertionError("Expected IllegalArgumentException");
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithNullArguments()
    {
        try
        {
            parser.parse(options, (String[]) null, (Properties) null, false);
        }
        catch (IllegalArgumentException e)
        {
            throw new AssertionError("Expected ParseException");
        }
    }

    @After
    public void tearDown()
    {
        parser = null;
        options = null;
    }
}