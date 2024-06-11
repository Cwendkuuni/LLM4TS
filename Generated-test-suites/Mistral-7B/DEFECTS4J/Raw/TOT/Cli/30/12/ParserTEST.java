import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testBasicUsage() throws ParseException
    {
        String[] args = {"-a", "1", "--b", "2", "--c", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals(true, cmd.hasOption("c"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getOptionValue("d"));
    }

    @Test
    public void testMissingOption()
    {
        String[] args = {"--a", "1", "--b", "2", "--d", "4"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Missing required option 'd'", e.getMessage());
        }
    }

    @Test
    public void testMissingArgument()
    {
        String[] args = {"--a", "--b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.getOption("b").setRequired(false);

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected MissingArgumentException");
        }
        catch (MissingArgumentException e)
        {
            Assert.assertEquals("Option 'b' requires an argument", e.getMessage());
        }
    }

    @Test
    public void testUnrecognizedOption()
    {
        String[] args = {"--x", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected UnrecognizedOptionException");
        }
        catch (UnrecognizedOptionException e)
        {
            Assert.assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        String[] args = {"--", "nonOption", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("nonOption", cmd.getArgs().get(0));
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = {"--foo=bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("bar", cmd.getOptionValue("foo"));
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException
    {
        String[] args = {"--", "nonOption", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);
        Assert.fail("Expected ParseException");
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.getOption("a").setRequired(true);

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Missing required option 'b'", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentForOption() throws ParseException
    {
        String[] args = {"--a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.getOption("a").setRequired(true);
        options.getOption("a").setValueSeparator(':');

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected MissingArgumentException");
        }
        catch (MissingArgumentException e)
        {
            Assert.assertEquals("Option 'a' requires an argument", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NoSuchOptionException.class)
    public void testNoSuchOption() throws ParseException
    {
        String[] args = {"--x"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected NoSuchOptionException");
        }
        catch (NoSuchOptionException e)
        {
            Assert.assertEquals("Invalid option: x", e.getMessage());
            throw e;
        }
    }
}