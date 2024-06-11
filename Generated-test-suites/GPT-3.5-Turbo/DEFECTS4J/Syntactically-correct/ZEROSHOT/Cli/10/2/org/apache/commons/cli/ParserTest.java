import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", "Required Option C");
    }

    @Test
    public void testSetOptions() {
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        parser.setOptions(options);
        Assert.assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("b", true, "Option B");
        ListIterator iterator = Arrays.asList("-b", "value").listIterator();
        parser.processArgs(option, iterator);
        Assert.assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("b", true, "Option B");
        ListIterator iterator = Arrays.asList("-b").listIterator();
        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("a", false, "Option A");
        ListIterator iterator = Arrays.asList("-a").listIterator();
        parser.processOption("-a", iterator);
        Assert.assertTrue(parser.getOptions().hasOption("a"));
        Assert.assertTrue(parser.getCommandLine().hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Option option = new Option("a", false, "Option A");
        ListIterator iterator = Arrays.asList("-x").listIterator();
        parser.processOption("-x", iterator);
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = "-a -b value -c".split(" ");
        CommandLine commandLine = parser.parse(options, arguments);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("value", commandLine.getOptionValue("b"));
        Assert.assertTrue(commandLine.hasOption("c"));
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        String[] arguments = "-a -b value nonOption".split(" ");
        CommandLine commandLine = parser.parse(options, arguments, true);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("value", commandLine.getOptionValue("b"));
        Assert.assertTrue(commandLine.getArgs().contains("nonOption"));
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");
        properties.setProperty("c", "true");
        parser.processProperties(properties);
        CommandLine commandLine = parser.getCommandLine();
        Assert.assertEquals("true", commandLine.getOptionValue("a"));
        Assert.assertEquals("value", commandLine.getOptionValue("b"));
        Assert.assertEquals("true", commandLine.getOptionValue("c"));
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        parser.setOptions(options);
        parser.getRequiredOptions().remove("c");
        Assert.assertTrue(parser.getRequiredOptions().isEmpty());
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOption() throws MissingOptionException {
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
}