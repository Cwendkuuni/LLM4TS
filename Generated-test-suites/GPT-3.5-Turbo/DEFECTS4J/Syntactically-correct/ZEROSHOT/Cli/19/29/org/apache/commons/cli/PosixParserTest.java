import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(false, parser.isEatTheRest());
        assertEquals(0, parser.getTokens().size());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        Option option1 = new Option("a", false, "");
        Option option2 = new Option("b", false, "");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        String[] arguments = {"-a", "--", "-b"};
        
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);
        
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--", flattened[1]);
        assertEquals("-b", flattened[2]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.setEatTheRest(true);
        parser.getTokens().add("token1");
        parser.getTokens().add("token2");
        
        String[] remainingTokens = {"token3", "token4"};
        Iterator<String> iter = Arrays.asList(remainingTokens).iterator();
        
        parser.gobble(iter);
        
        assertEquals(4, parser.getTokens().size());
        assertEquals("token1", parser.getTokens().get(0));
        assertEquals("token2", parser.getTokens().get(1));
        assertEquals("token3", parser.getTokens().get(2));
        assertEquals("token4", parser.getTokens().get(3));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Option option = new Option("a", true, "");
        parser.setCurrentOption(option);
        
        parser.process("value");
        
        assertEquals(1, parser.getTokens().size());
        assertEquals("value", parser.getTokens().get(0));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionToken() {
        Option option = new Option("a", false, "");
        Options options = new Options();
        options.addOption(option);
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        
        parser.processOptionToken("-a", true);
        
        assertEquals(1, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals(option, parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        Option option = new Option("a", false, "");
        Options options = new Options();
        options.addOption(option);
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        
        parser.burstToken("-ab", true);
        
        assertEquals(2, parser.getTokens().size());
        assertEquals("-a", parser.getTokens().get(0));
        assertEquals("b", parser.getTokens().get(1));
        assertNull(parser.getCurrentOption());
    }

}