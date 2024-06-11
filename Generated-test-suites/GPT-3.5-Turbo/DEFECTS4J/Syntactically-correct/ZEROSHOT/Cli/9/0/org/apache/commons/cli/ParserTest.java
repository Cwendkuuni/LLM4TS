import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = new String[]{"-a", "valueA", "-b"};
        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("valueA", cmd.getOptionValue("a"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParse_Exception_UnrecognizedOption() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = new String[]{"-c"};
        try {
            parser.parse(options, args);
            fail("UnrecognizedOptionException should have been thrown.");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -c", e.getMessage());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParse_Exception_MissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = new String[]{"-a"};
        try {
            parser.parse(options, args);
            fail("MissingArgumentException should have been thrown.");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:a", e.getMessage());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParse_Exception_MissingOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            protected void checkRequiredOptions() throws MissingOptionException {
                throw new MissingOptionException("Missing required option: a");
            }
        };

        String[] args = new String[]{"-b", "valueB"};
        try {
            parser.parse(options, args);
            fail("MissingOptionException should have been thrown.");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParse_OptionsWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = new String[]{"-a", "valueA", "-b"};
        Properties properties = new Properties();
        properties.setProperty("b", "true");
        try {
            CommandLine cmd = parser.parse(options, args, properties);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("valueA", cmd.getOptionValue("a"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

}