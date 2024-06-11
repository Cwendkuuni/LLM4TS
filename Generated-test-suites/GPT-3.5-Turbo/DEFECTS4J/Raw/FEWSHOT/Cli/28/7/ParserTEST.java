import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new ParserImpl();
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("d", "directory", false, "Directory option");
        options.addOption("h", "help", false, "Help option");

        String[] args = {"-f", "test.txt", "-d", "-h"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.hasOption("directory"));
        assertTrue(cmd.hasOption("help"));

        assertEquals("test.txt", cmd.getOptionValue("file"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");

        String[] args = {"-f"};
        parser.parse(options, args);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}