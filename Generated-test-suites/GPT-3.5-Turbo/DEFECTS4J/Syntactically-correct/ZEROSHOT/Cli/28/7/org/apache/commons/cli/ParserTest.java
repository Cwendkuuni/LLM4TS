import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };

        options = new Options();
        option1 = new Option("a", "option1", false, "Description for option 1");
        option2 = new Option("b", "option2", false, "Description for option 2");
        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        List requiredOptions = parser.getRequiredOptions();
        assertTrue(requiredOptions.contains(option1));
        assertTrue(requiredOptions.contains(option2));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        ListIterator iterator = Arrays.asList(new String[]{"value1", "value2"}).listIterator();
        parser.processArgs(option1, iterator);

        String[] values = option1.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        ListIterator iterator = Arrays.asList(new String[]{}).listIterator();
        parser.processArgs(option1, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        ListIterator iterator = Arrays.asList(new String[]{}).listIterator();
        parser.processOption("-a", iterator);

        assertTrue(parser.getRequiredOptions().isEmpty());
        assertTrue(parser.getOptions().getOptionGroup(option1).isSelected());
        assertTrue(parser.parse(options, new String[]{"-b"}).hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        ListIterator iterator = Arrays.asList(new String[]{}).listIterator();
        parser.processOption("-c", iterator);
    }
}