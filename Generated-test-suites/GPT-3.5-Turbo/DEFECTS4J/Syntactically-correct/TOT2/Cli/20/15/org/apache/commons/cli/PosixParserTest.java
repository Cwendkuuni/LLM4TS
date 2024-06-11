import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNotNull(parser.tokens);
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "value1", "--option", "-b", "--", "arg1"};
        String[] flattenedTokens = parser.flatten(options, arguments, false);
        assertNotNull(flattenedTokens);
        assertEquals(6, flattenedTokens.length);
        assertEquals("-a", flattenedTokens[0]);
        assertEquals("value1", flattenedTokens[1]);
        assertEquals("--option", flattenedTokens[2]);
        assertEquals("-b", flattenedTokens[3]);
        assertEquals("--", flattenedTokens[4]);
        assertEquals("arg1", flattenedTokens[5]);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option optionA = new Option("a", "option a");
        Option optionB = new Option("b", "option b");
        options.addOption(optionA);
        options.addOption(optionB);

        parser.burstToken("-ab", false);
        assertNotNull(parser.tokens);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));

        parser.tokens.clear();

        parser.burstToken("-barg", true);
        assertNotNull(parser.tokens);
        assertEquals(1, parser.tokens.size());
        assertEquals("-barg", parser.tokens.get(0));
    }
}