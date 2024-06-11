import org.junit.Assert;
import org.junit.Test;
import java.util.Properties;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithNullOptionsAndArguments() {
        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(null, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.getOptions().length);
        Assert.assertEquals(0, result.getArgs().length);
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addRequiredOption("b", "bb", true, "Required option 'b'");
        options.addOption("c", true, "Option 'c'");

        String[] arguments = {"-a", "-b", "value", "-c", "arg1", "arg2"};
        CommandLine result = parser.parse(options, arguments);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.getOptions().length);
        Assert.assertEquals(2, result.getArgs().length);
        Assert.assertTrue(result.hasOption("a"));
        Assert.assertTrue(result.hasOption("b"));
        Assert.assertTrue(result.hasOption("c"));
        Assert.assertEquals("value", result.getOptionValue("b"));
        Assert.assertEquals("arg1", result.getArgs()[0]);
        Assert.assertEquals("arg2", result.getArgs()[1]);
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addRequiredOption("b", "bb", true, "Required option 'b'");
        options.addOption("c", true, "Option 'c'");

        String[] arguments = {"-a", "-b", "value", "-c", "arg1", "arg2"};
        CommandLine result = parser.parse(options, arguments, true);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getOptions().length);
        Assert.assertEquals(4, result.getArgs().length);
        Assert.assertTrue(result.hasOption("a"));
        Assert.assertTrue(result.hasOption("b"));
        Assert.assertFalse(result.hasOption("c"));
        Assert.assertEquals("value", result.getOptionValue("b"));
        Assert.assertEquals("-c", result.getArgs()[0]);
        Assert.assertEquals("arg1", result.getArgs()[1]);
        Assert.assertEquals("arg2", result.getArgs()[2]);
        Assert.assertEquals("-c", result.getArgs()[3]);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options. addOption("c", true, "Option 'c'");

        String[] arguments = {"-a"};
        CommandLine result = parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addRequiredOption("b", "bb", true, "Required option 'b'");
        options.addOption("c", true, "Option 'c'");

        Properties properties = new Properties();
        properties.setProperty("c", "value");

        String[] arguments = {"-a", "-b", "value"};
        CommandLine result = parser.parse(options, arguments, properties);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.getOptions().length);
        Assert.assertEquals(0, result.getArgs().length);
        Assert.assertTrue(result.hasOption("a"));
        Assert.assertTrue(result.hasOption("b"));
        Assert.assertTrue(result.hasOption("c"));
        Assert.assertEquals("value", result.getOptionValue("b"));
        Assert.assertEquals("value", result.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option 'a'");
        options.addOption("c", true, "Option 'c'");

        String[] arguments = {"-a", "-b"};
        CommandLine result = parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", true, "Option 'a'");

        String[] arguments = {"-a"};

        CommandLine result = parser.parse(options, arguments);
    }

    private static class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            // provide a dummy implementation for testing purposes
            return new String[0];
        }
    }
}