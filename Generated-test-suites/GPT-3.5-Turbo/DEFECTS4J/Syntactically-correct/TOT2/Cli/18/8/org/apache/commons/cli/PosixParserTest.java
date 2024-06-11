import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        
        assertEquals(parser.eatTheRest, false);
        assertEquals(parser.tokens.size(), 0);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-a", "arg", "--option2=value2"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(flattened.length, 5);
        assertEquals(flattened[0], "--option1");
        assertEquals(flattened[1], "value1");
        assertEquals(flattened[2], "-a");
        assertEquals(flattened[3], "arg");
        assertEquals(flattened[4], "--option2=value2");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("value1", "value2").iterator();
        parser.gobble(iter);

        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "value1");
        assertEquals(parser.tokens.get(1), "value2");
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");

        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-");
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.process("value");

        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.get(1), "value");
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "aaa", false, "AAA option");
        options.addOption("b", "bbb", true, "BBB option");
        String token = "-a";
        
        parser.processOptionToken(token, false);
        
        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.currentOption.getOpt(), "a");
        
        token = "-c";
        parser.processOptionToken(token, false);
        
        assertEquals(parser.eatTheRest, true);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "aaa", false, "AAA option");
        options.addOption("b", "bbb", true, "BBB option");
        String token = "-abc";
        
        parser.burstToken(token, false);

        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-abc");
    }
}