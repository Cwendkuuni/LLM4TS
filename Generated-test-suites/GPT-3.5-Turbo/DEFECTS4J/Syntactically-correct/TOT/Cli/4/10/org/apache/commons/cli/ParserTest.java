import org.apache.commons.cli.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private CommandLineParser commandLineParser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        options = new Options();
        commandLineParser = new DefaultParser();
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
        commandLineParser = null;
    }

    @Test
    public void testParse_NoOptions_ReturnsEmptyCommandLine() throws ParseException {
        String[] args = new String[0];

        CommandLine commandLine = parser.parse(options, args);

        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_SingleOptionWithNoArgument_ReturnsCommandLineWithSingleOption() throws ParseException {
        options.addOption("a", false, "Option A");

        String[] args = new String[]{"-a"};

        CommandLine commandLine = parser.parse(options, args);

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_SingleOptionWithArgument_ReturnsCommandLineWithOptionAndArgument() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());

        String[] args = new String[]{"-a", "value"};

        CommandLine commandLine = parser.parse(options, args);

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertTrue(commandLine.getArgs().length == 0);
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_SingleOptionWithMissingArgument_ThrowsMissingArgumentException() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());

        String[] args = new String[]{"-a"};

        CommandLine commandLine = parser.parse(options, args);
    }

    @Test
    public void testParse_MultipleOptionsWithArguments_ReturnsCommandLineWithOptionsAndArguments() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").hasArg().build());

        String[] args = new String[]{"-a", "value1", "-b", "value2", "-c", "value3"};

        CommandLine commandLine = parser.parse(options, args);

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.getArgs().length == 0);
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertEquals("value2", commandLine.getOptionValue("b"));
        assertEquals("value3", commandLine.getOptionValue("c"));
    }

    @Test
    public void testParse_ArgumentsAfterDoubleDash_StopParsingAfterDoubleDash() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());

        String[] args = new String[]{"-a", "value1", "--", "-b", "value2"};

        CommandLine commandLine = parser.parse(options, args);

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertTrue(commandLine.getArgs().length == 2);
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertEquals("-b", commandLine.getArgs()[0]);
        assertEquals("value2", commandLine.getArgs()[1]);
    }

}