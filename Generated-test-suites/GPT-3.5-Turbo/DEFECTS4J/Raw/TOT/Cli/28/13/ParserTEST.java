import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        options = new Options();
    }
    
    @Test
    public void testParse_withOptionsAndArguments_returnCommandLine() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2"};

        Option optionA = new Option("a", "option A");
        Option optionB = new Option("b", "option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParse_withRequiredOptionMissing_throwMissingOptionException() throws ParseException {
        String[] arguments = {"-a", "value1"};

        Option optionA = new Option("a", "option A");
        Option optionB= Option.builder("b").required().build();
        options.addOption(optionA);
        options.addOption(optionB);

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_withStopAtNonOption_returnCommandLineWithRemainingArguments() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2", "remaining"};

        Option optionA = new Option("a", "option A");
        Option optionB = new Option("b", "option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine result = parser.parse(options, arguments, true);

        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
        assertEquals("remaining", result.getArgs()[0]);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_withUnrecognizedOption_throwUnrecognizedOptionException() throws ParseException {
        String[] arguments = {"-a", "value1", "-c", "value2"};

        Option optionA = new Option("a", "option A");
        options.addOption(optionA);

        parser.parse(options, arguments);
    }
}