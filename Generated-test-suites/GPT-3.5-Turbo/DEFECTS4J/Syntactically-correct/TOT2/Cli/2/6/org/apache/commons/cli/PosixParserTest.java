import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
        assertFalse(posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption("h", "help", false, "print this help message");
        options.addOption("v", "verbose", false, "verbose output");
        String[] arguments = {"-h", "--help", "-v", "verbose"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, false);
        assertEquals(4, flattenedArguments.length);
        assertEquals("-h", flattenedArguments[0]);
        assertEquals("--help", flattenedArguments[1]);
        assertEquals("-v", flattenedArguments[2]);
        assertEquals("verbose", flattenedArguments[3]);
    }

    @Test
    public void testGobble() {
        PosixParser posixParser = new PosixParser();
        Iterator<String> iter = Arrays.asList("one", "two", "three").iterator();
        posixParser.gobble(iter);
        assertTrue(posixParser.eatTheRest);
    }

    @Test
    public void testProcess() {
        PosixParser posixParser = new PosixParser();
        posixParser.currentOption = new Option("v", "verbose", false, "verbose output");
        posixParser.process("value");
        assertEquals(2, posixParser.tokens.size());
        assertEquals("--", posixParser.tokens.get(0));
        assertEquals("value", posixParser.tokens.get(1));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser posixParser = new PosixParser();
        posixParser.processSingleHyphen("-");
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-", posixParser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption("h", "help", false, "print this help message");
        posixParser.options = options;
        posixParser.processOptionToken("-h", false);
        assertNotNull(posixParser.currentOption);
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-h", posixParser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption("h", "help", false, "print this help message");
        options.addOption("v", "verbose", false, "verbose output");
        posixParser.options = options;
        posixParser.burstToken("-hv", false);
        assertEquals(2, posixParser.tokens.size());
        assertEquals("-h", posixParser.tokens.get(0));
        assertEquals("-v", posixParser.tokens.get(1));
    }
}