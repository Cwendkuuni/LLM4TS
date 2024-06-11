import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithOptionsArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        Parser parser = new ParserImpl();
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("p", true, "Option P");
        Parser parser = new ParserImpl();
        Properties properties = new Properties();
        properties.setProperty("p", "value");
        CommandLine cmd = parser.parse(options, new String[]{}, properties);
        assertTrue(cmd.hasOption("p"));
        assertEquals("value", cmd.getOptionValue("p"));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "arg1", "arg2", "--", "-b"};
        try {
            String[] flattened = parser.flatten(options, arguments, true);
            assertArrayEquals(new String[]{"-a", "arg1", "arg2"}, flattened);
        } catch (ParseException e) {
            fail("Exception thrown when flattening arguments");
        }
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "arg1", "arg2", "--", "-b"};
        try {
            String[] flattened = parser.flatten(options, arguments, false);
            assertArrayEquals(new String[]{"-a", "arg1", "arg2", "--", "-b"}, flattened);
        } catch (ParseException e) {
            fail("Exception thrown when flattening arguments");
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option requiredOption = new Option("r", "Required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        Parser parser = new ParserImpl();
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessOptionWithValidOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        Parser parser = new ParserImpl();
        ListIterator iterator = new ArrayList(Arrays.asList("-a")).listIterator();
        try {
            parser.processOption("-a", iterator);
            CommandLine cmd = parser.getCmd();
            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Exception thrown when processing valid option");
        }
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithInvalidOption() throws ParseException {
        Options options = new Options();
        Parser parser = new ParserImpl();
        ListIterator iterator = new ArrayList(Arrays.asList("-x")).listIterator();
        parser.processOption("-x", iterator);
    }
}