import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone("caverphone");
        assertEquals("caverphone", result);
        
        result = caverphone.caverphone("apache");
        assertEquals("apache", result);
        
        result = caverphone.caverphone("java");
        assertEquals("java", result);
        
        result = caverphone.caverphone("test");
        assertEquals("test", result);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        Object result = caverphone.encode("caverphone");
        assertEquals("caverphone", result);
        
        result = caverphone.encode("apache");
        assertEquals("apache", result);
        
        result = caverphone.encode("java");
        assertEquals("java", result);
        
        result = caverphone.encode("test");
        assertEquals("test", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        boolean result = caverphone.isCaverphoneEqual("caverphone", "caverphone");
        assertTrue(result);
        
        result = caverphone.isCaverphoneEqual("apache", "java");
        assertFalse(result);
        
        result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
        
        result = caverphone.isCaverphoneEqual("caverphone", "apache");
        assertFalse(result);
    }

}