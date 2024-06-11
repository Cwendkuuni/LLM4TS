import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsAndArguments() throws ParseException {
        String[] args = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValues("help"));
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException {
        String[] args = {"-a"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an optional argument");
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertArrayEquals(new String[]{"arg"}, cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        String[] args = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an required argument");
        options.addOption(opt);
        try {
            parser.parse(options, args);
            Assert.fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            Assert.assertEquals("Required option 'a' is missing", e.getMessage());
        }
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        String[] args = {"--long-option"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("lo", "long-option", false, "a long option");
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("lo"));
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException {
        String[] args = {"-a", "--long-option", "-b"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optA = new Option("a", "argA", true, "option A");
        Option optB = new Option("b", "argB", true, "option B");
        Option optLong = new Option("lo", "long-option", false, "a long option");
        options.addOption(optA);
        options.addOption(optLong);
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("argA", cmd.getArgs()[0]);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("lo"));
        Assert.assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {};
        Properties properties = new Properties();
        properties.setProperty("myOption", "myValue");
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("m", "myOption", true, "an option from properties");
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, (String[]) null, properties, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertArrayEquals(new String[]{"myValue"}, cmd.getOptionValues("m"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = {"-a"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "an required argument");
        options.addOption(opt);
        try {
            parser.parse(options, args);
        } catch (ParseException e) {
            throw new AssertionError("Unexpected exception", e);
        }
        Assert.fail("MissingArgumentException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown-option"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        try {
            parser.parse(options, args);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: unknown-option", e.getMessage());
        }
    }
}