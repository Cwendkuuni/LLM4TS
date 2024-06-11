import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link Parser} class.
 */
public class ParserTest
{
    /**
     * Test parsing a command line with valid arguments.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseValidArguments()
        throws ParseException
    {
        // create the Options instance
        Options options = new Options();

        // add some options to the options instance
        options.addOption("a", true, "an option with no argument");
        options.addOption("b", 1, "an option with an integer argument");
        options.addOption("c", 2, "an option with a short argument");
        options.addOption("d", 3, "an option with a long argument");

        // create a Parser instance
        Parser parser = new DefaultParser();

        // parse the arguments
        String[] arguments = new String[] {"--a", "1", "--b=2", "--c=3", "--d=4", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        // check the parsed command line
        Assert.assertEquals("--a", cmd.getOptionValue("a"));
        Assert.assertEquals("1", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertEquals("4", cmd.getOptionValue("d"));
        Assert.assertEquals(2, cmd.getArgs().length);
        Assert.assertEquals("arg1", cmd.getArgs()[0]);
        Assert.assertEquals("arg2", cmd.getArgs()[1]);
    }

    /**
     * Test parsing a command line with missing required options.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions()
        throws ParseException
    {
        // create the Options instance
        Options options = new Options();

        // add some options to the options instance
        options.addOption("a", true, "an option with no argument");
        options.addOption("b", true, "an required option");

        // create a Parser instance
        Parser parser = new DefaultParser();

        // parse the arguments
        String[] arguments = new String[] {"--a"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing a command line with unrecognized options.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOptions()
        throws ParseException
    {
        // create the Options instance
        Options options = new Options();

        // add some options to the options instance
        options.addOption("a", true, "an option with no argument");

        // create a Parser instance
        Parser parser = new DefaultParser();

        // parse the arguments
        String[] arguments = new String[] {"--x"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing a command line with invalid arguments for an option.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidArgumentsForOption()
        throws ParseException
    {
        // create the Options instance
        Options options = new Options();

        // add some options to the options instance
        options.addOption("a", true, "an option with no argument");

        // create a Parser instance
        Parser parser = new DefaultParser();

        // parse the arguments
        String[] arguments = new String[] {"--a=xyz"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing a command line with no arguments.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseNoArguments()
        throws ParseException
    {
        // create the Options instance
        Options options = new Options();

        // add some options to the options instance
        options.addOption("a", true, "an option with no argument");

        // create a Parser instance
        Parser parser = new DefaultParser();

        // parse the arguments
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);

        // check the parsed command line
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    /**
     * Test parsing a command line with a single argument.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseSingleArgument()
        throws ParseException
    {
        // create the Options instance
        Options options = new Options();

        // add some options to the options instance
        options.addOption("a", true, "an option with no argument");

        // create a Parser instance
        Parser parser = new DefaultParser();

        // parse the arguments
        String[] arguments = new String[] {"arg1"};
        CommandLine cmd = parser.parse(options, arguments);

        // check the parsed command line
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("arg1", cmd.getArgs()[0]);
    }
}