import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Test empty string
        assertEquals("", metaphone.metaphone(""));
        
        // Test single character
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("M", metaphone.metaphone("m"));
        
        // Test initial 2 character exceptions
        assertEquals("N", metaphone.metaphone("kn"));
        assertEquals("AE", metaphone.metaphone("ae"));
        assertEquals("W", metaphone.metaphone("wh"));
        
        // Test X becomes S
        assertEquals("S", metaphone.metaphone("x"));
        
        // Test other cases
        assertEquals("S", metaphone.metaphone("sss"));
        assertEquals("SSKSF", metaphone.metaphone("sksfv"));
        assertEquals("S", metaphone.metaphone("s"));
        assertEquals("JN", metaphone.metaphone("jng"));
        assertEquals("ZW", metaphone.metaphone("zw"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Test empty string
        try {
            metaphone.encode("");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", e.getMessage());
        }
        
        // Test normal string
        assertEquals("SS", metaphone.encode("ss"));
        
        // Test null string
        try {
            metaphone.encode(null);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Test equal strings
        assertTrue(metaphone.isMetaphoneEqual("test", "test"));
        
        // Test different strings
        assertFalse(metaphone.isMetaphoneEqual("test", "taste"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Test default maxCodeLen
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Test set maxCodeLen
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Test set maxCodeLen
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

}