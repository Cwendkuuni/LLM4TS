import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }

    @Test
    public void testParseWithOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value"};

        CommandLine result = parser.parse(options, arguments);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertTrue(result.hasOption("b"));
        Assert.assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testProcessArgs() throws Exception {
        Option option = OptionBuilder.withArgName("option")
                .hasArg()
                .withDescription("Option with argument")
                .isRequired()
                .create("opt");

        ListIterator<String> iterator = Arrays.asList("value1", "value2", "value3").listIterator();

        parser.processArgs(option, iterator);

        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
        Assert.assertEquals("value3", option.getValue(2));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws Exception {
        Option option = OptionBuilder.withArgName("option")
                .hasArg()
                .withDescription("Option with argument")
                .isRequired()
                .create("opt");

        ListIterator<String> iterator = Collections.singletonList("").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws Exception {
        Option option = OptionBuilder.withArgName("option")
                .hasArg()
                .withDescription("Option with argument")
                .create("opt");

        ListIterator<String> iterator = Collections.singletonList("-u").listIterator();

        parser.processOption("-u", iterator);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments, properties);

        Assert.assertTrue(result.hasOption("a"));
    }

    @Test
    public void testParseStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "value1", "non-option", "value2"};

        CommandLine result = parser.parse(options, arguments, true);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertEquals(2, result.getArgList().size());
        Assert.assertEquals("value1", result.getArgList().get(0));
        Assert.assertEquals("non-option", result.getArgList().get(1));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOption() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {};

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptionsAllPresent() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {};

        parser.setOptions(options);
        parser.getRequiredOptions().remove("a");
        parser.getRequiredOptions().remove("b");

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        CommandLine cmd = new CommandLine();

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "false");

        parser.setOptions(options);
        parser.processProperties(properties);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

}