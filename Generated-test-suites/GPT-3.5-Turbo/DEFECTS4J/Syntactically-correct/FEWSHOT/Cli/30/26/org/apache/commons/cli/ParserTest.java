import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new ParserImpl();
        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-f", "file.txt", "--verbose", "--output", "output.txt", "-a", "arg1", "arg2"};
        Option optionF = new Option("f", "file", true, "input file");
        Option optionV = new Option("v", "verbose", false, "enable verbose output");
        Option optionO = new Option("o", "output", true, "output file");
        Option optionA = new Option("a", true, "argument");

        options.addOption(optionF);
        options.addOption(optionV);
        options.addOption(optionO);
        options.addOption(optionA);

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("a"));

        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals("output.txt", cmd.getOptionValue("o"));

        String[] remainingArgs = cmd.getArgs();
        assertEquals(2, remainingArgs.length);
        assertEquals("arg1", remainingArgs[0]);
        assertEquals("arg2", remainingArgs[1]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--file", "file.txt", "--output", "output.txt"};
        Option optionF = new Option("f", "file", true, "input file");
        Option optionV = new Option("v", "verbose", false, "enable verbose output");
        Option optionO = new Option("o", "output", true, "output file");

        options.addOption(optionF);
        options.addOption(optionV);
        options.addOption(optionO);

        Properties properties = new Properties();
        properties.setProperty("verbose", "true");

        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("v"));

        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertNull(cmd.getOptionValue("v"));
    }

    @Test(expected = MissingOptionException.class)
    public void testRequiredOptions() throws ParseException {
        String[] args = {"-f", "file.txt"};
        Option optionF = new Option("f", "file", true, "input file");
        Option optionV = new Option("v", "verbose", false, "enable verbose output");
        Option optionO = new Option("o", "output", true, "output file");

        options.addOption(optionF);
        options.addOption(optionV);
        options.addOption(optionO);

        parser.parse(options, args);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}