import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("t", true, "Test option");
        String[] arguments = {"-t", "value"};
        Properties properties = new Properties();
        properties.setProperty("t", "value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("t"));
        assertEquals("value", cmd.getOptionValue("t"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option requiredOption = new Option("r", true, "Required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        String[] arguments = {};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", true, "Option with argument");
        options.addOption(option);
        String[] arguments = {"-a"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-x"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }

}