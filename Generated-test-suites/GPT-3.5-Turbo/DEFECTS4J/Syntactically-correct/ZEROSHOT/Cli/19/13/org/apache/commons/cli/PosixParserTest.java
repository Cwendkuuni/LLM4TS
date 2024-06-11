/**
 * This class provides JUnit tests for the PosixParser class.
 */
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

import static org.junit.Assert.*;

public class PosixParserTest {
    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));

        String[] args = {"-a", "-b", "value"};

        String[] flattenedArgs = posixParser.flatten(options, args, false);

        assertEquals("Expected length of 3", 3, flattenedArgs.length);
        assertEquals("Result should contain -a", "-a", flattenedArgs[0]);
        assertEquals("Result should contain -b", "-b", flattenedArgs[1]);
        assertEquals("Result should contain value", "value", flattenedArgs[2]);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));

        boolean stopAtNonOption = false;

        posixParser.processOptionToken("-a", stopAtNonOption);
        assertEquals("Current option should be option a", options.getOption("a"), posixParser.currentOption);
        assertEquals("Processed tokens should contain -a", "-a", posixParser.tokens.get(0));

        posixParser.init();

        posixParser.processOptionToken("-c", stopAtNonOption);
        assertNull("Current option should be null", posixParser.currentOption);
        assertEquals("Processed tokens should contain -c", "-c", posixParser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));

        boolean stopAtNonOption = false;

        posixParser.burstToken("-abc", stopAtNonOption);
        assertEquals("Processed tokens should contain -a", "-a", posixParser.tokens.get(0));
        assertEquals("Processed tokens should contain -b", "-b", posixParser.tokens.get(1));
        assertEquals("Processed tokens should contain -c", "-c", posixParser.tokens.get(2));
    }
}