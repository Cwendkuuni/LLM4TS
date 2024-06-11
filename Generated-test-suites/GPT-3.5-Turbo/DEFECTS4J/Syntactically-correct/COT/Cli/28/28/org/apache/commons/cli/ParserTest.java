import org.junit.Test;
import org.junit.Assert;

public class ParserTest {

    private Parser parser;

    @Test
    public void testParse_WithNullArguments_ReturnsEmptyCommandLine() throws ParseException {
        Options options = new Options();
        String[] arguments = null;
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        CommandLine actual = parser.parse(options, arguments);

        CommandLine expected = new CommandLine();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithNoOptions_ReturnsEmptyCommandLine() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        CommandLine actual = parser.parse(options, arguments);

        CommandLine expected = new CommandLine();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithOneOption_ReturnsCommandLineWithOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option", false, "No argument option");
        options.addOption(option);
        String[] arguments = new String[]{"-a"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"-a"};
            }
        };

        CommandLine actual = parser.parse(options, arguments);

        CommandLine expected = new CommandLine();
        expected.addOption(option);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithMultipleOptions_ReturnsCommandLineWithAllOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "No argument option");
        Option option2 = new Option("b", "option2", true, "Option with argument");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"-a", "-b", "value"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"-a", "-b", "value"};
            }
        };

        CommandLine actual = parser.parse(options, arguments);

        CommandLine expected = new CommandLine();
        expected.addOption(option1);
        expected.addOption(option2, "value");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParse_WithUnrecognizedOption_ThrowsUnrecognizedOptionException() {
        Options options = new Options();
        String[] arguments = new String[]{"-c"};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"-c"};
            }
        };

        try {
            parser.parse(options, arguments);
            Assert.fail("Expected UnrecognizedOptionException to be thrown");
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: -c", e.getMessage());
            Assert.assertEquals("-c", e.getOption());
        } catch (ParseException e) {
            Assert.fail("Expected UnrecognizedOptionException to be thrown, instead got ParseException");
        }
    }

    @Test
    public void testParse_WithRequiredOptionNotPresent_ThrowsMissingOptionException() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "Required option");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = new String[]{};
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        try {
            parser.parse(options, arguments);
            Assert.fail("Expected MissingOptionException to be thrown");
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: a", e.getMessage());
            Assert.assertEquals(1, e.getMissingOptions().size());
            Assert.assertEquals(option, e.getMissingOptions().get(0));
        } catch (ParseException e) {
            Assert.fail("Expected MissingOptionException to be thrown, instead got ParseException");
        }
    }

}