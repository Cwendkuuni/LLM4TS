import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithValidOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");

        String[] args = {"-a", "--b=1"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assert cmd.hasOption("a");
        assert cmd.hasOption("b");
        assert cmd.getOptionValue("b").equals("1");
        assert cmd.getArgs().length == 0;
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A (required)");
        options.addRequiredOption("b", true, "option B (required)");

        String[] args = {"-a"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            fail("MissingRequiredOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option 'b'", e.getMessage());
        }

        args = new String[]{"-a", "--b=1"};
        CommandLine cmd = parser.parse(options, args);

        assert cmd.hasOption("a");
        assert cmd.hasOption("b");
        assert cmd.getOptionValue("b").equals("1");
        assert cmd.getArgs().length == 0;
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", true, "option A");

        String[] args = {"--x", "--a"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: x", e.getMessage());
        }

        String[] argsWithQuotedUnrecognizedOption = {"--x=\"\"", "--a"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, argsWithQuotedUnrecognizedOption);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");

        String[] args = {"-a"};
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addRequiredOption("b", true, "option B (required)");

        String[] args = {"-a"};
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");

        String[] args = {"-a", "-a", "value1", "-a", "value2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Option optionA = cmd.getOption("a");
        List<String> values = optionA.getValues();
        assertEquals(values.size(), 3);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");

        String[] args = {};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assert cmd.hasOption("a");
        assert cmd.getArgs().length == 0;
    }
}