import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("h", "help", false, "print this message");

        PosixParser parser = new PosixParser();
        String[] args = {"-h"};
        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-h", result[0]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"-h", "--", "arg1", "arg2"}).iterator();
        parser.gobble(iter);

        assertTrue(parser.eatTheRest);
        assertEquals(4, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("arg1");

        assertTrue(parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("h", false, "print help message");
        PosixParser parser = new PosixParser();
        parser.processOptionToken("h", true);

        assertNotNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("h", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("f", false, "set flag");

        PosixParser parser = new PosixParser();
        parser.burstToken("-fh", true);

        assertNotNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));
        assertEquals("h", parser.tokens.get(1));
    }
}