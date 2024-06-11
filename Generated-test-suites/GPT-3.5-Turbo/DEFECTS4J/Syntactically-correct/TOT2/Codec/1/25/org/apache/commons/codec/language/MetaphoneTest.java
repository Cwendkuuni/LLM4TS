import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KK", metaphone.metaphone("cake"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals("FL", metaphone.encode("flower"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("flower", "flour");
        assertEquals(true, result);
    }

    @Test
    public void testEdgeCases() {
        Metaphone metaphone = new Metaphone();
        // Testing single character input
        assertEquals("A", metaphone.metaphone("a"));
        // Testing maximum code length
        int maxCodeLen = 8;
        metaphone.setMaxCodeLen(maxCodeLen);
        assertEquals(maxCodeLen, metaphone.getMaxCodeLen());
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(123); // Trying to encode a non-string object
    }

}