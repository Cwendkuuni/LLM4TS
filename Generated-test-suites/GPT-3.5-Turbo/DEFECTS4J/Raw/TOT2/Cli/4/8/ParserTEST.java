import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        // Test parsing options and arguments
        // Setup
        Options options = new Options();
        options.addOption("h", "help", false, "Prints help message");

        Parser parser = new DefaultParser();
        String[] args = {"-h", "arg1"};

        // Execute
        CommandLine cmd = parser.parse(options, args);

        // Verify
        assertTrue(cmd.hasOption("h"));
        assertEquals("arg1", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithProperties() {
        // Test parsing with properties
        // Setup
        Options options = new Options();
        options.addOption("f", "file", true, "Specify file");

        Parser parser = new DefaultParser();
        String[] args = {"-f", "test.txt"};
        Properties properties = new Properties();
        properties.setProperty("file", "test.properties");

        // Execute
        CommandLine cmd = parser.parse(options, args, properties);

        // Verify
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertEquals("test.properties", cmd.getOptionValue("file"));
    }

    @Test
    public void testParseWithRequiredOptions() {
        // Test parsing with required options
        // Setup
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"-r", "value"};

        // Execute and Verify
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseStopAtNonOption() {
        // Test parsing with stop at non-option
        // Setup
        Options options = new Options();
        options.addOption("f", "file", true, "Specify file");

        Parser parser = new DefaultParser();
        String[] args = {"-f", "test.txt", "arg1", "--", "arg2"};

        // Execute
        CommandLine cmd = parser.parse(options, args, true);

        // Verify
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertArrayEquals(new String[]{"arg1", "--", "arg2"}, cmd.getArgs());
    }

}