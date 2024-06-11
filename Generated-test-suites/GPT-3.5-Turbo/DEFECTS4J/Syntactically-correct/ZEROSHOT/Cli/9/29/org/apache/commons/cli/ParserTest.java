import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        options = new Options();
    }

    @Test
    public void testSetOptions() {
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        parser.setOptions(options);
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() {
        String[] arguments = new String[]{"-a", "-b", "value"};
        CommandLine cmd = new CommandLine();
        Options options = new Options();

        try {
            cmd = parser.parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Assert statements for the command line arguments
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getArgList().contains("value"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        String[] arguments = new String[]{"-a", "-b", "value", "-c", "non-option"};
        CommandLine cmd = new CommandLine();
        Options options = new Options();

        try {
            cmd = parser.parse(options, arguments, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Assert statements for the command line arguments
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getArgList().contains("value"));
        assertTrue(cmd.getArgList().contains("non-option"));
    }

    @Test
    public void testParseWithProperties() {
        String[] arguments = new String[]{"-a", "-b"};
        Properties properties = new Properties();
        properties.setProperty("c", "value");
        CommandLine cmd = new CommandLine();
        Options options = new Options();

        try {
            cmd = parser.parse(options, arguments, properties);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Assert statements for the command line arguments
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("c"));
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        properties.setProperty("b", "valueB");

        parser.processProperties(properties);

        CommandLine cmd = parser.parse(options, new String[]{});

        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));

    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        options.addOption(Option.builder("a").required().build());
        parser.setOptions(options);

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() {
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList("-a", "value").listIterator();
        CommandLine cmd = new CommandLine();
        options.addOption(option);

        try {
            parser.processArgs(option, iterator);
            cmd = parser.parse(options, new String[]{});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws MissingArgumentException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList("-a").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() {
        Option option = Option.builder("a").build();
        ListIterator iterator = Arrays.asList("-a").listIterator();
        CommandLine cmd = new CommandLine();
        options.addOption(option);

        try {
            parser.processOption("-a", iterator);
            cmd = parser.parse(options, new String[]{});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws UnrecognizedOptionException {
        ListIterator iterator = Arrays.asList("-z").listIterator();

        parser.processOption("-z", iterator);
    }
}