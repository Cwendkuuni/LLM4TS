import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    //Test for metaphone method
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals(metaphone.metaphone("hello"), "HL");
        assertEquals(metaphone.metaphone("world"), "WRL");
        assertEquals(metaphone.metaphone(""), "");
    }

    //Test for encode method
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals(metaphone.encode("hello"), "HL");
        assertEquals(metaphone.encode("world"), "WRL");
        assertEquals(metaphone.encode(""), "");
    }

    //Test for isMetaphoneEqual method
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("hello", "HL"));
        assertFalse(metaphone.isMetaphoneEqual("hello", "WRL"));
    }

    //Test for getMaxCodeLen method
    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(metaphone.getMaxCodeLen(), 4);
    }

    //Test for setMaxCodeLen method
    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(10);
        assertEquals(metaphone.getMaxCodeLen(), 10);
    }

}