import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.processOptionToken("-f", false);
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-f");
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.process("value");
        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.get(1), "value");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.burstToken("-abc", false);
        assertEquals(parser.tokens.size(), 3);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.tokens.get(1), "-b");
        assertEquals(parser.tokens.get(2), "-c");
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("f", false, "dummy option");
        options.addOption(option);
        
        String[] arguments = { "-f", "value" };
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        
        assertEquals(flattenedArguments.length, 2);
        assertEquals(flattenedArguments[0], "-f");
        assertEquals(flattenedArguments[1], "value");
    }

}