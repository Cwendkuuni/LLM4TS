import org.junit.Test;
import org.apache.commons.cli.MissingOptionException;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new MyParser();

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-b"};

        Parser parser = new MyParser();

        parser.parse(options, arguments);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addRequiredOption("c", true, "Required option c");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new MyParser();

        parser.parse(options, arguments);
    }

}