import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseWithOptions() throws Exception
    {
        // given
        String[] args = new String[] {"-a", "1", "-b", "2", "--foo", "bar"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("foo", false, "Option Foo");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        // then
        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("foo"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception
    {
        // given
        String[] args = new String[] {"-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true);

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                return arguments;
            }
        };

        // then
        CommandLine cmd = parser.parse(options, args);

        // assert exception
        Assert.fail("Missing required option: a");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception
    {
        // given
        String[] args = new String[] {"-d"};
        Options options = new Options();
        options.addOption("d", true, "Option D", 1);

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                return arguments;
            }
        };

        // then
        CommandLine cmd = parser.parse(options, args);

        // assert exception
        Assert.fail("Option -d requires an argument.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception
    {
        // given
        String[] args = new String[] {"--x", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                return arguments;
            }
        };

        // then
        CommandLine cmd = parser.parse(options, args);

        // assert exception
        Assert.fail("Unrecognized option: -x");
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        // given
        String[] args = new String[] {"--foo=bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");

        Options options = new Options();
        options.addOption("foo", false, "Option Foo");

        // when
        CommandLineParser parser = new Parser()
        {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                return new String[0];
            }

            @Override
            protected void processProperties(Properties properties)
            {
                super.processProperties(properties);
                getOptions().getOption("foo").setValue(properties.getProperty("foo"));
            }
        };

        CommandLine cmd = parser.parse(options, new String[0], properties, false);

        // then
        Assert.assertEquals("bar", cmd.getOptionValue("foo"));
    }
}