import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class ParserTest {

    private Parser parser = new Parser() {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    };

    // Test parse() method with options and arguments
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        String[] arguments = {"-a", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    // Test parse() method with options and properties
    @Test
    public void testParseWithOptionsAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        CommandLine cmd = parser.parse(options, null, properties);
        assertTrue(cmd.hasOption("a"));
    }

    // Test parse() method with stopAtNonOption=true
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        String[] arguments = {"-a", "arg1", "nonOption"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("nonOption", cmd.getArgs()[1]);
    }

    // Test parse() method with required options
    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        String[] arguments = {};
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: -a", e.getMessage());
        }
    }

    // Test processArgs() method with option and arguments
    @Test
    public void testProcessArgs() throws ParseException {
        ListIterator iter = Arrays.asList("arg1", "arg2").listIterator();
        Option opt = new Option("a", true, "option a");
        parser.processArgs(opt, iter);
        assertEquals("arg1", opt.getValues()[0]);
        assertEquals("arg2", opt.getValues()[1]);
    }

    // Test processOption() method with option
    @Test
    public void testProcessOption() throws ParseException {
        ListIterator iter = Arrays.asList("arg1", "arg2").listIterator();
        Options options = new Options();
        options.addOption("a", true, "option a");
        Option opt = options.getOption("a");
        parser.processOption("a", iter);
        assertTrue(opt.hasArg());
        assertEquals("arg1", opt.getValues()[0]);
        assertEquals("arg2", opt.getValues()[1]);
    }
}