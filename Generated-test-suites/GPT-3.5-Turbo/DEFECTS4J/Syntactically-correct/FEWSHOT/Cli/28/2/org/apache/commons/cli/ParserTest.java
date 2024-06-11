import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        options.addOption(new Option("b", false, "option b"));

        String[] arguments = new String[]{"-a", "arg1", "-b", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("arg1", commandLine.getOptionValue("a"));
        assertEquals("arg2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParseWithoutOptions() throws Exception {
        Options options = new Options();

        String[] arguments = new String[]{"arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(2, commandLine.getArgs().length);
        assertEquals("arg1", commandLine.getArgs()[0]);
        assertEquals("arg2", commandLine.getArgs()[1]);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption(new Option("a", true, "option a"));
        options.addOption(new Option("b", true, "option b"));

        String[] arguments = new String[]{};

        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));

        String[] arguments = new String[]{"-a", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, true);

        assertTrue(commandLine.hasOption("a"));
        assertEquals(1, commandLine.getArgs().length);
        assertEquals("arg1", commandLine.getArgs()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption(new Option("a", true, "option a"));
        options.addOption(new Option("b", true, "option b"));

        String[] arguments = new String[]{};

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));

        String[] arguments = new String[]{"-a", "-b"};

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
    }
}