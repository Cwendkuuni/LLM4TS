import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse_OptionsAndArguments_ParsesSuccessfully() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[]{"-a"};

        // Act
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);

        // Assert
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidOptions_ThrowsParseException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[]{"-b"};

        // Act
        Parser parser = new ParserImpl();
        parser.parse(options, args);
    }

    @Test
    public void testParse_NullArguments_ParsesSuccessfully() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");

        // Act
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, null);

        // Assert
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParse_StopAtNonOption_StopParsingSuccessfully() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[]{"-a", "nonOption"};

        // Act
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args, true);

        // Assert
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertArrayEquals(new String[]{"nonOption"}, cmd.getArgs());
    }

    @Test
    public void testParse_PropertiesAndArguments_ParsesSuccessfully() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");

        Properties properties = new Properties();
        properties.setProperty("b", "true");

        String[] args = new String[]{"-a"};

        // Act
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args, properties);

        // Assert
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionsMissing_ThrowsMissingOptionException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());

        String[] args = new String[]{};

        // Act
        Parser parser = new ParserImpl();
        parser.parse(options, args);
    }

    class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}