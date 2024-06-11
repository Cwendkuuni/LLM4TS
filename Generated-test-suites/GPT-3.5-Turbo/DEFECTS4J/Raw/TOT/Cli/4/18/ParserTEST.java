import org.apache.commons.cli.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private static Options options;
    private static final String[] ARGUMENTS = {"-a", "valueA", "--opt", "--help", "arg1", "arg2"};

    @BeforeClass
    public static void setUp() {
        options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("opt", false, "option opt");
        options.addOption("help", false, "option help");
    }

    @Test
    public void testParse_NoStopAtNonOption_CorrectCommandlineArguments() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, ARGUMENTS, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("help"));

        assertEquals("valueA", cmd.getOptionValue("a"));

        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testParse_NoStopAtNonOption_MissingRequiredOption() {
        options.addOption(Option.builder("r").required().build());

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, ARGUMENTS, false);
            fail("Expected exception not thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required options: r", e.getMessage());
        } catch (ParseException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, ARGUMENTS, true);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("help"));

        assertEquals("valueA", cmd.getOptionValue("a"));

        String[] args = cmd.getArgs();
        assertEquals(4, args.length);
        assertEquals("--help", args[0]);
        assertEquals("arg1", args[1]);
        assertEquals("arg2", args[2]);
        assertEquals("--help", args[3]);
    }

    @Test
    public void testProcessProperties_AddOptionAndValue() throws ParseException {
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");

        CommandLine cmd = parser.parse(options, ARGUMENTS, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("help"));

        assertEquals("valueA", cmd.getOptionValue("a"));
    }

    @Test
    public void testProcessProperties_IgnoreOptionWithEmptyValue() throws ParseException {
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "");

        CommandLine cmd = parser.parse(options, ARGUMENTS, properties);

        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testProcessProperties_IgnoreOptionWithInvalidValue() throws ParseException {
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "invalidValue");

        CommandLine cmd = parser.parse(options, ARGUMENTS, properties);

        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testProcessArgs_OptionWithValue() throws ParseException {
        Option option = Option.builder("a").argName("value").hasArg().build();

        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("valueA", cmd.getOptionValue("a"));
    }

    @Test
    public void testProcessArgs_MissingOptionValue() {
        Option option = Option.builder("a").argName("value").hasArg().build();

        Parser parser = new DefaultParser();
        String[] arguments = {"-a"};
        try {
            parser.parse(options, arguments);
            fail("Expected exception not thrown");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:a", e.getMessage());
        } catch (ParseException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testProcessOption_ValidOption() throws ParseException {
        Option option = Option.builder("a").argName("value").hasArg().build();

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, ARGUMENTS);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("invalid"));
    }

    @Test
    public void testProcessOption_UnrecognizedOption() {
        Option option = Option.builder("a").argName("value").hasArg().build();

        Parser parser = new DefaultParser();
        String[] arguments = {"-b"};
        try {
            parser.parse(options, arguments);
            fail("Expected exception not thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -b", e.getMessage());
        } catch (ParseException e) {
            fail("Wrong exception thrown");
        }
    }

}