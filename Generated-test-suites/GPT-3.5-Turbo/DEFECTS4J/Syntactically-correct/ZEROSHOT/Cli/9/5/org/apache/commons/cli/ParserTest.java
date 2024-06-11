import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Properties;

public class ParserTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a", "-b", "value"};
        CommandLine commandLine = parser.parse(options, args);
        Assert.assertNotNull(commandLine);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        String[] args = {"-a"};
        CommandLine commandLine = parser.parse(options, args, properties);
        Assert.assertNotNull(commandLine);
    }

    @Test
    public void testParseWithOptionsAndStopAtNonOption() throws ParseException {
        String[] args = {"-a", "-b", "value", "--", "nonOption"};
        CommandLine commandLine = parser.parse(options, args, true);
        Assert.assertNotNull(commandLine);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOption() throws MissingOptionException {
        options.addRequiredOption("a");
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        options.addRequiredOption("a");
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").hasArg(true).build();
        String[] args = {"value"};
        ListIterator iterator = Arrays.asList(args).listIterator();
        parser.processArgs(option, iterator);
        Assert.assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option option = Option.builder("a").hasArg(true).build();
        parser.processArgs(option, new ArrayList<String>().listIterator());
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        options.addOption(Option.builder("a").build());
        ListIterator iterator = Arrays.asList("-a").listIterator();
        parser.processOption("-a", iterator);
        Assert.assertTrue(parser.getCommand().hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        parser.processOption("-c", new ArrayList<String>().listIterator());
    }

}