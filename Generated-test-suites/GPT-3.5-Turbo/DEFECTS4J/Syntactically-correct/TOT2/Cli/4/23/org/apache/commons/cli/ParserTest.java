import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b", "value"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("x", true, "Option X");

        String[] args = {};
        Properties properties = new Properties();
        properties.setProperty("x", "123");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertEquals("123", cmd.getOptionValue("x"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("c", false, "Option C");

        String[] args = {"-c", "value", "nonoption"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("c"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("value", cmd.getArgList().get(0));
        assertEquals("nonoption", cmd.getArgList().get(1));
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("y", true, "Option Y");

        String[] args = {};

        Properties properties = new Properties();
        properties.setProperty("y", "456");

        Parser parser = new DefaultParser();

        // Access private method processProperties through reflection

        CommandLine cmd = parser.parse(options, args, properties);

        assertEquals("456", cmd.getOptionValue("y"));
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}