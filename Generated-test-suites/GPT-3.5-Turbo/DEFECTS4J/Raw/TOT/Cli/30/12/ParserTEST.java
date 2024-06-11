import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser() {

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                // dummy implementation for testing purposes
                return new String[0];
            }
        };
    }
    
    @Test
    public void testParse_withNoArguments_shouldReturnEmptyCommandLine() throws ParseException {
        Options options = new Options();
        String[] arguments = {};

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParse_withOptionArgument_shouldSetOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        String[] arguments = {"-f", "file.txt"};

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParse_withMissingRequiredOption_shouldThrowMissingOptionException() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("r");
        String[] arguments = {};

        parser.parse(options, arguments);
    }
    
    @Test
    public void testParse_withOptionalOption_shouldNotThrowException() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o").hasArg().build());
        String[] arguments = {};

        CommandLine cmd = parser.parse(options, arguments);

        assertFalse(cmd.hasOption("o"));
    }
    
    @Test
    public void testParse_withStopAtNonOption_shouldStopParsingAtNonOptionArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a");
        String[] arguments = {"-a", "value", "nonOption"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals(1, cmd.getArgs().length);
        assertEquals("nonOption", cmd.getArgs()[0]);
    }
    
    @Test
    public void testParse_withProperties_shouldSetOptionValuesFromProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        options.addOption(Option.builder("o").build());
        String[] arguments = {};
        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");
        properties.setProperty("o", "true");

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("o"));
    }

}