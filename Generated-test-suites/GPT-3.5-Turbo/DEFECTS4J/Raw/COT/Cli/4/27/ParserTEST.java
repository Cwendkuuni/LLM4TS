
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("optionB"));
        Assert.assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithoutOptionsAndArguments() throws ParseException {
        Options options = new Options();

        String[] arguments = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("optionA"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("optionB"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "Option A");
        optionA.setRequired(true);
        options.addOption(optionA);

        String[] arguments = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"-a"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }
}
