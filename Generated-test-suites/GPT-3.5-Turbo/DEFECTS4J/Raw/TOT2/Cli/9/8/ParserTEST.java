import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        // Create Parser object
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());

        List requiredOptions = options.getRequiredOptions();
        assertEquals(requiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testParse() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        Option option = new Option("i", "input", false, "input file");
        options.addOption(option);

        String[] arguments = {"-i", "input.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("i"));
        assertEquals("input.txt", cmd.getOptionValue("i"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        Option option = Option.builder("f").required().build();
        options.addOption(option);

        String[] arguments = {};
        parser.parse(options, arguments);
    }

}