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
                return null;
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {

            }

            @Override
            public void processArgs(Option opt, ListIterator iter) throws ParseException {

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
    public void testGetRequiredOptions() {
        Options options = new Options();
        parser.setOptions(options);
        List requiredOptions = parser.getRequiredOptions();
        assertNotNull(requiredOptions);
        assertEquals(0, requiredOptions.size());

        options.addOption(Option.builder("f").required().build());
        parser.setOptions(options);
        requiredOptions = parser.getRequiredOptions();
        assertNotNull(requiredOptions);
        assertEquals(1, requiredOptions.size());
        assertEquals("f", ((Option) requiredOptions.get(0)).getOpt());
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("f", "value");
        properties.setProperty("g", "true");

        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        options.addOption(Option.builder("g").build());

        parser.setOptions(options);
        parser.processProperties(properties);

        CommandLine cmd = parser.parse(options, new String[]{});

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("g"));
        assertEquals("value", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
}