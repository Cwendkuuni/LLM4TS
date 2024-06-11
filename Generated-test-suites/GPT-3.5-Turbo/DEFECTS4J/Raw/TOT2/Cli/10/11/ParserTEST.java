import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        parser.setOptions(options);
        List requiredOptions = Arrays.asList(new Option("required", "description", false, "value"));
        options.addRequiredOptions(requiredOptions);
        assertEquals(requiredOptions, parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "arg1", "-b", "arg2"}, flattenedArguments);
    }

    @Test
    public void testParse() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        parser.processProperties(properties);
        assertEquals("value1", parser.getOptions().getOption("option1").getValue());
    }

    @Test
    public void testCheckRequiredOptions() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("required", "description", false, "value"));
        options.addRequiredOptions(requiredOptions);
        parser.setOptions(options);
        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: required", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() {
        Parser parser = new ParserImpl();
        Option option = new Option("test", "description", true, "test");
        ListIterator iter = Arrays.asList("value1", "value2").listIterator();
        parser.processArgs(option, iter);
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testProcessOption() {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption(new Option("opt", "description"));
        ListIterator iter = Arrays.asList("opt").listIterator();
        parser.processOption("opt", iter);
        assertTrue(parser.getCommandLine().hasOption("opt"));
    }
}