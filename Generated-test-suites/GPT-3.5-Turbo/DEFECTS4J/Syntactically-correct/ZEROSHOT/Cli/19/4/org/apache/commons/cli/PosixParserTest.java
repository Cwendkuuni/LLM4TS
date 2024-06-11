import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        
        // specify options
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").hasArg().build());
        
        // test with stopAtNonOption set to true
        String[] arguments1 = {"-a", "-b", "-c", "value", "nonOption"};
        String[] expected1 = {"-a", "-b", "-c", "value"};
        assertArrayEquals(expected1, parser.flatten(options, arguments1, true));
        
        // test with stopAtNonOption set to false
        String[] arguments2 = {"-a", "-b", "-c", "value", "nonOption"};
        String[] expected2 = {"-a", "-b", "-c", "value", "nonOption"};
        assertArrayEquals(expected2, parser.flatten(options, arguments2, false));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        
        // specify options
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        // test with option exists
        parser.processOptionToken("-a", false);
        assertNotNull(parser.getCurrentOption());
        Option currentOption = options.getOption("a");
        assertEquals(currentOption, parser.getCurrentOption());
        
        // test with option does not exist and stopAtNonOption is true
        parser.processOptionToken("-c", true);
        assertNull(parser.getCurrentOption());
        assertTrue(parser.isEatTheRest());
        
        // test with option does not exist and stopAtNonOption is false
        parser.processOptionToken("-c", false);
        assertNull(parser.getCurrentOption());
        assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        
        // specify options
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        
        // test with option exists and has argument
        String token1 = "-bvalue";
        parser.burstToken(token1, false);
        assertNull(parser.getCurrentOption());
        assertEquals(2, parser.getTokens().size());
        assertEquals("-b", parser.getTokens().get(0));
        assertEquals("value", parser.getTokens().get(1));
        
        // test with option exists and does not have argument
        String token2 = "-a";
        parser.burstToken(token2, false);
        assertNull(parser.getCurrentOption());
        assertEquals(3, parser.getTokens().size());
        assertEquals("-b", parser.getTokens().get(0));
        assertEquals("value", parser.getTokens().get(1));
        assertEquals("-a", parser.getTokens().get(2));
        
        // test with option does not exist and stopAtNonOption is true
        String token3 = "-cvalue";
        parser.burstToken(token3, true);
        assertNull(parser.getCurrentOption());
        assertTrue(parser.isEatTheRest());
        assertEquals(4, parser.getTokens().size());
        assertEquals("-b", parser.getTokens().get(0));
        assertEquals("value", parser.getTokens().get(1));
        assertEquals("-a", parser.getTokens().get(2));
        assertEquals("-cvalue", parser.getTokens().get(3));
        
        // test with option does not exist and stopAtNonOption is false
        String token4 = "-cvalue";
        parser.burstToken(token4, false);
        assertNull(parser.getCurrentOption());
        assertFalse(parser.isEatTheRest());
        assertEquals(4, parser.getTokens().size());
        assertEquals("-b", parser.getTokens().get(0));
        assertEquals("value", parser.getTokens().get(1));
        assertEquals("-a", parser.getTokens().get(2));
        assertEquals("-cvalue", parser.getTokens().get(3));
    }
}