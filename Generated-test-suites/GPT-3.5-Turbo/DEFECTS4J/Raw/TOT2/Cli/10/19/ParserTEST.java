import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithNoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "value"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option a");

        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        parser.parse(options, args);
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("a", true, "option a");

        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option requiredOption = Option.builder("a").required().build();
        options.addOption(requiredOption);

        Parser parser = new DefaultParser();
        String[] args = {};
        parser.parse(options, args);
    }

}