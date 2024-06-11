import org.junit.*;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        String txt = null;
        String expected = "1111111111";
        String result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "";
        expected = "1111111111";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "Test";
        expected = "1111111111";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "cough";
        expected = "AF";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "rough";
        expected = "R2F";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "tough";
        expected = "T2F";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "enough";
        expected = "3NUF";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "trough";
        expected = "TR2F";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
        
        txt = "gnash";
        expected = "2N1";
        result = caverphone.caverphone(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        String txt = "Test";
        String expected = "1111111111";
        String result = caverphone.encode(txt);
        assertEquals(expected, result);
        
        Object obj = "Test";
        expected = "1111111111";
        result = (String) caverphone.encode(obj);
        assertEquals(expected, result);
        
        obj = new StringBuilder("Test");
        try {
            result = (String) caverphone.encode(obj);
        } catch (EncoderException e) {
            assertTrue(true);
        }
        
        txt = null;
        expected = "1111111111";
        result = caverphone.encode(txt);
        assertEquals(expected, result);
        
        txt = "";
        expected = "1111111111";
        result = caverphone.encode(txt);
        assertEquals(expected, result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        String str1 = "Test";
        String str2 = "Test";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
        
        str1 = "Hello";
        str2 = "World";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
        
        str1 = "";
        str2 = "";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
        
        str1 = null;
        str2 = null;
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }
}