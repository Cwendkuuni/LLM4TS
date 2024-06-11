import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {
                // Do nothing
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        String[] arguments = {"-i", "input.txt", "-o", "output.txt"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals("input.txt", commandLine.getOptionValue("input"));
        assertEquals("output.txt", commandLine.getOptionValue("output"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingOption() throws ParseException {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        String[] arguments = {"-i", "input.txt"};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        String[] arguments = {"-i", "input.txt", "-f", "output.txt"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_missingArgument() throws ParseException {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        String[] arguments = {"-i", "-o", "output.txt"};

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");
        properties.setProperty("output", "output.txt");

        parser.processProperties(properties);

        assertEquals("input.txt", parser.cmd.getOptionValue("input"));
        assertEquals("output.txt", parser.cmd.getOptionValue("output"));
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        parser.setOptions(options);

        parser.checkRequiredOptions();

        assertTrue(parser.getRequiredOptions().contains("input"));
        assertTrue(parser.getRequiredOptions().contains("output"));

        parser.getOptions().getOption("input").addValueForProcessing("input.txt");
        parser.getOptions().getOption("output").addValueForProcessing("output.txt");

        parser.checkRequiredOptions();

        assertFalse(parser.getRequiredOptions().contains("input"));
        assertFalse(parser.getRequiredOptions().contains("output"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_missingOption() {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        parser.setOptions(options);

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option input = new Option("i", "input", true, "input file");

        ListIterator iterator = Arrays.asList("input.txt").listIterator();

        parser.processArgs(input, iterator);

        assertEquals("input.txt", input.getValue());
    }
}