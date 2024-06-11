import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("t", "test", false, "testing option");

        Parser parser = new DefaultParser();

        // Typical use case
        String[] args = {"-t"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("p", "property", true, "property option");

        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("property", "value");

        // Testing with properties
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, properties);
        assertTrue(cmd.hasOption("property"));
        assertEquals("value", cmd.getOptionValue("property"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("s", "stop", false, "stop option");

        Parser parser = new DefaultParser();

        // Testing stop at non option
        String[] args = {"-s", "value1", "value2", "non-option"};
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("stop"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("value1", cmd.getArgs()[0]);
        assertEquals("value2", cmd.getArgs()[1]);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option requiredOption = new Option("r", "required", true, "required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);

        Parser parser = new DefaultParser();

        // Testing missing required option
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();

        Parser parser = new DefaultParser();

        // Testing unrecognized option
        String[] args = {"-u"};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentForOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        Parser parser = new DefaultParser();

        // Testing missing argument for option
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args);
    }
}