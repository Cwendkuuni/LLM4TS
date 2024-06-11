import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", "custom", false, "Option c");
        String[] arguments = {"-a", "valueA", "--", "-b", "valueB", "-c=customValue", "-d"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertEquals(6, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("valueA", flattened[1]);
        assertEquals("--", flattened[2]);
        assertEquals("-b", flattened[3]);
        assertEquals("valueB", flattened[4]);
        assertEquals("-c=customValue", flattened[5]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"test1", "test2", "test3"}).iterator();
        parser.gobble(iter);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.process("value");
        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
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
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        parser.options = options;
        parser.processOptionToken("a", true);
        assertNotNull(parser.currentOption);
        assertEquals("a", parser.currentOption.getOpt());
        assertEquals("Option a", parser.currentOption.getDescription());
        assertEquals(1, parser.tokens.size());
        assertEquals("a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
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