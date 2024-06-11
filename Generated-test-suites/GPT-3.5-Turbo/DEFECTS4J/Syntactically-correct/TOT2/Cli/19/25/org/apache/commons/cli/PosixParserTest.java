import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("v", "verbose", false, "Verbose option");
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-f", "test.txt", "-v", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("-f", result[0]);
        assertEquals("test.txt", result[1]);
        assertEquals("-v", result[2]);
        assertEquals("--", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }

    @Test
    public void testInit() {
        parser.init();

        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        parser.processOptionToken("-f", false);

        assertNotNull(parser.currentOption);
        assertEquals("-f", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        parser.burstToken("-fv", false);

        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));
        assertEquals("-v", parser.tokens.get(1));
    }
}