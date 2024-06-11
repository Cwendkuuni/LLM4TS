import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("option", "option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertTrue(parser.getRequiredOptions().contains(option));
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("option", "option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-option", "value"};
        try {
            String[] flattened = parser.flatten(options, arguments, false);
            assertEquals(3, flattened.length);
            assertEquals("-option", flattened[0]);
            assertEquals("value", flattened[1]);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testParse() {
        Options options = new Options();
        Option option = new Option("option", "option description", false, "value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-option", "value"};
        try {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("option"));
            assertEquals("value", cmd.getOptionValue("option"));
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", "option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {};
        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs() {
        Options options = new Options();
        Option option = new Option("option", "option description", true, "value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iterator = Arrays.asList("value").listIterator();
        try {
            parser.processArgs(option, iterator);
            assertTrue(option.getValuesList().contains("value"));
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        Option option = new Option("option", "option description", false, null);
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iterator = Arrays.asList("-option").listIterator();
        try {
            parser.processOption("-option", iterator);
            assertTrue(parser.getCommandLine().hasOption("option"));
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        Option option = new Option("option", "option description", false, "value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        parser.processProperties(properties);
        assertTrue(parser.getCommandLine().hasOption("option"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", "option description", true, "value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iterator = Arrays.asList("value", "value2").listIterator();
        parser.processArgs(option, iterator);
    }

}