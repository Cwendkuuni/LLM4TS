import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                // Implementation not needed for testing
                return null;
            }
        };
    }

    @Test
    public void testParseWithNullOptionsAndArguments() throws ParseException {
        assertNull(parser.parse(null, null));
    }

    @Test
    public void testParseWithNullProperties() throws ParseException {
        assertNull(parser.parse(new Options(), null, null));
    }

    @Test
    public void testProcessPropertiesWithNullProperties() throws ParseException {
        parser.processProperties(null);
    }

    @Test
    public void testCheckRequiredOptionsWithEmptyRequiredOptions() throws MissingOptionException {
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithNonEmptyRequiredOptions() throws MissingOptionException {
        parser.getRequiredOptions().add("option1");
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgsWithNullOption() throws ParseException {
        parser.processArgs(null, null);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithNoArguments() throws ParseException {
        Option option = new Option("o", true, "option with argument");
        parser.processArgs(option, null);
    }

    @Test
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        ListIterator<String> iterator = Arrays.asList("-xyz").listIterator();
        try {
            parser.processOption("-xyz", iterator);
            assertTrue(false);
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -xyz", e.getMessage());
        }
    }

    @Test
    public void testProcessOptionWithOptionRequiresArgument() throws ParseException {
        ListIterator<String> iterator = Arrays.asList("-opt").listIterator();
        Option option = Option.builder("opt").argName("arg").hasArg().build();
        try {
            parser.processOption("-opt", iterator);
            assertTrue(false);
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: -opt", e.getMessage());
        }
    }
}