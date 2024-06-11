import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleUsage() throws ParseException {
        String[] args = new String[]{"-a", "1", "--b", "2"};
        Options options = new Options();
        options.addRequiredOption("a", true, "A value for option a");
        options.addOption("b", true, "A value for option b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(cmd.getOptionValue("a"), "1");
        Assert.assertEquals(cmd.getOptionValue("b"), "2");
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addRequiredOption("a", true, "A value for option a");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws ParseException {
        String[] args = new String[]{"--c", "3"};
        Options options = new Options();
        options.addOption("a", true, "A value for option a");
        options.addOption("b", true, "A value for option b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail();
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--x", "4"};
        Options options = new Options();
        options.addOption("a", true, "A value for option a");
        options.addOption("b", true, "A value for option b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail();
    }

    @Test(expected = NoSuchOptionException.class)
    public void testNoSuchOptionGroup() throws ParseException {
        String[] args = new String[]{"--group:x"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group name");
        group.addOption(new Option("x", true, "Option x"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        String[] args = new String[]{"--a", "--b"};
        Options options = new Options();
        options.addOption("a", true, "A value for option a");
        options.addOption("b", true, "A value for option b");
        options.addOption("a", true, "Another value for option a");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail();
    }

    @Test
    public void testHelp() throws ParseException {
        String[] args = new String[]{"--help"};
        Options options = new Options();
        options.addOption("a", true, "A value for option a");
        options.addOption("b", true, "A value for option b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ParserTest", "java ParserTest [options]", true);
    }
}