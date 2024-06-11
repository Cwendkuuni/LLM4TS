import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        String[] arguments = {"-a", "valueA", "--b=valueB", "argC"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(result.length, 4);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "valueA");
        assertEquals(result[2], "--b");
        assertEquals(result[3], "valueB");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = new Option("a", false, "option a");
        parser.process("value");
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "value");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[] {"token1", "token2"}).iterator();
        parser.eatTheRest = true;
        parser.gobble(iter);
        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "token1");
        assertEquals(parser.tokens.get(1), "token2");
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-");
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        parser.options = options;
        parser.processOptionToken("a", true);
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "a");
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        parser.options = options;
        parser.burstToken("-abc", true);
        assertEquals(parser.tokens.size(), 3);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.tokens.get(1), "-b");
        assertEquals(parser.tokens.get(2), "c");
    }

}