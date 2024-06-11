import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        // Check if tokens list is cleared
        assertTrue(parser.tokens.isEmpty());
        // Check if eatTheRest is set to false
        assertFalse(parser.eatTheRest);
        // Check if currentOption is set to null
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        // Check if "-" is added to tokens list
        assertTrue(parser.tokens.contains("-"));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", "option A");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.processOptionToken("a", false);
        // Check if option "a" is added to tokens list
        assertTrue(parser.tokens.contains("a"));
        // Check if currentOption is set to option "a"
        assertEquals(options.getOption("a"), parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", false, "option B");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "arg1", "-b", "-c", "arg2"};
        String[] flattened = parser.flatten(options, arguments, true);

        // Check if arguments are properly flattened
        assertArrayEquals(new String[]{"-a", "arg1", "-b", "-c", "arg2"}, flattened);
    }
}