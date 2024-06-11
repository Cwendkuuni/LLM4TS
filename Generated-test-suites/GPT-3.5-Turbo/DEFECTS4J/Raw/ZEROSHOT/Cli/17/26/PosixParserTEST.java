import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "valueA", "-b", "valueB", "arg1", "arg2"};
        String[] flattened = parser.flatten(options, arguments, false);
        
        assertEquals(6, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("valueA", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("valueB", flattened[3]);
        assertEquals("arg1", flattened[4]);
        assertEquals("arg2", flattened[5]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();

        String[] tokens1 = {"arg1", "arg2"};
        Iterator<String> iter1 = Arrays.asList(tokens1).iterator();
        parser.tokens.add("token1");
        parser.tokens.add("token2");

        parser.gobble(iter1);
        assertEquals(4, parser.tokens.size());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));

        String[] tokens2 = {"arg1", "arg2"};
        Iterator<String> iter2 = Arrays.asList(tokens2).iterator();
        parser.tokens.clear();

        parser.eatTheRest = true;
        parser.gobble(iter2);
        assertEquals(2, parser.tokens.size());
        assertEquals("arg1", parser.tokens.get(0));
        assertEquals("arg2", parser.tokens.get(1));
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");

        PosixParser parser = new PosixParser();
        parser.currentOption = options.getOption("a");

        parser.process("valueA");
        assertEquals(1, parser.tokens.size());
        assertEquals("valueA", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.currentOption = options.getOption("b");
        parser.process("valueB");
        assertEquals(1, parser.tokens.size());
        assertEquals("valueB", parser.tokens.get(0));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();

        parser.processSingleHyphen("-");
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));

        parser.processSingleHyphen("--");
        assertEquals(2, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
        assertEquals("-", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.processOptionToken("-a", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
        assertEquals(options.getOption("a"), parser.currentOption);

        parser.tokens.clear();
        parser.currentOption = null;
        parser.processOptionToken("-b", true);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);

        parser.tokens.clear();
        parser.currentOption = null;
        parser.processOptionToken("-b", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-b", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-ab", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-ab", parser.tokens.get(0));

        parser.tokens.clear();
        parser.burstToken("-ab", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.tokens.clear();
        parser.burstToken("-a", true);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));

        parser.tokens.clear();
        parser.burstToken("-a", false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }
}