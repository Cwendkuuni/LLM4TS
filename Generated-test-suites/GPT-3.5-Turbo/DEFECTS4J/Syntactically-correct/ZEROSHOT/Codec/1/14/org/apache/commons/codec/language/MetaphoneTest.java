import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("hello");
        assertEquals("HL", result);
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        String result = (String) metaphone.encode("hello");
        assertEquals("HL", result);
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.encode("hello");
        assertEquals("HL", result);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("hello", "hi");
        assertTrue(result);
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int result = metaphone.getMaxCodeLen();
        assertEquals(4, result);
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(6);
        int result = metaphone.getMaxCodeLen();
        assertEquals(6, result);
    }
}