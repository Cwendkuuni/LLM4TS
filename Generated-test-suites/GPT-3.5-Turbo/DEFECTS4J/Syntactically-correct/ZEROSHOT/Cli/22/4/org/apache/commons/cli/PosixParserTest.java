import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"--a", "--b=value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("--a", result[1]);
        assertEquals("--b=value", result[2]);
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
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        parser.options = options;

        parser.processOptionToken("-a", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        parser.options = options;

        parser.burstToken("-ab", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }

}