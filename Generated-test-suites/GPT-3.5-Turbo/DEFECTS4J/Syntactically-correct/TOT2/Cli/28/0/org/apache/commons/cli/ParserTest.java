import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        // Test setting Options
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testProcessProperties() {
        // Test processing Properties
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        options.addOption("option1", false, "option1 description");
        parser.setOptions(options);
        parser.parse(options, new String[0], properties);
        assertTrue(parser.getCmd().hasOption("option1"));
    }

    @Test
    public void testCheckRequiredOptions() {
        // Test checking Required Options
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        Options options = new Options();
        options.addOption("requiredOption", true, "required option description");
        parser.setOptions(options);
        try {
            parser.parse(options, new String[0]);
            fail("MissingOptionException not thrown for missing required option");
        } catch (MissingOptionException e) {
            assertEquals(1, e.getMissingOptions().size());
        }
    }

    @Test
    public void testProcessArgs() {
        // Test processing Arguments
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        Option option = new Option("argOption", true, "option with argument");
        parser.setOptions(options);
        ListIterator iter = Arrays.asList(new String[]{"value1"}).listIterator();
        try {
            parser.processArgs(option, iter);
            assertTrue(option.getValuesList().contains("value1"));
        } catch (ParseException e) {
            fail("ParseException thrown for valid argument");
        }
    }

    @Test
    public void testProcessOption() {
        // Test processing Option
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        options.addOption("testOption", false, "test option");
        parser.processOption("testOption", Arrays.asList(new String[]{}).listIterator());
        assertTrue(parser.getCmd().hasOption("testOption"));
    }

    @Test
    public void testParse() {
        // Test parsing command line arguments
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[]{"-testOption", "value"};
            }
        };
        options.addOption("testOption", true, "test option with argument");
        parser.setOptions(options);
        try {
            CommandLine cmd = parser.parse(options, new String[]{});
            assertTrue(cmd.hasOption("testOption"));
            assertEquals("value", cmd.getOptionValue("testOption"));
        } catch (ParseException e) {
            fail("ParseException thrown for valid arguments");
        }
    }

}