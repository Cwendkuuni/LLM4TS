import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        parser.setOptions(options);
        List expectedRequiredOptions = new ArrayList();
        assertEquals(expectedRequiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testProcessProperties() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("f", "file", false, "file to process");
        option.setArgName("file");
        options.addOption(option);
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.put("file", "test.txt");
        parser.processProperties(properties);
        assertTrue(parser.parse(options, new String[] {}).hasOption("f"));
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("f", "file", false, "file to process");
        option.setRequired(true);
        options.addOption(option);
        parser.setOptions(options);
        try {
            parser.checkRequiredOptions();
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing required options: f", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("f", "file", true, "file to process");
        option.setArgName("file");
        options.addOption(option);
        parser.setOptions(options);
        ListIterator iterator = Arrays.asList(new String[] { "test.txt" }).listIterator();
        parser.processArgs(option, iterator);
        assertEquals("test.txt", option.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("f", "file", true, "file to process");
        option.setArgName("file");
        options.addOption(option);
        parser.setOptions(options);
        ListIterator iterator = Arrays.asList(new String[] { "-f", "test.txt" }).listIterator();
        parser.processOption("-f", iterator);
        assertTrue(parser.parse(options, new String[] {}).hasOption("f"));
    }

}