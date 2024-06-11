import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertTrue(parser.getRequiredOptions().isEmpty());
    }

    @Test
    public void testFlatten() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--option", "arg2"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("--option", flattened[2]);
        assertEquals("arg2", flattened[3]);
    }

    @Test
    public void testFlatten_stopAtNonOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "nonOption", "--option", "arg2"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("nonOption", flattened[2]);
    }

    @Test
    public void testParse() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));
        String[] arguments = {"-a", "value a", "-b", "value b"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value a", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value b", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addRequiredOption(new Option("a", "option a"));
        String[] arguments = {};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_withProperties() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));
        Properties properties = new Properties();
        properties.setProperty("a", "value a");
        properties.setProperty("b", "value b");
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value a", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value b", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse_stopAtNonOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        String[] arguments = {"-a", "value a", "nonOption", "--option", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value a", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("nonOption"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"-a"};
        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new ParserImpl();
        Option option = new Option("a", true, "option a");
        ListIterator iterator = Arrays.asList("arg1", "arg2").listIterator();
        parser.processArgs(option, iterator);
        assertEquals(2, option.getValues().length);
        assertEquals("arg1", option.getValue(0));
        assertEquals("arg2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingValue() throws ParseException {
        Parser parser = new ParserImpl();
        Option option = new Option("a", true, "option a");
        ListIterator iterator = Arrays.asList().listIterator();
        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        Option option = new Option("a", "option a");
        options.addOption(option);
        ListIterator iterator = Arrays.asList("-a", "value a").listIterator();
        parser.processOption("-a", iterator);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value a", cmd.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_unrecognizedOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        ListIterator iterator = Arrays.asList("-a").listIterator();
        parser.processOption("-a", iterator);
    }

}

class ParserImpl extends Parser {
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return arguments;
    }
}