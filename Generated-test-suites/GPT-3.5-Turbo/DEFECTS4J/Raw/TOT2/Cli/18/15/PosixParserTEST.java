import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        List<String> tokens = parser.getTokens();
        assertTrue(tokens.isEmpty());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Test Option A");
        options.addOption("b", true, "Test Option B");
        parser.flatten(options, new String[]{"-a", "valueA", "-b", "valueB"}, true);
        List<String> tokens = parser.getTokens();
        assertEquals(4, tokens.size());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Test Option A");
        options.addOption("b", true, "Test Option B");
        parser.flatten(options, new String[]{"-ab"}, true);
        List<String> tokens = parser.getTokens();
        assertEquals(2, tokens.size());
    }
}