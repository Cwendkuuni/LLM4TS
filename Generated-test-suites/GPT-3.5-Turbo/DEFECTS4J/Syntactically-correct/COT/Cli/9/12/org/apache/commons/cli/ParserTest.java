import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        
        options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[] {"-a"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = new Option("b", true, "option b");
        ListIterator iter = Arrays.asList("value").listIterator();
        parser.processArgs(opt, iter);
        assertTrue(opt.hasArg());
        assertEquals("value", opt.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArg() throws ParseException {
        Option opt = new Option("b", true, "option b");
        ListIterator iter = Arrays.asList().listIterator();
        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option opt = new Option("a", false, "option a");
        ListIterator iter = Arrays.asList().listIterator();
        parser.processOption("a", iter);
        assertTrue(opt.hasArg());
        assertFalse(iter.hasNext());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        parser.processOption("c", Arrays.asList().listIterator());
    }
}