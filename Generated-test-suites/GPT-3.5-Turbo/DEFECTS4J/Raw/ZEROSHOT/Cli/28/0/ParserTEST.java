import org.junit.Test;
import org.apache.commons.cli.*;
import java.util.Properties;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertTrue(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Enable verbose mode");
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-v"};
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("verbose"));
        assertFalse(cmd.hasOption("invalidOption"));
        assertTrue(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithArgs() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testParseWithOptionsAndArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("v", "version", false, "Print version information");
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-v", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("version"));
        assertFalse(cmd.hasOption("invalidOption"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-invalid"};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("v", "true");
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Enable verbose mode");
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("verbose"));
        assertFalse(cmd.getArgList().isEmpty());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionValue() throws ParseException {
        Options options = new Options();
        Option option = new Option("v", "value", true, "Option with required value");
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-v"};
        CommandLine cmd = parser.parse(options, args);
    }
}