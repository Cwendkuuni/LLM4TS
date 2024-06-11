import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class ParserTest {
    private CommandLineParser parser = new DefaultParser();
    private Option option1 = Option.builder("a").desc("Option 1").build();
    private Option option2 = Option.builder("b").desc("Option 2").hasArg().argName("arg2").build();

    @Test
    public void testParseNoOptions() throws ParseException {
        String[] args = {};
        Options options = new Options();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getOptions().length == 0);
    }

    @Test
    public void testParseSingleOptionNoArg() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption(option1);
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseSingleOptionWithArg() throws ParseException {
        String[] args = {"-b", "value"};
        Options options = new Options();
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseMultipleOptionsNoArgs() throws ParseException {
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseMultipleOptionsWithArgs() throws ParseException {
        String[] args = {"-a", "-b", "value"};
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {};
        Options options = new Options();
        options.addOption(option1);
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = {"-c"};
        Options options = new Options();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgumentValue() throws ParseException {
        String[] args = {"-b"};
        Options options = new Options();
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, args);
    }
}