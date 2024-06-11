import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("test");

        assertEquals(true, parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("test", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        Option option = new Option("a");

        parser.options.addOption(option);

        parser.processOptionToken("-a", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        Option option = new Option("a");
        option.setArgs(1);

        parser.options.addOption(option);

        parser.burstToken("-abc", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("bc", parser.tokens.get(1));
    }
}