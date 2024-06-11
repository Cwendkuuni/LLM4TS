import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsArgumentsStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("e").build());

        String[] arguments = new String[]{"-f", "file.txt", "arg1", "arg2"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("e"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParse_OptionsArgumentsStopAtNonOption_RemainingArguments() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("e").build());

        String[] arguments = new String[]{"-f", "file.txt", "arg1", "arg2"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, false);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("e"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParse_OptionsArgumentsWithProperties() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("e").build());

        String[] arguments = new String[]{"-f", "file.txt", "-b", "-e"};

        Properties properties = new Properties();
        properties.setProperty("b", "true");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("e"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOptionException() throws Exception {
        Options options = new Options();

        String[] arguments = new String[]{"-f", "file.txt"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgumentException() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("f").argName("file").hasArg().build());

        String[] arguments = new String[]{"-f"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingOptionException() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        options.addOption(Option.builder("b").required().build());

        String[] arguments = new String[]{};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }
}