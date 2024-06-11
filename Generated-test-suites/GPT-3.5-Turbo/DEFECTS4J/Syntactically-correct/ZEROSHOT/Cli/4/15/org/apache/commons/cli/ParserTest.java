import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message");
        options.addOption("v", "version", false, "Print version information");

        String[] args = new String[]{"-h"};
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertNull(cmd.getOptionValue("h"));
        assertNull(cmd.getOptionValue("v"));
    }
    
    @Test
    public void testParseArguments() {
        Options options = new Options();
        options.addOption("f", "file", true, "Input file");

        String[] args = new String[]{"-f", "input.txt"};
        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }
    
    @Test
    public void testParseRequiredOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "Input file");
        options.addOption("o", "output", true, "Output file");
        options.addRequiredOption("f");

        String[] args = new String[]{"-o", "output.txt"};
        Parser parser = new ParserImpl();
        
        try {
            parser.parse(options, args);
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: f", e.getMessage());
        }
    }

    private class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return null;
        }
    }
}