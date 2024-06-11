import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, properties);
        assertNotNull(cmd);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        boolean stopAtNonOption = true;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, stopAtNonOption);
        assertNotNull(cmd);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Option option = new Option("o", "option", false, "an option");
        Options options = new Options();
        options.addOption(option);
        String[] arguments = new String[]{"arg1", "arg2"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-invalid"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }
}