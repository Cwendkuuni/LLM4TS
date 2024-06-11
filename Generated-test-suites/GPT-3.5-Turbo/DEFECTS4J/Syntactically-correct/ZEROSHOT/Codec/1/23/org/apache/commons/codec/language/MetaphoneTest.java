import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {
    
    private Metaphone metaphone;
    
    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }
    
    @Test
    public void testMetaphone() {
        String result = metaphone.metaphone("Apache");
        assertEquals("APK", result);
    }
    
    @Test
    public void testEncode() {
        Object result = metaphone.encode("Apache");
        assertEquals("APK", result);
    }
    
    @Test
    public void testEncodeInvalidObject() {
        try {
            metaphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        boolean result = metaphone.isMetaphoneEqual("Apache", "Appache");
        assertTrue(result);
    }
    
    @Test
    public void testIsMetaphoneEqualNotEqual() {
        boolean result = metaphone.isMetaphoneEqual("Apache", "Apple");
        assertFalse(result);
    }
    
    @Test
    public void testGetMaxCodeLen() {
        int result = metaphone.getMaxCodeLen();
        assertEquals(4, result);
    }
    
    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        int result = metaphone.getMaxCodeLen();
        assertEquals(6, result);
    }
}