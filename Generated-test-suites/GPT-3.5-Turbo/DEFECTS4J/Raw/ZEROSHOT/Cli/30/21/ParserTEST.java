import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }

    @Test
    public void testSetOptions() {
        options.addOption(new Option("h", "help", false, "Print help"));
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        options.addOption(new Option("h", "help", false, "Print help"));
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("f", "file", true, "File path");
        option.setRequired(true);
        options.addOption(option);
        
        parser.setOptions(options);
        
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() throws ParseException {
        options.addOption(new Option("h", "help", false, "Print help"));
        String[] args = {"-h"};
        
        CommandLine commandLine = parser.parse(options, args);
        
        assertTrue(commandLine.hasOption("h"));
        assertFalse(commandLine.hasOption("help"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOptions() throws ParseException {
        Option option = new Option("f", "file", true, "File path");
        option.setRequired(true);
        options.addOption(option);
        
        String[] args = {};
        
        CommandLine commandLine = parser.parse(options, args);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("f", "file", true, "File path");
        parser.processArgs(option, new java.util.ArrayList<>().listIterator());
        
        assertEquals(0, option.getValues().length);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingValue() throws ParseException {
        Option option = new Option("f", "file", true, "File path");
        option.setArgName("path");
        parser.processArgs(option, new java.util.ArrayList<>().listIterator());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        String arg = "-x";
        parser.processOption(arg, new java.util.ArrayList<>().listIterator());
    }
}