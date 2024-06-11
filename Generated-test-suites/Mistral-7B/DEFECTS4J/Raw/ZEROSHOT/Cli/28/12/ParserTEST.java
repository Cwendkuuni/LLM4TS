import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseSimpleCommandLine() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseCommandLineWithUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"-x", "--optionA", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertTrue(cmd.getArgs().length > 0);
        Assert.assertEquals("x", cmd.getArgs()[0]);

        List<String> unrecognizedOptions = cmd.getUnrecognizedOptions();
        Assert.assertEquals(1, unrecognizedOptions.size());
        Assert.assertEquals("x", unrecognizedOptions.get(0));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseCommandLineWithMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true, "An required option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.fail(); // Should throw MissingOptionException
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseCommandLineWithMissingArgument() throws ParseException {
        String[] arguments = new String[]{"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "An option with a required argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.fail(); // Should throw MissingArgumentException
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithInvalidOptionValue() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "abc"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.fail(); // Should throw ParseException
    }

    @Test
    public void testParseCommandLineWithProperties() throws ParseException {
        String[] arguments = new String[]{"--property1", "value1", "--property2", "value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOption("p", true, "property", true, "property option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments, properties, false);

        Assert.assertEquals(true, cmd.hasOption("property1"));
        Assert.assertEquals("value1", cmd.getOptionValue("property1"));
        Assert.assertEquals(true, cmd.hasOption("property2"));
        Assert.assertEquals("value2", cmd.getOptionValue("property2"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseCommandLineWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments, null, true);

        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("arg1", cmd.getArgs()[0]);
        Assert.assertEquals("arg2", cmd.getArgs()[1]);
    }
}

class DefaultParser extends Parser {
    // Empty implementation
}