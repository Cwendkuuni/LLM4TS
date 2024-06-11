import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "apple", false, "include apple");
        options.addOption("b", "banana", true, "include banana");
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--", "-b", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("t", "test", true, "test option");
        parser.process("value");
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("t", "test", false, "test option");
        
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-t", false);
        assertEquals("-t", parser.tokens.get(0));
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", "apple", false, "include apple");

        PosixParser parser = new PosixParser();
        parser.burstToken("-ab", false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}