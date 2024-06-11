import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class MetaphoneTest {

    // Test case for the metaphone method
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("hello");
        assertEquals("HL", result);
    }

    // Test case for the encode method
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        try {
            Object result = metaphone.encode("world");
            assertEquals("WRLT", result);
        } catch (EncoderException e) {
            fail("EncoderException occurred");
        }
    }

    // Test case for the encode method with invalid input
    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        Object result = metaphone.encode(123);
    }

    // Test case for the isMetaphoneEqual method
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("hello", "healo");
        assertTrue(result);
    }

}