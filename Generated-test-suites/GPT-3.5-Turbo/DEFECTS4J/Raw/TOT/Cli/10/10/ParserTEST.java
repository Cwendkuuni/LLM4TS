import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new ParserImpl();
    }

    @Test
    public void testParse_DefaultSettings_Success() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option A"));

        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParse_ShortOption_Success() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("optionA").hasArg().desc("Option A").build());

        String[] arguments = {"-a", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("optionA"));
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals("value", cmd.getOptionValue("optionA"));
    }

    @Test
    public void testParse_LongOption_Success() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder().longOpt("optionA").hasArg().desc("Option A").build());

        String[] arguments = {"--optionA=value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("optionA"));
        assertEquals("value", cmd.getOptionValue("optionA"));
    }

    @Test
    public void testParse_RequiredOption_Success() throws ParseException {
        Options options = new Options();
        options.addRequiredOption(Option.builder("a").longOpt("optionA").hasArg().desc("Option A").build());

        String[] arguments = {"-a", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOption_MissingOption_ExceptionThrown() throws ParseException {
        Options options = new Options();
        options.addRequiredOption(Option.builder("a").longOpt("optionA").hasArg().desc("Option A").build());

        String[] arguments = {};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_OptionMissingArgument_ExceptionThrown() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("optionA").hasArg().desc("Option A").build());

        String[] arguments = {"-a"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ExceptionThrown() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("optionA").hasArg().desc("Option A").build());

        String[] arguments = {"-b"};
        parser.parse(options, arguments);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}