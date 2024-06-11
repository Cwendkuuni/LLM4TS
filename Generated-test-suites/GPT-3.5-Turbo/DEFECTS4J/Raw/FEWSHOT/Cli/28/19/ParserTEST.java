import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;

public class ParserTest {
    
    private Options options;
    
    public ParserTest() {
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
    }

    @Test
    public void testParse_noArguments() throws ParseException {
        String[] arguments = {};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParse_optionA() throws ParseException {
        String[] arguments = {"-a"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParse_optionB() throws ParseException {
        String[] arguments = {"-b", "value"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse_multipleOptions() throws ParseException {
        String[] arguments = {"-a", "-b", "value"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse_unrecognizedOption() {
        String[] arguments = {"-c"};

        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options, arguments);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -c", e.getMessage());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }
}