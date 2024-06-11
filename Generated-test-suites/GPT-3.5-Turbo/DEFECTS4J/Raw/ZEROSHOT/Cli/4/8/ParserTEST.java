import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        Option option3 = new Option("c", "option3", false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "value", "-c"};
        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.hasOption("c"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("ParseException");
        }
    }

    @Test
    public void testParseProperties() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        try {
            CommandLine cmd = parser.parse(options, args, properties);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("ParseException");
        }
    }

    @Test
    public void testParseStopAtNonOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "value", "nonOption"};
        try {
            CommandLine cmd = parser.parse(options, args, true);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
            assertEquals(1, cmd.getArgs().length);
            assertEquals("nonOption", cmd.getArgs()[0]);
        } catch (ParseException e) {
            fail("ParseException");
        }
    }

    @Test
    public void testMissingRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        String[] args = {"-a", "value1"};
        try {
            CommandLine cmd = parser.parse(options, args);
        } catch (ParseException e) {
            assertTrue(e instanceof MissingOptionException);
            assertEquals("Missing required options: b", e.getMessage());
        }
    }

    @Test
    public void testUnrecognizedOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        options.addOption(option1);

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b"};
        try {
            CommandLine cmd = parser.parse(options, args);
        } catch (ParseException e) {
            assertTrue(e instanceof UnrecognizedOptionException);
            assertEquals("Unrecognized option: -b", e.getMessage());
        }
    }

    @Test
    public void testMissingArgument() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "Option 1");
        options.addOption(option1);

        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        try {
            CommandLine cmd = parser.parse(options, args);
        } catch (ParseException e) {
            assertTrue(e instanceof MissingArgumentException);
            assertEquals("Missing argument for option:a", e.getMessage());
        }
    }

}