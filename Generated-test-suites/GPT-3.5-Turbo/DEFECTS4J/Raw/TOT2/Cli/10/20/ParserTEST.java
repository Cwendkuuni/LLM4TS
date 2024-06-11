import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "valueB"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("p", "property", true, "Property");

        Parser parser = new GnuParser();
        String[] args = {"-p", "value"};
        Properties properties = new Properties();
        properties.setProperty("prop", "value");
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("p"));
        assertEquals("value", cmd.getOptionValue("p"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("r").longOpt("required").required().build());

        Parser parser = new PosixParser();
        String[] args = {};
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        Parser parser = new BasicParser();
        String[] args = {"-a"};
        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("v", "verbose", false, "Verbose");

        Parser parser = new DefaultParser();
        String[] args = {"-v", "-x"};
        parser.parse(options, args);
    }

    @Test
    public void testMixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "Option");
        options.addOption("a", "argument", false, "Argument");

        Parser parser = new GnuParser();
        String[] args = {"-o", "value", "-a"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertTrue(cmd.hasOption("a"));
    }
}