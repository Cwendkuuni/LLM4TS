import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        String[] args = {"-a", "1", "--b", "2", "--c=3", "--d=4", "-e", "--f", "--g", "--h"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");
        options.addOption("d", true, "Option D with argument");
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");
        options.addOption("g", false, "Option G");
        options.addOption("h", false, "Option H");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args);

        Assert.assertEquals(1, cmd.getNumArgs());
        Assert.assertEquals("1", cmd.getArg(0));

        Assert.assertEquals(7, cmd.getNumOptions());

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertTrue(cmd.hasOption("e"));
        Assert.assertTrue(cmd.hasOption("f"));
        Assert.assertTrue(cmd.hasOption("g"));
        Assert.assertTrue(cmd.hasOption("h"));

        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertEquals("4", cmd.getOptionValue("d"));

        Assert.assertFalse(cmd.hasOption("i"));

        Assert.assertEquals(Arrays.asList("--e", "--f", "--g", "--h"), cmd.getArgList());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"-a", "1", "--b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");
        options.addRequiredOption("d", false, "Option D");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(args);

        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptionException() throws ParseException
    {
        String[] args = {"-a", "1", "--b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");
        options.addRequiredOption("d", false, "Option D");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(args, new MissingOptionException(""));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = {"--i", "1"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(args);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testUnhandledStopAtNonOption() throws ParseException
    {
        String[] args = {"--", "i", "1"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(args, true);

        Assert.fail();
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"--propertyfile", "test.properties"};

        Options options = new Options();
        options.addOption("propertyfile", true, "Property file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("test.option", "test");
        properties.setProperty("test.option.arg", "arg");

        CommandLine cmd = parser.parse(args, properties);

        Assert.assertEquals("test.properties", cmd.getOptionValue("propertyfile"));
        Assert.assertEquals("test", cmd.getOptionValue("test.option"));
        Assert.assertEquals("arg", cmd.getOptionValue("test.option.arg"));

        Assert.assertTrue(cmd.getArgs().isEmpty());
    }
}