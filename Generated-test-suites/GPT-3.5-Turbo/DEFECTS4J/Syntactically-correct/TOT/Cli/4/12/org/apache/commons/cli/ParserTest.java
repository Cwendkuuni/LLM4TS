import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption(Option.builder("a")
                .longOpt("optionA")
                .desc("Option A")
                .hasArg()
                .argName("value")
                .build());
        options.addOption(Option.builder("b")
                .longOpt("optionB")
                .desc("Option B")
                .hasArg()
                .argName("value")
                .required()
                .build());

        parser = new DefaultParser();
    }

    @Test
    public void testValidArguments() throws ParseException {
        String[] args = {"-a", "valueA", "-b", "valueB"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue("Option -a should be present", cmd.hasOption("a"));
        assertTrue("Option -b should be present", cmd.hasOption("b"));
        assertEquals("Value of option -a should be \"valueA\"", "valueA", cmd.getOptionValue("a"));
        assertEquals("Value of option -b should be \"valueB\"", "valueB", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredArgument() throws ParseException {
        String[] args = {"-a", "valueA"};

        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"-c", "valueC"};

        parser.parse(options, args);
    }

    @Test
    public void testEmptyArguments() throws ParseException {
        String[] args = {};

        CommandLine cmd = parser.parse(options, args);

        assertFalse("Option -a should not be present", cmd.hasOption("a"));
        assertFalse("Option -b should not be present", cmd.hasOption("b"));
    }

    @Test
    public void testDoubleDash() throws ParseException {
        String[] args = {"-a", "valueA", "--", "-b"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue("Option -a should be present", cmd.hasOption("a"));
        assertFalse("Option -b should not be present", cmd.hasOption("b"));
        assertEquals("Value of option -a should be \"valueA\"", "valueA", cmd.getOptionValue("a"));
    }

    @Test
    public void testSingleDash() throws ParseException {
        String[] args = {"-a", "valueA", "-", "-b"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue("Option -a should be present", cmd.hasOption("a"));
        assertTrue("Option - should be present", cmd.hasOption("-"));
        assertTrue("Option -b should be present", cmd.hasOption("b"));
        assertEquals("Value of option -a should be \"valueA\"", "valueA", cmd.getOptionValue("a"));
    }

}