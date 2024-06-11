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
    public void testSetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertNotNull(parser.getOptions());
    }

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(parser.getOptions(), args);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOption("help"));
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(parser.getOptions(), args);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "arg");
        String[] args = {"-a", "value"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-x"};
        parser.parse(parser.getOptions(), args);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissing() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a");
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "argA");
        options.addOption("b", true, "argB");
        String[] args = {"-a", "argA1", "-b", "argB1"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("argA1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("argB1", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidValue() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "arg");
        String[] args = {"-a", "invalidValue"};
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "arg");
        String[] args = {"-a"};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "arg");
        Properties properties = new Properties();
        properties.setProperty("a", "propValue");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("propValue", cmd.getOptionValue("a"));

    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = {"-x"};
        parser.parse(options, args);
    }
}