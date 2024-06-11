import org.junit.Assert;
import org.junit.Test;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse_withOptionsAndArguments_returnsValidCommandLine() {
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {"-f", "test.txt"};

        Parser parser = new ParserImpl();

        CommandLine expected = new CommandLine();
        expected.addOption(option);
        expected.addArg("test.txt");

        try {
            CommandLine actual = parser.parse(options, arguments);
            Assert.assertEquals(expected, actual);
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_withOptionsOnly_returnsValidCommandLine() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose output");
        option.setOptionalArg(true);
        options.addOption(option);

        String[] arguments = {"-v"};

        Parser parser = new ParserImpl();

        CommandLine expected = new CommandLine();
        expected.addOption(option);

        try {
            CommandLine actual = parser.parse(options, arguments);
            Assert.assertEquals(expected, actual);
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_withProperties_returnsValidCommandLine() {
        Options options = new Options();
        Option option = new Option("h", "help", false, "display help");
        option.setOptionalArg(true);
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("help", "true");

        Parser parser = new ParserImpl();

        CommandLine expected = new CommandLine();
        expected.addOption(option);

        try {
            CommandLine actual = parser.parse(options, null, properties);
            Assert.assertEquals(expected, actual);
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        }
    }

    @Test(expected = ParseException.class)
    public void testParse_withMissingRequiredOption_throwsParseException() throws ParseException {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose output");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {};

        Parser parser = new ParserImpl();

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_withStopAtNonOption_returnsValidCommandLine() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        String[] arguments = {"-o", "output.txt", "arg1", "arg2"};

        Parser parser = new ParserImpl();

        CommandLine expected = new CommandLine();
        expected.addOption(option);
        expected.addArg("arg1");
        expected.addArg("arg2");

        try {
            CommandLine actual = parser.parse(options, arguments, true);
            Assert.assertEquals(expected, actual);
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_withDoubleDashStopsParsing() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        String[] arguments = {"-o", "output.txt", "--", "-m"};

        Parser parser = new ParserImpl();

        CommandLine expected = new CommandLine();
        expected.addOption(option);
        expected.addArg("-m");

        try {
            CommandLine actual = parser.parse(options, arguments);
            Assert.assertEquals(expected, actual);
        } catch (ParseException e) {
            Assert.fail("ParseException should not be thrown");
        }
    }
}