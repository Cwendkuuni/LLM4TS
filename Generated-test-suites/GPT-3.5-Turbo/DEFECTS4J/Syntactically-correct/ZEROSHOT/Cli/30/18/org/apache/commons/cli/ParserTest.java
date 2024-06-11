import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "Input file");
        options.addOption(option);

        String[] arguments = {"-f", "input.txt"};

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "Input file");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("file", "input.txt");

        String[] arguments = {};

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "Input file");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "file", true, "Input file");
        options.addOption(option);

        String[] arguments = {"-x"};

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("f", "file", true, "Input file");

        ListIterator iter = Arrays.asList("input.txt").listIterator();

        parser.processArgs(option, iter);

        assertTrue(option.hasArg());
        assertNotNull(option.getValue());
        assertEquals("input.txt", option.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("f", "file", true, "Input file");

        ListIterator iter = Arrays.asList("input.txt").listIterator();

        parser.processOption("f", iter);

        assertTrue(option.hasArg());
        assertNotNull(option.getValue());
        assertEquals("input.txt", option.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        ListIterator iter = Arrays.asList("input.txt").listIterator();

        parser.processOption("x", iter);
    }
}