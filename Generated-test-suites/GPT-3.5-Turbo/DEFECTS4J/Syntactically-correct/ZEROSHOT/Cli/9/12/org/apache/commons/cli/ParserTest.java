import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.Parser;

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
                return new String[0];
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);
        String[] args = new String[]{"-t"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("t"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_InvalidOption() throws ParseException {
        String[] args = new String[]{"-r"};

        parser.parse(options, args);

        fail("Should have thrown an UnrecognizedOptionException");
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);
        String[] args = new String[]{"-t", "value", "arg1", "arg2"};

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("t"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Option requiredOption = new Option("r", "required", true, "Required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        String[] args = new String[]{};

        parser.parse(options, args);

        fail("Should have thrown a MissingOptionException");
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);
        String arg = "-t";

        parser.processOption(arg, null);

        assertTrue(parser.getOptions().hasOption("t"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_InvalidOption() throws ParseException {
        String arg = "-r";

        parser.processOption(arg, null);

        fail("Should have thrown an UnrecognizedOptionException");
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("t", "test", true, "Test option");
        options.addOption(option);
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_NoValue() throws ParseException {
        Option option = new Option("t", "test", true, "Test option");
        options.addOption(option);
        ListIterator iterator = Arrays.asList().listIterator();

        parser.processArgs(option, iterator);

        fail("Should have thrown a MissingArgumentException");
    }

    @Test
    public void testProcessProperties() {
        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("r", "required", true, "Required option");
        options.addOption(option1);
        options.addOption(option2);
        Properties properties = new Properties();
        properties.setProperty("t", "");
        properties.setProperty("r", "value");

        parser.processProperties(properties);

        assertTrue(parser.getCommandLine().hasOption("t"));
        assertTrue(parser.getCommandLine().hasOption("r"));
        assertEquals(0, option1.getValues().length);
        assertEquals("value", option2.getValue());
    }

    @Test
    public void testProcessProperties_SkipOption() {
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("t", "true");

        parser.processProperties(properties);

        assertFalse(parser.getCommandLine().hasOption("t"));
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();

        parser.setOptions(options);

        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        parser.setOptions(options);

        Options result = parser.getOptions();

        assertSame(options, result);
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);
        parser.setOptions(options);

        List result = parser.getRequiredOptions();

        assertEquals(1, result.size());
        assertEquals(option, result.get(0));
    }
}