import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertEquals(parser.eatTheRest, false);
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", "test option");
        options.addOption(option);
        parser.options = options;

        parser.processOptionToken("-a", true);
        assertEquals(parser.currentOption, option);
        assertEquals(parser.tokens.get(0), "-a");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", "test option");
        parser.process("value");
        assertNull(parser.currentOption);
        assertEquals(parser.eatTheRest, true);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.get(1), "value");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", "test option");
        options.addOption(option);
        parser.options = options;

        parser.burstToken("-aarg", false);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.tokens.get(1), "arg");
    }
}