import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    // Initialize PosixParser instance
    PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        // Test flatten method with stopAtNonOption true
        String[] arguments = {"-a", "arg1", "--opt1", "-b", "arg2"};
        String[] result = posixParser.flatten(new Options(), arguments, true);
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--opt1", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        // Test flatten method without stopAtNonOption
        String[] arguments = {"-a", "arg1", "--opt1", "-b", "arg2"};
        String[] result = posixParser.flatten(new Options(), arguments, false);
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--opt1", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        // Test burstToken method with stopAtNonOption true
        String token = "-ab";
        String[] options = {"-a", "-b"};
        posixParser.burstToken(token, true);
        assertArrayEquals(options, posixParser.getTokens().toArray());
    }

    @Test
    public void testBurstTokenWithoutStopAtNonOption() {
        // Test burstToken method without stopAtNonOption
        String token = "-ab";
        String[] options = {"-a", "-b"};
        posixParser.burstToken(token, false);
        assertArrayEquals(options, posixParser.getTokens().toArray());
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        // Test processOptionToken method with stopAtNonOption true
        String token = "--opt";
        posixParser.processOptionToken(token, true);
        assertTrue(posixParser.getEatTheRest());
    }

    @Test
    public void testProcessOptionTokenWithoutStopAtNonOption() {
        // Test processOptionToken method without stopAtNonOption
        String token = "--opt";
        posixParser.processOptionToken(token, false);
        assertFalse(posixParser.getEatTheRest());
    }

    @Test
    public void testProcessNonOptionToken() {
        // Test processNonOptionToken method
        String value = "value";
        posixParser.processNonOptionToken(value);
        assertTrue(posixParser.getEatTheRest());
        assertEquals("--", posixParser.getTokens().get(0));
        assertEquals(value, posixParser.getTokens().get(1));
    }

}