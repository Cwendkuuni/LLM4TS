import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
    }

    @Test
    public void testParse() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "an option");
        options.addOption(option);

        String[] arguments = new String[]{"-a"};

        try {
            CommandLine result = parser.parse(options, arguments);

            assertTrue(result.hasOption("a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "an option");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        String[] arguments = new String[0];

        try {
            CommandLine result = parser.parse(options, arguments, properties);

            assertTrue(result.hasOption("a"));
            assertEquals("value", result.getOptionValue("a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "an option");
        options.addOption(option);

        String[] arguments = new String[]{"-a", "nonOption"};

        try {
            CommandLine result = parser.parse(options, arguments, true);

            assertTrue(result.hasOption("a"));
            assertFalse(result.hasOption("nonOption"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseWithRequiredOption() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "an option");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = new String[0];

        try {
            parser.parse(options, arguments);
            fail("ParseException not thrown");
        } catch (ParseException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("a", "option", true, "an option");
        ListIterator iter = Arrays.asList(new String[]{"value1", "value2"}).listIterator();

        try {
            parser.processArgs(option, iter);

            assertTrue(option.hasValue());
            assertEquals("value1", option.getValue(0));
            assertEquals("value2", option.getValue(1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "an option");
        options.addOption(option);

        ListIterator iter = Arrays.asList(new String[]{"-a", "value1", "value2"}).listIterator();

        try {
            parser.processOption("-a", iter);

            CommandLine result = parser.parse(options, new String[0]);

            assertTrue(result.hasOption("a"));
            assertEquals("value1", result.getOptionValue("a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}