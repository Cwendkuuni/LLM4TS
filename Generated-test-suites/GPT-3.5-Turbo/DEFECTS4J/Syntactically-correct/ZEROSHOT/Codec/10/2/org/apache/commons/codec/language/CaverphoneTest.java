import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test lowercase conversion and removal of non-alphabetic characters
        assertEquals("abcdef", caverphone.caverphone("aBCD@%EF123"));
        
        // Test removal of final e
        assertEquals("Hello", caverphone.caverphone("Helloe"));
        
        // Test various start options
        assertEquals("cou2fa", caverphone.caverphone("cough"));
        assertEquals("rou2fa", caverphone.caverphone("rough"));
        assertEquals("tou2fa", caverphone.caverphone("tough"));
        assertEquals("enou2fa", caverphone.caverphone("enough"));
        assertEquals("trou2fa", caverphone.caverphone("trough"));
        assertEquals("2nf", caverphone.caverphone("gn"));
        
        // Test end replacement
        assertEquals("m2", caverphone.caverphone("mb"));
        
        // Test replacements
        assertEquals("2q", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        
        // Add more test cases for other replacements
        
        // Test caverphone code length
        assertEquals(10, caverphone.caverphone("SomeString").length());
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test encode method with an object
        assertEquals("caverphonecode", caverphone.encode((Object) "CaverphoneCode"));
        
        // Test encode method with a string
        assertEquals("caverphonecode", caverphone.encode("CaverphoneCode"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test identical caverphones
        assertTrue(caverphone.isCaverphoneEqual("hello", "Hello"));
        
        // Test different caverphones
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }
}