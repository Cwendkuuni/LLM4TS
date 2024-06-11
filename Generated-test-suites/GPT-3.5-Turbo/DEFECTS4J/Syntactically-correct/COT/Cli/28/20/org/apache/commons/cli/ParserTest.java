
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "Flag option");
        options.addOption("d", true, "Option with argument");
        
        String[] args = {"-f", "-d", "value"};
        
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);
        
        assert(cmd.hasOption("f"));
        assert(cmd.getOptionValue("d").equals("value"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "Flag option");
        
        String[] args = {"-g"};
        
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArg() throws ParseException {
        Options options = new Options();
        options.addOption("d", true, "Option with argument");
        
        String[] args = {"-d"};
        
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);
    }
    
    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}
