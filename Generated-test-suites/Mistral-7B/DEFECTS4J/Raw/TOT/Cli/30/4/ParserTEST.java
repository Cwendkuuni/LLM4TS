import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParserWithOptionalOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-c"};

        Options options = new Options();
        options.addOption("c", true, "Option C", true, "This is a required option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail("A ParseException should have been thrown");
    }

    @Test(expected = ParseException.class)
    public void testParserWithInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalid"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail("A ParseException should have been thrown");
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        String[] args = new String[]{"--foo=value1", "--bar=value2"};

        Properties properties = new Properties();
        properties.put("foo", "value1");
        properties.put("bar", "value2");

        Options options = new Options();
        options.addOption("foo", true, null, true, "Option Foo");
        options.addOption("bar", true, null, true, "Option Bar");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertEquals(true, cmd.hasOption("bar"));
        Assert.assertEquals("value1", cmd.getOptionValue("foo"));
        Assert.assertEquals("value2", cmd.getOptionValue("bar"));
    }
}