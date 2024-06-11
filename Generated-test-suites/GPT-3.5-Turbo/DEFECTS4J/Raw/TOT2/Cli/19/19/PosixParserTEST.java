import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertTrue(parser.getTokens().isEmpty());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);
        
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "fruit"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("fruit", flattened[1]);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);
        
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);
        assertNotNull(parser.getCurrentOption());
        assertEquals(option, parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);
        
        PosixParser parser = new PosixParser();
        parser.burstToken("-apple", false);
        assertNotNull(parser.getCurrentOption());
        assertEquals(option, parser.getCurrentOption());
    }
}