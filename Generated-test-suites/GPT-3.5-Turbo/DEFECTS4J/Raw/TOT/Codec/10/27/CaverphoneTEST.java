import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        String input1 = "hello";
        String expected1 = "H111111111";
        assertEquals(expected1, caverphone.caverphone(input1));
        
        // Edge case: empty string
        String input2 = "";
        String expected2 = "1111111111";
        assertEquals(expected2, caverphone.caverphone(input2));
        
        // Error scenario: null input
        String input3 = null;
        String expected3 = "1111111111";
        assertEquals(expected3, caverphone.caverphone(input3));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        String input1 = "hello";
        String expected1 = "H111111111";
        assertEquals(expected1, caverphone.encode(input1));
        
        // Error scenario: non-string input
        Object input2 = new Object();
        try {
            caverphone.encode(input2);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case: equal caverphones
        String input1 = "hello";
        String input2 = "hElLo";
        assertTrue(caverphone.isCaverphoneEqual(input1, input2));
        
        // Typical use case: different caverphones
        String input3 = "hello";
        String input4 = "world";
        assertFalse(caverphone.isCaverphoneEqual(input3, input4));
        
        // Edge case: empty strings
        String input5 = "";
        String input6 = "";
        assertTrue(caverphone.isCaverphoneEqual(input5, input6));
    }
}