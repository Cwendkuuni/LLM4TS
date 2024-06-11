import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        // Test resetting members to their original state
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(0, parser.getTokens().size());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        // Test flattening command line arguments
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--optionB", "valueB", "-c", "valueC"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(5, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("--optionB", flattened[1]);
        assertEquals("valueB", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("valueC", flattened[4]);
    }

    @Test
    public void testGobble() {
        // Test adding remaining tokens to processed tokens list
        PosixParser parser = new PosixParser();
        parser.setEatTheRest(true);
        parser.getTokens().add("remaining");
        parser.gobble(Arrays.asList(new String[]{"token1", "token2"}).iterator());

        assertEquals(3, parser.getTokens().size());
        assertEquals("remaining", parser.getTokens().get(0));
        assertEquals("token1", parser.getTokens().get(1));
        assertEquals("token2", parser.getTokens().get(2));
    }

    @Test
    public void testProcess() {
        // Test processing token based on current option
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        Option optionA = options.getOption("a");
        parser.setCurrentOption(optionA);

        parser.process("value");

        assertEquals(1, parser.getTokens().size());
        assertEquals("value", parser.getTokens().get(0));
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionToken() {
        // Test processing option token
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        parser.processOptionToken("a", false);

        assertEquals(1, parser.getTokens().size());
        assertEquals("a", parser.getTokens().get(0));
        assertNotNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        // Test bursting token into constituent parts
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        parser.burstToken("-abc", true);

        assertEquals(1, parser.getTokens().size());
        assertEquals("-abc", parser.getTokens().get(0));
    }
}