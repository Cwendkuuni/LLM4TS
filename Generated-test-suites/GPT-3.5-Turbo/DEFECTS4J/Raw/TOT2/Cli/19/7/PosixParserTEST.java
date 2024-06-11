import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("f", "file", false, "File option");
        options.addOption(option);
        String[] arguments = {"-f", "test.txt"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-f", flattened[0]);
        assertEquals("test.txt", flattened[1]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        parser.init();
        Iterator iter = Arrays.asList(arguments).iterator();
        parser.gobble(iter);
        assertEquals(2, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("f", "file", true, "File option");
        options.addOption(option);
        parser.currentOption = option;
        parser.process("test.txt");
        assertEquals(1, parser.tokens.size());
        assertEquals("test.txt", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Verbose option");
        options.addOption(option);
        boolean stopAtNonOption = false;
        parser.processOptionToken("-v", stopAtNonOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-v", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("f", "file", true, "File option");
        options.addOption(option);
        boolean stopAtNonOption = false;
        String token = "-fvalue";
        parser.burstToken(token, stopAtNonOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
}