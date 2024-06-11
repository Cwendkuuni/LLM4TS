import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        // Test parsing with options and arguments
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with argument");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "value"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() {
        // Test parsing with properties
        Options options = new Options();
        options.addOption("x", false, "Option X");

        Parser parser = new DefaultParser();
        String[] args = {};
        Properties properties = new Properties();
        properties.setProperty("x", "true");

        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("x"));
    }

    @Test
    public void testParseWithInvalidOption() {
        // Test parsing with an invalid option
        Options options = new Options();
        options.addOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        String[] args = {"-d"};

        try {
            CommandLine cmd = parser.parse(options, args);
            fail("Should have thrown UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            // Expected exception
        }
    }

    // Add more test cases for other methods in Parser class

}