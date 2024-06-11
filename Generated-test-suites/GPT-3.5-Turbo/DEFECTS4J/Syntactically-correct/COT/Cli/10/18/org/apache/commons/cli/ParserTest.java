
import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse_OptionsArguments_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", "C", false, "Option C");

        String[] arguments = {"-a", "-b", "value", "-c"};

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return Arrays.copyOf(arguments, arguments.length);
            }
        };

        CommandLine result = parser.parse(options, arguments);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertTrue(result.hasOption("b"));
        Assert.assertTrue(result.hasOption("c"));
        Assert.assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParse_OptionsProperties_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", "C", false, "Option C");

        String[] arguments = {"-a"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");
        properties.setProperty("c", "true");

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return Arrays.copyOf(arguments, arguments.length);
            }
        };

        CommandLine result = parser.parse(options, arguments, properties);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertTrue(result.hasOption("b"));
        Assert.assertTrue(result.hasOption("c"));
        Assert.assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParse_OptionsArgumentsStopAtNonOption_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "nonOption"};

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return Arrays.copyOf(arguments, arguments.length);
            }
        };

        CommandLine result = parser.parse(options, arguments, true);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertFalse(result.hasOption("b"));
        Assert.assertEquals("nonOption", result.getArgs()[0]);
    }

    @Test
    public void testParse_OptionsArgumentsMissingRequiredOptions_ThrowsMissingOptionException() {
        Options options = new Options();
        options.addRequiredOption("a", false, "Option A");
        options.addRequiredOption("b", true, "Option B");

        String[] arguments = {};

        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return Arrays.copyOf(arguments, arguments.length);
            }
        };

        try {
            parser.parse(options, arguments);
            Assert.fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required options: a, b", e.getMessage());
        } catch (ParseException e) {
            Assert.fail("ParseException should not have been thrown");
        }
    }

}
