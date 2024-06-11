import static org.junit.Assert.*;
import org.junit.Test;

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
        // Test cases to be added here
    }

    @Test
    public void testGobble() {
        // Test cases to be added here
    }

    @Test
    public void testProcess() {
        // Test cases to be added here
    }

    @Test
    public void testProcessOptionToken() {
        // Test cases to be added here
    }

    @Test
    public void testBurstToken() {
        // Test cases to be added here
    }
}