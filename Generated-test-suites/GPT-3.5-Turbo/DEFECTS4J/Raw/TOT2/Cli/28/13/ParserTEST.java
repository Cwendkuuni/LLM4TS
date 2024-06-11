import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new ConcreteParser();
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
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1"};
        assertArrayEquals(arguments, parser.flatten(options, arguments, false));
    }

    @Test
    public void testParse() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("opt", "value");
        parser.processProperties(properties);
        assertTrue(parser.getOptions().hasOption("opt"));
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        options.addOption("opt", false, "option");
        String[] arguments = {};
        ParseException exception = assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments);
        });
        assertEquals("MissingOptionException", exception.getClass().getSimpleName());
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("opt", false, "option");
        ListIterator iter = Arrays.asList("value1", "value2").listIterator();
        parser.processArgs(option, iter);
        assertNotNull(option.getValues());
    }

    @Test
    public void testProcessOption() {
        Option option = new Option("o", "option");
        ListIterator iter = Arrays.asList("value").listIterator();
        parser.processOption("o", iter);
        assertTrue(parser.getCommandLine().hasOption("o"));
    }
}