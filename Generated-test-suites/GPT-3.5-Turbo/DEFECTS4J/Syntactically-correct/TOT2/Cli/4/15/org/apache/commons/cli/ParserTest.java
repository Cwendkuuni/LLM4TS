import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseOptions() {
        // Test parsing options with arguments
        Options options = new Options();
        Option option = Option.builder("f").hasArg().build();
        options.addOption(option);

        String[] args = {"-f", "file.txt"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testMissingArgument() {
        // Test missing argument scenario
        Options options = new Options();
        Option option = Option.builder("f").hasArg().build();
        options.addOption(option);

        String[] args = {"-f"};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, args);
            fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testUnrecognizedOption() {
        // Test unrecognized option scenario
        Options options = new Options();
        Option option = Option.builder("f").hasArg().build();
        options.addOption(option);

        String[] args = {"-x"};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, args);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            // Expected exception
        }
    }
}