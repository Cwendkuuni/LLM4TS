import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }

    @Test
    public void testParse_OptionsArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test
    public void testParse_OptionsArgumentsProperties() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};
        Properties properties = new Properties();
        CommandLine result = parser.parse(options, arguments, properties);
        assertNotNull(result);
    }

    @Test
    public void testParse_OptionsArgumentsStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};
        boolean stopAtNonOption = true;
        CommandLine result = parser.parse(options, arguments, stopAtNonOption);
        assertNotNull(result);
    }

    @Test
    public void testParse_OptionsArgumentsPropertiesStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};
        Properties properties = new Properties();
        boolean stopAtNonOption = true;
        CommandLine result = parser.parse(options, arguments, properties, stopAtNonOption);
        assertNotNull(result);
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        List requiredOptions = parser.getRequiredOptions();
        assertNotNull(requiredOptions);
    }

    @Test
    public void testCheckRequiredOptions() {
        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            // success
        }
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = new Option("a", "option");
        ListIterator iter = Arrays.asList("value1", "value2").listIterator();
        parser.processArgs(opt, iter);
        assertTrue(opt.hasValues());
        assertEquals("value1", opt.getValue(0));
        assertEquals("value2", opt.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingArgument() throws ParseException {
        Option opt = new Option("a", "option", true, "option with argument");
        ListIterator iter = Collections.emptyList().listIterator();
        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option");
        options.addOption(option);
        ListIterator iterator = Arrays.asList("-a").listIterator();
        parser.processOption("-a", iterator);
        assertTrue(parser.getCommandline().hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option");
        options.addOption(option);
        ListIterator iterator = Collections.emptyList().listIterator();
        parser.processOption("-b", iterator);
    }
}