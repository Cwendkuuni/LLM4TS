import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value", "--option1", "--option2=value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
        assertEquals("--option1", result[4]);
        assertEquals("--option2", result[5]);
        assertEquals("value", result[6]);
    }

    @Test
    public void testProcessNonOptionToken() {
        parser.processNonOptionToken("value");

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        parser.processOptionToken("-a", true);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        parser.options = options;

        parser.burstToken("-ab");

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }
}