import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ConcreteParser();
        options = new Options();
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);
    }

    // Test for parse(Options, String[], boolean)
    @Test
    public void testParse_SingleOption_WithRequiredOption() throws ParseException {
        String[] args = {"-f", "test.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParse_SingleOption_WithoutRequiredOption() throws ParseException {
        String[] args = {"-f"};
        
        try {
            parser.parse(options, args, false);
        } catch (MissingOptionException e) {
            assertEquals("Missing required options: f", e.getMessage());
        }
    }

    // Test for parse(Options, String[], boolean, Properties)
    @Test
    public void testParse_WithProperties() throws ParseException {
        String[] args = {"-f"};
        Properties properties = new Properties();
        properties.setProperty("f", "test.txt");

        CommandLine cmd = parser.parse(options, args, true, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }

    // Test for processArgs(Option, ListIterator)
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        ListIterator<String> iterator = Arrays.asList("test.txt").listIterator();

        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals("test.txt", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingArgument() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        ListIterator<String> iterator = Collections.emptyList().listIterator();

        parser.processArgs(option, iterator);
    }

    // Test for processOption(String, ListIterator)
    @Test
    public void testProcessOption_ValidOption() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        ListIterator<String> iterator = Arrays.asList("test.txt").listIterator();

        parser.processOption("-f", iterator);

        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_InvalidOption() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        ListIterator<String> iterator = Collections.emptyList().listIterator();

        parser.processOption("-x", iterator);
    }

    // Test for checkRequiredOptions()
    @Test
    public void testCheckRequiredOptions_AllRequiredOptionsPresent() throws ParseException {
        String[] args = {"-f", "test.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        parser.checkRequiredOptions();// No exception is thrown
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_RequiredOptionsMissing() throws ParseException {
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, false);

        parser.checkRequiredOptions();
    }

    // Test for updateRequiredOptions(Option)
    @Test
    public void testUpdateRequiredOptions_OptionIsNotRequired() throws ParseException {
        Option option = new Option("f", "file", true, "input file");

        parser.updateRequiredOptions(option);

        assertFalse(parser.getRequiredOptions().contains(option.getKey()));
    }

    @Test
    public void testUpdateRequiredOptions_OptionIsRequired() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);

        parser.updateRequiredOptions(option);

        assertFalse(parser.getRequiredOptions().contains(option.getKey()));
    }

    @Test
    public void testUpdateRequiredOptions_OptionIsPartOfGroup() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        options.addOptionGroup(group);

        parser.updateRequiredOptions(option);

        assertFalse(parser.getRequiredOptions().contains(group));
    }

    // ConcreteParser class for testing
    private static class ConcreteParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }

}