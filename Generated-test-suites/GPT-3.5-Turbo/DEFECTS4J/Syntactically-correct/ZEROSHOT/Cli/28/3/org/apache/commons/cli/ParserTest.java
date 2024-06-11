import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        List requiredOptions = parser.getRequiredOptions();
        assertNotNull(requiredOptions);
    }

    @Test
    public void testFlatten() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "arg1"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertNotNull(flattened);
        assertEquals(3, flattened.length);
        assertEquals("--option1", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("arg1", flattened[2]);
    }

    @Test
    public void testParse() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "arg1"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertArrayEquals(new String[]{"arg1"}, commandLine.getArgs());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "arg1"};
        Properties properties = new Properties();
        properties.setProperty("option2", "value2");
        CommandLine commandLine = parser.parse(options, arguments, properties);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
        assertArrayEquals(new String[]{"arg1"}, commandLine.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(new Option("option1", false, ""));
        options.addOption(new Option("option2", false, ""));
        options.addOption(new Option("option3", false, ""));
        String[] arguments = {"--option1", "value1", "--option2", "value2", "arg1"};
        CommandLine commandLine = parser.parse(options, arguments, true);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasArg("value2"));
        assertArrayEquals(new String[]{"value2", "arg1"}, commandLine.getArgs());
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("option1", false, "");
        options.addOption("option2", false, "");
        CommandLine commandLine = new CommandLine();
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        parser.processProperties(options, properties);
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new ParserImpl();
        Option option = new Option("option1", true, "");
        ListIterator iterator = Arrays.asList(new String[]{"value1", "value2"}).listIterator();
        parser.processArgs(option, iterator);
        assertTrue(option.hasArg());
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArg() throws ParseException {
        Parser parser = new ParserImpl();
        Option option = new Option("option1", true, "");
        ListIterator iterator = Arrays.asList(new String[]{}).listIterator();
        parser.processArgs(option, iterator);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        Parser parser = new ParserImpl();
        ListIterator iterator = Arrays.asList(new String[]{"option1"}).listIterator();
        parser.processOption("option1", iterator);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOptions() throws MissingOptionException {
        Parser parser = new ParserImpl();
        Option requiredOption = new Option("option1", true, "");
        Options options = new Options();
        options.addOption(requiredOption);
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}