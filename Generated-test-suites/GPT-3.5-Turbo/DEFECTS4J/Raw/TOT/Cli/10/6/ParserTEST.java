import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse_NullOptions() {
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};
        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(null, arguments);

        Assert.assertNotNull(commandLine);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParse_NullArguments() {
        Options options = new Options();
        Option option = new Option("a", true, "Option a");
        options.addOption(option);

        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(options, null);

        Assert.assertNotNull(commandLine);
        Assert.assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParse_NoArguments() {
        Options options = new Options();
        Option option = new Option("a", true, "Option a");
        options.addOption(option);

        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(options, new String[]{});

        Assert.assertNotNull(commandLine);
        Assert.assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParse_ShortAndLongOptions() {
        Options options = new Options();
        Option option1 = new Option("a", true, "Option a");
        Option option2 = new Option("b", true, "Option b");
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(options, new String[]{"-a", "value1", "--b", "value2"});

        Assert.assertNotNull(commandLine);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParse_StopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", true, "Option a");
        options.addOption(option);

        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(options, new String[]{"-a", "value1", "nonOption"});

        Assert.assertNotNull(commandLine);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertEquals(1, commandLine.getArgs().length);
        Assert.assertEquals("nonOption", commandLine.getArgs()[0]);
    }

    @Test
    public void testParse_UnrecognizedOption() {
        Options options = new Options();
        Option option = new Option("a", true, "Option a");
        options.addOption(option);

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, new String[]{"-b", "value1"});
            Assert.fail("ParseException should have been thrown");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_MissingOptionArgument() {
        Options options = new Options();
        Option option = new Option("a", true, "Option a");
        options.addOption(option);

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, new String[]{"-a"});
            Assert.fail("MissingArgumentException should have been thrown");
        } catch (MissingArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_RequiredOptionsMissing() {
        Options options = new Options();
        Option option1 = new Option("a", true, "Option a");
        option1.setRequired(true);
        Option option2 = new Option("b", true, "Option b");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, new String[]{"-a", "value1"});
            Assert.fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            // Expected exception
        }
    }
}

class ParserImpl extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
    }
}