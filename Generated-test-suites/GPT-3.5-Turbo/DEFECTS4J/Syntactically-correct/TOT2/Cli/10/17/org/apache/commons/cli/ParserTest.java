import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A description");
        options.addOption("b", "optionB", true, "Option B description");

        String[] args = {"-a", "-b", "valueB"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("x", "optionX", true, "Option X description");

        Properties properties = new Properties();
        properties.setProperty("x", "valueX");

        String[] args = {};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("x"));
        assertEquals("valueX", cmd.getOptionValue("x"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);

        String[] args = {};

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("c", "optionC", false, "Option C description");

        String[] args = {"-c", "valueC", "nonOption"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("c"));
        assertEquals("valueC", cmd.getOptionValue("c"));
        assertEquals("nonOption", cmd.getArgList().get(0));
    }
}