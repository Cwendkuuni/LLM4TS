import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }
    
    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("A");
        assertEquals("A", result);
    }
    
    @Test
    public void testMetaphoneWord() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("Hello");
        assertEquals("HL", result);
    }
    
    @Test
    public void testEncodeEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.encode("");
        assertEquals("", result);
    }
    
    @Test
    public void testEncodeNonStringObject() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode(123);
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.encode("Hello");
        assertEquals("HL", result);
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("Hello", "Hallo");
        assertFalse(result);
        
        result = metaphone.isMetaphoneEqual("Hello", "Hello");
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
        metaphone.setMaxCodeLen(3);
        int result = metaphone.getMaxCodeLen();
        assertEquals(3, result);
    }

}