import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testInit() {
        parser.init();
        assertTrue(parser.tokens.isEmpty());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionTokenWithValidOption() {
        Options options = new Options();
        options.addOption(new Option("a", "option A"));
        parser.flatten(options, new String[] { "-a" }, false);
        assertEquals("a", parser.currentOption.getOpt());
    }

    @Test
    public void testProcessOptionTokenWithInvalidOptionAndStopAtNonOption() {
        Options options = new Options();
        parser.flatten(options, new String[] { "-x" }, true);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessOptionTokenWithInvalidOptionAndNoStopAtNonOption() {
        Options options = new Options();
        parser.flatten(options, new String[] { "-z" }, false);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testProcessSpecialToken() {
        parser.flatten(new Options(), new String[] { "--option=value" }, true);
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testProcessSingleHyphenToken() {
        parser.flatten(new Options(), new String[] { "-" }, true);
        assertEquals("-", parser.tokens.get(0));
    }

}