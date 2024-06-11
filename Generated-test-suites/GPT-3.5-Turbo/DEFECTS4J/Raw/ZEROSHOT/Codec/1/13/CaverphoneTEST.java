import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test string with no special characters
        assertEquals("1111111111", caverphone.caverphone("hello"));
        
        // Test string with special characters
        assertEquals("1111111111", caverphone.caverphone("hello!@#$%^&*()"));
        
        // Test string with uppercase characters
        assertEquals("hk11111111", caverphone.caverphone("HelloWorld"));
        
        // Test string with replacements
        assertEquals("2211k11111", caverphone.caverphone("cqci"));
        assertEquals("2211k11111", caverphone.caverphone("CQCI"));
        
        // Add more test cases...
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test encoding object of type String
        assertEquals("1111111111", caverphone.encode((Object) "hello"));
        
        // Test encoding object of other type
        try {
            caverphone.encode((Object) 123);
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            // EncoderException thrown as expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test equality of caverphone codes
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }
}