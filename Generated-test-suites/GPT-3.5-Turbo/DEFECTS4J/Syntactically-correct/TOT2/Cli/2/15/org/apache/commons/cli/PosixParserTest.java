import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.getTokens().size());
    }

    @Test
    public void testFlatten() {
        // create test cases for flatten method
    }

    @Test
    public void testGobble() {
        // create test cases for gobble method
    }

    @Test
    public void testProcess() {
        // create test cases for process method
    }

    @Test
    public void testProcessSingleHyphen() {
        // create test cases for processSingleHyphen method
    }

    @Test
    public void testProcessOptionToken() {
        // create test cases for processOptionToken method
    }

    @Test
    public void testBurstToken() {
        // create test cases for burstToken method
    }
}