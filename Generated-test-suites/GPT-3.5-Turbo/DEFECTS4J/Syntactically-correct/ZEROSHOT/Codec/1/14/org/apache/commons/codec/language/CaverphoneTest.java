import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Expected result for an empty string should be "1111111111"
        String result1 = caverphone.caverphone("");
        assertEquals("1111111111", result1);
        
        // Expected result for "Hello" should be "HAA1111111"
        String result2 = caverphone.caverphone("Hello");
        assertEquals("HAA1111111", result2);
        
        // Expected result for "Apache" should be "APHX111111"
        String result3 = caverphone.caverphone("Apache");
        assertEquals("APHX111111", result3);
        
        // Expected result for "Testing" should be "TAZYN11111"
        String result4 = caverphone.caverphone("Testing");
        assertEquals("TAZYN11111", result4);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Expected result for an empty string should be "1111111111"
        Object result1 = caverphone.encode("");
        assertEquals("1111111111", result1);
        
        // Expected result for "Hello" should be "HAA1111111"
        Object result2 = caverphone.encode("Hello");
        assertEquals("HAA1111111", result2);
        
        // Expected result for "Apache" should be "APHX111111"
        Object result3 = caverphone.encode("Apache");
        assertEquals("APHX111111", result3);
        
        // Expected result for "Testing" should be "TAZYN11111"
        Object result4 = caverphone.encode("Testing");
        assertEquals("TAZYN11111", result4);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Two empty strings should have the same caverphone value
        boolean result1 = caverphone.isCaverphoneEqual("", "");
        assertTrue(result1);
        
        // "Hello" and "Hello" should have the same caverphone value
        boolean result2 = caverphone.isCaverphoneEqual("Hello", "Hello");
        assertTrue(result2);
        
        // "Apache" and "Apacha" should have different caverphone values
        boolean result3 = caverphone.isCaverphoneEqual("Apache", "Apacha");
        assertFalse(result3);
        
        // "Testing" and "Test" should have different caverphone values
        boolean result4 = caverphone.isCaverphoneEqual("Testing", "Test");
        assertFalse(result4);
    }

}