import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithNoOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, 
                            boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        String[] arguments = {"hello", "world"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, arguments);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertNotNull(cmd);
        assertEquals(2, cmd.getArgs().length);
        assertEquals("hello", cmd.getArgs()[0]);
        assertEquals("world", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithOption() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, 
                            boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option opt = new Option("f", true, "File");
        options.addOption(opt);

        String[] arguments = {"-f", "test.txt"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, arguments);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws Exception {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, 
                            boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option opt = new Option("f", true, "File");
        opt.setRequired(true);
        options.addOption(opt);

        String[] arguments = {};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, 
                            boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option opt = new Option("f", true, "File");
        opt.setRequired(true);
        options.addOption(opt);

        String[] arguments = {"-f"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, 
                            boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option opt = new Option("f", true, "File");
        options.addOption(opt);

        String[] arguments = {"-d"};
        parser.parse(options, arguments);
    }
}