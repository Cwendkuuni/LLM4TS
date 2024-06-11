import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParseOptionsStringArray() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] arguments = {"-a", "-b", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsStringArrayProperties() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsStringArrayBoolean() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] arguments = {"-a", "-b", "value", "extra"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
        assertEquals("value", cmd.getArgList().get(0));
        assertEquals("extra", cmd.getArgList().get(1));
    }

    @Test
    public void testParseOptionsStringArrayPropertiesBoolean() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments, properties, true);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        Option option = Option.builder("a").hasArg().build();
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        parser.processProperties(properties);
        assertEquals(cmd, parser.getCommandLine());
    }

    @Test
    public void testCheckRequiredOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        Option option = Option.builder("a").required().build();
        options.addOption(option);
        List requiredOptions = new ArrayList();
        requiredOptions.add(option);
        parser.setOptions(options);
        parser.checkRequiredOptions();
        assertEquals(requiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new ParserImpl();
        Option opt = Option.builder("a").hasArg().build();
        ListIterator iter = Arrays.asList("value").listIterator();
        parser.processArgs(opt, iter);
        assertEquals("value", opt.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Parser parser = new ParserImpl();
        Option opt = Option.builder("a").hasArg().build();
        ListIterator iter = Collections.emptyList().listIterator();
        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        Option option = Option.builder("a").required().hasArg().build();
        options.addOption(option);
        parser.setOptions(options);
        ListIterator iter = Arrays.asList("-a", "value").listIterator();
        parser.processOption("-a", iter);
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertEquals("value", parser.getCommandLine().getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        ListIterator iter = Arrays.asList("-c").listIterator();
        parser.processOption("-c", iter);
    }

}

class ParserImpl extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
    }
}