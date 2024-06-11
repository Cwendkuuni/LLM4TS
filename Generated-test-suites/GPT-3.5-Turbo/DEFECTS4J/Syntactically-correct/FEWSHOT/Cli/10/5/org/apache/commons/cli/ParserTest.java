import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new ParserImpl();
    }

    @Test
    public void testParseWithNoArgs() throws ParseException {
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testParseWithSingleArg() throws ParseException {
        String[] args = {"arg1"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals(1, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
    }

    @Test
    public void testParseWithMultipleArgs() throws ParseException {
        String[] args = {"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals(3, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
        assertEquals("arg3", cmd.getArgList().get(2));
    }
    
    @Test
    public void testParseWithOptions() throws ParseException {
        options.addOption("h", "help", false, "Print help message");
        options.addOption("v", "verbose", false, "Enable verbose output");
        options.addOption("o", "output", true, "Set output directory");
        
        String[] args = {"-h", "-v", "-o", "dir"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("help"));
        assertTrue(cmd.hasOption("verbose"));
        assertTrue(cmd.hasOption("output"));
        assertEquals("dir", cmd.getOptionValue("output"));
    }
    
    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        options.addOption("f", "file", true, "Input file").setRequired(true);
        options.addOption("o", "output", true, "Output directory").setRequired(true);
        
        String[] args = {"-f", "input.txt", "-o", "dir"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.hasOption("output"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
        assertEquals("dir", cmd.getOptionValue("output"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        options.addOption("f", "file", true, "Input file").setRequired(true);
        options.addOption("o", "output", true, "Output directory").setRequired(true);
        
        String[] args = {"-f", "input.txt"};
        parser.parse(options, args);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = {"-u"};
        parser.parse(options, args);
    }

}

class ParserImpl extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
    }
}