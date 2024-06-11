import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("3ll", caverphone.caverphone("Hello"));
        assertEquals("2tt", caverphone.caverphone("att"));
        assertEquals("kns", caverphone.caverphone("kings"));
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("3ll", caverphone.encode("Hello"));
        assertEquals("2tt", caverphone.encode("att"));
        assertEquals("kns", caverphone.encode("kings"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode((Object) ""));
        assertEquals("3ll", caverphone.encode((Object) "Hello"));
        assertEquals("2tt", caverphone.encode((Object) "att"));
        assertEquals("kns", caverphone.encode((Object) "kings"));
        
        // Test for exception handling
        try {
            caverphone.encode((Object) 123); // This should throw an EncoderException
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertTrue(caverphone.isCaverphoneEqual("Hello", "Hello"));
        assertFalse(caverphone.isCaverphoneEqual("Hello", "World"));
        assertFalse(caverphone.isCaverphoneEqual("Hello", "hEllo"));
    }
}