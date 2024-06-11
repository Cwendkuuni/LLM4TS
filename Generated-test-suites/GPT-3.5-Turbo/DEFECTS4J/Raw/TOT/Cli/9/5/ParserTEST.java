import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        options = new Options();
    }

    @Test
    public void testParse_NoOptions_NoArguments() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{});
        assertTrue(cmd.getOptions().length == 0);
        assertTrue(cmd.getArgs().length == 0);
    }

    @Test
    public void testParse_WithOptionWithValue() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        CommandLine cmd = parser.parse(options, new String[]{"-f", "file.txt"});
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_WithRequiredOptionMissingValue() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);
        CommandLine cmd = parser.parse(options, new String[]{"-f"});
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        CommandLine cmd = parser.parse(options, new String[]{"-f", "file.txt", "arg1", "--", "arg2"});
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.getArgs().length == 3);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("--", cmd.getArgs()[1]);
        assertEquals("arg2", cmd.getArgs()[2]);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");
        parser.processProperties(properties);
        assertTrue(parser.getCommandLine().hasOption("f"));
        assertEquals("file.txt", parser.getCommandLine().getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_MissingRequiredOptions() throws MissingOptionException {
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);
        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptions_RequiredOptionsPresent() throws MissingOptionException {
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);
        CommandLine cmd = parser.parse(options, new String[]{"-f", "file.txt"});
        parser.checkRequiredOptions();
    }

}