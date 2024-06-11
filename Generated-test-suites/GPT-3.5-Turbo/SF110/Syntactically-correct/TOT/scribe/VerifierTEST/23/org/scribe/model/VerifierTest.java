import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        assertEquals("test", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueConstructor() {
        Verifier verifier = new Verifier(null);
    }

    @Test
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }
    
    @Test
    public void testLongValue() {
        String longValue = "This is a very long value that exceeds the typical length of verifiers";
        Verifier verifier = new Verifier(longValue);
        assertEquals(longValue, verifier.getValue());
    }

    @Test
    public void testSpecialCharactersValue() {
        String value = "&^%$#@!";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testWhiteSpaceValue() {
        String value = " ";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}