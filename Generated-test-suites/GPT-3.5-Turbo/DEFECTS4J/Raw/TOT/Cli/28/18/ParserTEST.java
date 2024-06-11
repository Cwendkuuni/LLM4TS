
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);

        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
        assertTrue(parser.getRequiredOptions().contains(option.getOpt()));
    }

    @Test
    public void testParse() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        Options options = new Options();
        options.addOption(Option.builder("a")
                .longOpt("optionA")
                .hasArg()
                .desc("Option A")
                .required()
                .build());

        String[] args = {"-a", "value"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionMissing() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        Options options = new Options();
        options.addOption(Option.builder("a")
                .longOpt("optionA")
                .hasArg()
                .desc("Option A")
                .required()
                .build());

        String[] args = {};

        parser.parse(options, args);
    }
}

