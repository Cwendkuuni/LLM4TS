import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        String[] args = new String[]{"--option1", "-o1", "value1", "--option2", "-o2", "value2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(2, cmd.getNumArgs());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertEquals("arg1", cmd.getArgs()[0]);
        Assert.assertEquals("arg2", cmd.getArgs()[1]);

        // check that the options are present
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = new String[]{"--option1", "-o1", "value1", "--unknown", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, false);

        Assert.fail("Expected ParseException but it did not occur");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"--unknowneOption", "-u", "value"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, false);

        Assert.fail("Expected UnrecognizedOptionException but it did not occur");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        String[] args = new String[]{"--option1", "-o1"};
        Options options = new Options();
        options.addOption("option1", true, "o1").requiredArg();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, false);

        Assert.fail("Expected MissingArgumentException but it did not occur");
    }

    @Test
    public void testParseProperties() throws ParseException
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2"};
        Properties properties = new Properties();
        properties.put("option1", "value1");
        properties.put("option2", "value2");

        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(0, cmd.getNumArgs());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test(expected = ParseException.class)
    public void testParsePropertiesMissingOption() throws ParseException
    {
        String[] args = new String[]{"--option1=value1", "--unknown=value"};
        Properties properties = new Properties();
        properties.put("option1", "value1");

        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, properties, false);

        Assert.fail("Expected ParseException but it did not occur");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParsePropertiesUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"--unknownOption=value"};
        Properties properties = new Properties();

        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, properties, false);

        Assert.fail("Expected UnrecognizedOptionException but it did not occur");
    }
}