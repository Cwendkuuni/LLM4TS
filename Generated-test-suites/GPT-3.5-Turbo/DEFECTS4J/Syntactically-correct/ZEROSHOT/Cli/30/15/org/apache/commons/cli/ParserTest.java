import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                
            }

            @Override
            public void processArgs(Option opt, ListIterator iter) throws ParseException {
                
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {
                
            }
        };
    }
    
    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-a", "value1", "-b value2", "--option", "value3", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("option", true, "option");
        
        CommandLine cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("option"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("b"));
        assertEquals("value3", cmd.getOptionValue("option"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }
}