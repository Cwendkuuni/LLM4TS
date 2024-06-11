import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-a", "1", "-b", "2"};
        Parser parser = new ParserImpl();

        String[] flattened = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "1", "-b", "2"};

        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-a", "1", "-b", "2"};
        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertArrayEquals(new String[]{"1"}, commandLine.getOptionValues("a"));
        assertArrayEquals(new String[]{"2"}, commandLine.getOptionValues("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").argName("value").desc("Option A").hasArg().build());
        options.addOption(Option.builder("b").desc("Option B").build());
        String[] arguments = {};
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "true");
        Parser parser = new ParserImpl();

        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertArrayEquals(new String[]{"1"}, commandLine.getOptionValues("a"));
        assertNull(commandLine.getOptionValues("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").desc("Option A").required().build());
        String[] arguments = {};
        Parser parser = new ParserImpl();

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").hasArgs().build();
        ListIterator iterator = Arrays.asList("1", "2", "3").listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(option, iterator);

        assertArrayEquals(new String[]{"1", "2", "3"}, option.getValues());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = Option.builder("a").hasArgs().build();
        ListIterator iterator = Arrays.asList().listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = Option.builder("a").build();
        ListIterator iterator = Arrays.asList("-a").listIterator();

        Parser parser = new ParserImpl();
        parser.processOption("-a", iterator);

        assertTrue(option.hasOptionalArg());
        assertEquals("-a", option.getValues()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        ListIterator iterator = Arrays.asList("-x").listIterator();

        Parser parser = new ParserImpl();
        parser.processOption("-x", iterator);
    }

    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Option option = Option.builder("a").required().build();

        Parser parser = new ParserImpl();
        parser.updateRequiredOptions(option);

        assertFalse(parser.getRequiredOptions().contains("a"));
    }

}