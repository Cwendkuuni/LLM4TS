import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] arguments;
    private Properties properties;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();

        Option option1 = new Option("a", "option1", false, "Option 1");
        options.addOption(option1);

        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option2);

        arguments = new String[]{"-a", "-b", "value"};

        properties = new Properties();
        properties.setProperty("option1", "true");
        properties.setProperty("option2", "value");
    }

    @Test
    public void testParse() {
        try {
            CommandLine cmd = parser.parse(options, arguments);
            
            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseWithProperties() {
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            
            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseStopAtNonOption() {
        try {
            CommandLine cmd = parser.parse(options, arguments, true);
            
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertEquals(2, cmd.getArgs().length);
            assertEquals("-b", cmd.getArgs()[0]);
            assertEquals("value", cmd.getArgs()[1]);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseWithPropertiesStopAtNonOption() {
        try {
            CommandLine cmd = parser.parse(options, arguments, properties, true);
            
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertEquals(2, cmd.getArgs().length);
            assertEquals("-b", cmd.getArgs()[0]);
            assertEquals("value", cmd.getArgs()[1]);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}