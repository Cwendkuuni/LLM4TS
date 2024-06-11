import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        options = new Options();
        Option opt = new Option("h", "help", false, "print help");
        options.addOption(opt);

        arguments = new String[0];
    }

    @Test
    public void testParse_NoArguments_ReturnsCommandLine() throws ParseException {
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test
    public void testParse_Arguments_ReturnsCommandLine() throws ParseException {
        arguments = new String[]{"-h"};
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionMissing_ThrowsMissingOptionException() throws ParseException {
        Option opt = new Option("r", "required", true, "required option");
        opt.setRequired(true);
        options.addOption(opt);
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_ValidArgumentsAndProperties_ReturnsCommandLine() throws ParseException {
        Option opt = new Option("p", "property", true, "property option");
        opt.setArgs(2);
        options.addOption(opt);

        Properties properties = new Properties();
        properties.setProperty("property", "value1");
        properties.setProperty("property", "value2");

        CommandLine result = parser.parse(options, arguments, properties);
        assertNotNull(result);
    }

    @Test
    public void testProcessArgs_ValidOptionAndArguments_AddsValuesToOption() throws ParseException {
        Option opt = new Option("a", "arg", true, "argument option");
        ListIterator iter = Arrays.asList(new String[]{"value1", "value2"}).listIterator();

        parser.processArgs(opt, iter);

        String[] values = opt.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingValueForRequiredOption_ThrowsMissingArgumentException() throws ParseException {
        Option opt = new Option("a", "arg", true, "argument option");
        ListIterator iter = Arrays.asList(new String[0]).listIterator();

        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption_ValidOption_AddsOptionToCommandLine() throws ParseException {
        Option opt = new Option("t", "test", true, "test option");
        ListIterator iter = Arrays.asList(new String[]{"value"}).listIterator();

        parser.processOption("t", iter);

        CommandLine result = parser.parse(options, arguments);
        assertTrue(result.hasOption("t"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        ListIterator iter = Arrays.asList(new String[0]).listIterator();

        parser.processOption("x", iter);
    }
}