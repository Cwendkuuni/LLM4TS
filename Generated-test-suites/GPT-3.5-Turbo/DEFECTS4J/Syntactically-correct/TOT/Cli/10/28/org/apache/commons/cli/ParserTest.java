import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    // Test for the parse(Options options, String[] arguments) method
    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new Parser();

        try {
            CommandLine cmd = parser.parse(options, arguments);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));

        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    // Test for the parse(Options options, String[] arguments, Properties properties) method
    @Test
    public void testParseWithOptionsArgumentsAndProperties() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new Parser();

        try {
            CommandLine cmd = parser.parse(options, arguments, properties);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));

        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    // Test for the parse(Options options, String[] arguments, boolean stopAtNonOption) method
    @Test
    public void testParseWithOptionsArgumentsAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "nonOption"};

        Parser parser = new Parser();

        try {
            CommandLine cmd = parser.parse(options, arguments, true);

            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertTrue(cmd.getArgList().contains("nonOption"));

        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    // Test for the parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) method
    @Test
    public void testParseWithOptionsArgumentsPropertiesAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "nonOption"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new Parser();

        try {
            CommandLine cmd = parser.parse(options, arguments, properties, true);

            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertTrue(cmd.getArgList().contains("nonOption"));

        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    // Test for the flatten(Options opts, String[] arguments, boolean stopAtNonOption) method
    @Test
    public void testFlattenWithOptionsArgumentsAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new Parser();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, flattenedArguments);
    }

    // Test for the processOption(String arg, ListIterator iter) method
    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new Parser();

        ListIterator iterator = Arrays.asList(arguments).listIterator();

        try {
            parser.processOption("a", iterator);
            parser.processOption("b", iterator);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }

        assertFalse(iterator.hasNext());
    }

    // Test for the processProperties(Properties properties) method
    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new Parser();

        try {
            CommandLine cmd = parser.parse(options, arguments, properties);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));

        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    // Test for the checkRequiredOptions() method
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        options.addOption(Option.builder("b").required(true).build());

        String[] arguments = {};

        Parser parser = new Parser();

        parser.parse(options, arguments);
    }

    // Test for the processArgs(Option opt, ListIterator iter) method
    @Test
    public void testProcessArgs() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg(true).build());

        String[] arguments = {"-a", "value"};

        Parser parser = new Parser();

        ListIterator iterator = Arrays.asList(arguments).listIterator();

        try {
            Option option = Option.builder("a").hasArg(true).build();

            parser.processArgs(option, iterator);

            assertEquals("value", option.getValue());

        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

}