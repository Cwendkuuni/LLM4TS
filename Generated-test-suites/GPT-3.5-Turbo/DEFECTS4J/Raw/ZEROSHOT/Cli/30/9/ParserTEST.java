import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("f", "foo", false, "foo option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals("f", ((Option) parser.getRequiredOptions().get(0)).getOpt());
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").hasArgs().argName("files").required().build();
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        ListIterator<String> iterator = Arrays.asList("file1", "file2").listIterator();
        parser.processArgs(option, iterator);
        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("file1", values[0]);
        assertEquals("file2", values[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").hasArg().argName("file").required().build();
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        ListIterator<String> iterator = Arrays.asList().listIterator();
        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").hasArg().build();
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        ListIterator<String> iterator = Arrays.asList("-f", "file.txt").listIterator();
        parser.processOption("-f", iterator);
        CommandLine cmd = parser.getCmd();
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").hasArg().build();
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        ListIterator<String> iterator = Arrays.asList("-h", "file.txt").listIterator();
        parser.processOption("-h", iterator);
    }

    @Test
    public void testProcessProperties() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").argName("file").build();
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[0], properties);
        assertTrue(parser.getCmd().hasOption("f"));
        assertEquals("file.txt", parser.getCmd().getOptionValue("f"));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").argName("file").required().build();
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        List<String> requiredOptions = parser.getRequiredOptions();
        assertEquals("f", ((Option) requiredOptions.get(0)).getOpt());
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOption() throws ParseException {
        Options options = new Options();
        Option requiredOption = Option.builder("f").argName("file").required().build();
        options.addOption(requiredOption);
        Option nonRequiredOption = Option.builder("g").argName("group").build();
        options.addOption(nonRequiredOption);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
}