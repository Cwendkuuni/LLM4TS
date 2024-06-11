import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testProcessArgs() {
        Option option = new Option("o", "output", true, "output file");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        ListIterator<String> iterator = Arrays.asList("-o", "file.txt").listIterator();
        try {
            parser.processArgs(option, iterator);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
        assertEquals("file.txt", option.getValue());
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        ListIterator<String> iterator = Arrays.asList("-f", "file.txt").listIterator();
        try {
            parser.processOption("-f", iterator);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
        assertTrue(parser.getCmd().hasOption("f"));
        assertEquals("file.txt", parser.getCmd().getOptionValue("f"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        ListIterator<String> iterator = Arrays.asList("-x").listIterator();
        parser.processOption("-x", iterator);
    }

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        String[] arguments = {"-f", "file.txt"};
        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("f"));
            assertEquals("file.txt", cmd.getOptionValue("f"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        String[] arguments = null;
        Properties properties = new Properties();
        properties.setProperty("file", "file.txt");
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            assertTrue(cmd.hasOption("f"));
            assertEquals("file.txt", cmd.getOptionValue("f"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        options.addRequiredOption(Option.builder("f").longOpt("file").hasArg().build());
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            fail("Exception should not be thrown");
        }
        CommandLine cmd = parser.getCmd();
        cmd.addOption(Option.builder("f").longOpt("file").hasArg().build());
        parser.setOptions(options);
        try {
            parser.checkRequiredOptions();
            fail("Exception should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: f", e.getMessage());
        }
    }
}