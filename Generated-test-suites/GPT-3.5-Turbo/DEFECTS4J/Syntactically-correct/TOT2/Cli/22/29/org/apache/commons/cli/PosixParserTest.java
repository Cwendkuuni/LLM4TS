import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", "apple", false, "Include apple");
        options.addOption("b", "banana", true, "Include banana");
        parser = new PosixParser();
    }

    @Test
    public void testInit() {
        parser.init();
        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        String[] args = {"-a", "--apple", "-b", "value"};
        String[] flattenedArgs = parser.flatten(options, args, true);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("--apple", flattenedArgs[1]);
        assertEquals("-b", flattenedArgs[2]);
        assertEquals("value", flattenedArgs[3]);
    }

    @Test
    public void testProcessNonOptionToken() {
        parser.processNonOptionToken("-c");
        assertTrue(parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        parser.processOptionToken("-a", true);
        assertFalse(parser.eatTheRest);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        parser.burstToken("-af", false);
        assertEquals(4, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-f", parser.tokens.get(1));
        assertEquals("-a", parser.tokens.get(2));
        assertEquals("-f", parser.tokens.get(3));
    }
}