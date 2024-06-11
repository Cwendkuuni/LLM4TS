import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }

    @After
    public void tearDown() {
        options = null;
        parser = null;
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose output");
        options.addOption(option);
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose output");
        options.addOption(option);
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "enable verbose output");
        option.setRequired(true);
        options.addOption(option);
        parser.setOptions(options);
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("verbose", "true");
        options.addOption("v", "verbose", false, "enable verbose output");
        parser.setOptions(options);
        try {
            parser.parse(options, new String[0], properties);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(parser.getCommandLine().hasOption("v"));
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] args = {"-f", "test.txt"};
        parser.setOptions(options);
        try {
            parser.processArgs(option, Arrays.asList(args).listIterator());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("test.txt", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] args = {"-f"};
        parser.setOptions(options);
        parser.processArgs(option, Arrays.asList(args).listIterator());
    }

    @Test
    public void testProcessOption() {
        Option option = new Option("v", "verbose", false, "enable verbose output");
        options.addOption(option);
        String arg = "-v";
        parser.setOptions(options);
        try {
            parser.processOption(arg, Arrays.asList(arg).listIterator());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(parser.getCommandLine().hasOption("v"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        String arg = "-x";
        parser.setOptions(options);
        parser.processOption(arg, Arrays.asList(arg).listIterator());
    }

    @Test
    public void testParse() {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] args = {"-f", "test.txt"};
        parser.setOptions(options);
        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("f"));
            assertEquals("test.txt", cmd.getOptionValue("f"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissing() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

}