import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testReset() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);
        assertNotNull(parser.currentOption);
        assertEquals("a", parser.currentOption.getOpt());
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        PosixParser parser = new PosixParser();
        parser.burstToken("-abc", false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-bc", parser.tokens.get(1));
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArgs().build());

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "valueA", "-b", "valueB1", "valueB2"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("valueA", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("valueB1", flattened[3]);
        assertEquals("valueB2", flattened[4]);
    }
}