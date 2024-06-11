import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testFlatten() {
        // Setup
        Options options = new Options();
        String[] arguments = {"-a", "value1", "-b", "value2"};

        // Execute
        Parser parser = new ParserImpl();
        String[] flattened = parser.flatten(options, arguments, false);

        // Verify
        assertArrayEquals(new String[]{"-a", "value1", "-b", "value2"}, flattened);
    }

    @Test
    public void testParse() {
        // Setup
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        String[] arguments = {"-a", "-b", "value1", "value2"};

        // Execute
        Parser parser = new ParserImpl();
        CommandLine cmdLine = parser.parse(options, arguments);

        // Verify
        assertTrue(cmdLine.hasOption("a"));
        assertTrue(cmdLine.hasOption("b"));
        assertEquals("value1", cmdLine.getOptionValue("b"));
        assertEquals("value2", cmdLine.getArgList().get(0));
    }

    @Test
    public void testProcessProperties() {
        // Setup
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {};

        Properties properties = new Properties();
        properties.setProperty("a", "yes");
        properties.setProperty("b", "value");

        // Execute
        Parser parser = new ParserImpl();
        CommandLine cmdLine = parser.parse(options, arguments, properties);

        // Verify
        assertTrue(cmdLine.hasOption("a"));
        assertTrue(cmdLine.hasOption("b"));
        assertEquals("value", cmdLine.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_missingOption() {
        // Setup
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        String[] arguments = {"-a", "value1"};

        // Execute
        Parser parser = new ParserImpl();
        parser.parse(options, arguments);
    }
}