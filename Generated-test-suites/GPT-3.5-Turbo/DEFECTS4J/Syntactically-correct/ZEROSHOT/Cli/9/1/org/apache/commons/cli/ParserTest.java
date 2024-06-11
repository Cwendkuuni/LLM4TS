import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        parser.setOptions(options);
        options.addOption("r", true, "required option");
        List requiredOptions = parser.getRequiredOptions();
        assertNotNull(requiredOptions);
        assertEquals(1, requiredOptions.size());
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        parser.setOptions(options);
        options.addOption("r", true, "required option");
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("f", "value");
        properties.setProperty("o", "true");
        options.addOption("f", true, "option with value");
        options.addOption("o", false, "option without value");
        parser.setOptions(options);
        parser.processProperties(properties);
        CommandLine cmd = parser.parse(options, new String[]{});
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("f"));
    }

    @Test
    public void testParse() {
        options.addOption("f", true, "option with value");
        options.addOption("o", false, "option without value");
        parser.setOptions(options);
        try {
            CommandLine cmd = parser.parse(options, new String[]{"-f", "value", "-o"});
            assertTrue(cmd.hasOption("f"));
            assertTrue(cmd.hasOption("o"));
            assertEquals("value", cmd.getOptionValue("f"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        options.addOption("f", true, "option with value");
        parser.setOptions(options);
        parser.parse(options, new String[]{"-f"});
    }

    @Test
    public void testProcessOption() {
        options.addOption("f", true, "option with value");
        options.addOption("o", false, "option without value");
        parser.setOptions(options);
        try {
            parser.processOption("f", null);
            CommandLine cmd = parser.parse(options, new String[]{"-f", "value"});
            assertTrue(cmd.hasOption("f"));
            assertEquals("value", cmd.getOptionValue("f"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        options.addOption("f", true, "option with value");
        parser.setOptions(options);
        parser.processOption("o", null);
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("f", true, "option with value");
        ListIterator iterator = Arrays.asList(new String[]{"value1", "value2"}).listIterator();
        try {
            parser.processArgs(option, iterator);
            assertEquals("value1", option.getValue());
            assertTrue(iterator.hasNext());
            assertEquals("value2", iterator.next());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("f", true, "option with value");
        ListIterator iterator = Arrays.asList(new String[]{}).listIterator();
        parser.processArgs(option, iterator);
    }
}