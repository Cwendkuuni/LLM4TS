import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone code = new Metaphone();
        
        // Typical Use Cases
        assertEquals("MTFN", code.metaphone("Metaphone"));
        assertEquals("SRC", code.metaphone("Source"));
        assertEquals("HLLO", code.metaphone("Hello"));
        
        // Edge Cases
        assertEquals("A", code.metaphone("A"));
        assertEquals("", code.metaphone(""));
        assertEquals("A", code.metaphone("aa"));
        assertEquals("MTFN", code.metaphone("METAPHONE"));
        assertEquals("SRC", code.metaphone("source"));
        
        // Error Scenarios
        assertNull(code.metaphone(null));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Metaphone code = new Metaphone();
        
        // Typical Use Cases
        assertEquals("MTFN", code.encode("Metaphone"));
        assertEquals("SRC", code.encode("Source"));
        assertEquals("HLLO", code.encode("Hello"));
        
        // Edge Cases
        assertEquals("A", code.encode("A"));
        assertEquals("", code.encode(""));
        
        // Error Scenarios
        try {
            code.encode(123);
            fail("EncoderException was not thrown");
        } catch (EncoderException e) {
            // Exception was thrown as expected
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone code = new Metaphone();
        
        // Typical Use Cases
        assertTrue(code.isMetaphoneEqual("hello", "helo"));
        assertTrue(code.isMetaphoneEqual("Metaphone", "METAFOAN"));
        
        // Edge Cases
        assertTrue(code.isMetaphoneEqual("A", "a"));
        assertFalse(code.isMetaphoneEqual("", "abc"));
        
        // Error Scenarios
        assertFalse(code.isMetaphoneEqual(null, "hello"));
        assertFalse(code.isMetaphoneEqual("hello", null));
        assertFalse(code.isMetaphoneEqual(null, null));
    }
    
    @Test
    public void testGetMaxCodeLen() {
        Metaphone code = new Metaphone();
        
        // Typical Use Case
        assertEquals(4, code.getMaxCodeLen());
    }
    
    @Test
    public void testSetMaxCodeLen() {
        Metaphone code = new Metaphone();
        
        // Typical Use Case
        code.setMaxCodeLen(6);
        assertEquals(6, code.getMaxCodeLen());
    }
}