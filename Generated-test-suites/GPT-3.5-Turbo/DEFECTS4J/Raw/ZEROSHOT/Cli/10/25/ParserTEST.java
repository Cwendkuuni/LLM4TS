import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            public void processArgs(Option opt, ListIterator iter) throws ParseException {
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {
            }
        };

        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};

        CommandLine result = parser.parse(options, args);

        assertNotNull(result);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOptions() throws ParseException {
        options.addOption(Option.builder("a").required().build());

        String[] args = {"-b", "2"};

        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        String[] args = {"-a", "1", "-c", "3"};

        parser.parse(options, args);
    }

    @Test
    public void testParse_stopAtNonOption() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());

        String[] args = {"-a", "1", "-b", "2", "arg1", "arg2"};

        CommandLine result = parser.parse(options, args, true);

        assertNotNull(result);
        assertEquals(2, result.getOptions().length);
        assertEquals("1", result.getOptionValue("a"));
        assertEquals("2", result.getOptionValue("b"));
        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test
    public void testParse_properties() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());

        String[] args = {"-a", "1"};

        Properties properties = new Properties();
        properties.setProperty("b", "2");

        CommandLine result = parser.parse(options, args, properties);

        assertNotNull(result);
        assertEquals(2, result.getOptions().length);
        assertEquals("1", result.getOptionValue("a"));
        assertEquals("2", result.getOptionValue("b"));
    }

    @Test
    public void testProcessProperties() {
        Option optionA = Option.builder("a").hasArg().build();
        Option optionB = Option.builder("b").hasArg().build();

        options.addOption(optionA);
        options.addOption(optionB);

        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");
        properties.setProperty("c", "false");

        parser.processProperties(properties);

        assertTrue(optionA.hasArg());
        assertEquals("1", optionA.getValue());
        assertTrue(optionB.hasArg());
        assertEquals("2", optionB.getValue());
        assertFalse(options.hasOption("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingArgument() throws ParseException {
        Option option = Option.builder("a").required().hasArg().build();
        options.addOption(option);

        String[] args = {"-a"};

        parser.parse(options, args);
    }
}