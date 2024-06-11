import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testEmptyOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.assertNull(cmd.getArgs());
        Assert.assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testOptionsWithValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", 2, "Print B twice");
        String[] arguments = {"-a", "-b", "3"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(new String[]{"3"}, cmd.getArgs());
        Assert.assertEquals(1, cmd.getOptions().size());
        Option optionA = cmd.getOption("a");
        Assert.assertTrue(optionA.isSet());
        Assert.assertFalse(optionA.hasValue());
        Option optionB = cmd.getOption("b");
        Assert.assertTrue(optionB.isSet());
        Assert.assertEquals(2, optionB.getValues().length);
        Assert.assertEquals("3", optionB.getValues()[0]);
    }

    @Test
    public void testOptionsWithLongValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", 2, "Print B twice");
        String[] arguments = {"--long-option-a", "--long-option-b=3"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(new String[0], cmd.getArgs());
        Assert.assertEquals(1, cmd.getOptions().size());
        Option optionA = cmd.getOption("a");
        Assert.assertTrue(optionA.isSet());
        Assert.assertFalse(optionA.hasValue());
        Option optionB = cmd.getOption("b");
        Assert.assertTrue(optionB.isSet());
        Assert.assertEquals(1, optionB.getValues().length);
        Assert.assertEquals("3", optionB.getValues()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--unknown"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Print A", true);
        String[] arguments = {"--b"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", 2, "Print B twice", true);
        String[] arguments = {"-a"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
    }

    @Test
    public void testProperties() throws ParseException {
        Options options = new Options();
        options.addPropertySetter(new StringPropertySetter("foo", "bar"));
        String[] arguments = {};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null);
        Assert.assertEquals("bar", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", 2, "Print B twice");
        Properties properties = new Properties();
        properties.setProperty("b", "3");
        String[] arguments = {"-a"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);
        Assert.assertEquals(new String[0], cmd.getArgs());
        Assert.assertEquals(1, cmd.getOptions().size());
        Option optionA = cmd.getOption("a");
        Assert.assertTrue(optionA.isSet());
        Assert.assertFalse(optionA.hasValue());
        Option optionB = cmd.getOption("b");
        Assert.assertTrue(optionB.isSet());
        Assert.assertEquals(1, optionB.getValues().length);
        Assert.assertEquals("3", optionB.getValues()[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Print A");
        options.addOption("b", 2, "Print B twice");
        String[] arguments = {"-a", "1", "-b"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
    }
}