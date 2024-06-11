import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        String inputText = "hello";
        String expectedOutput = "HL11111111";
        assertEquals(expectedOutput, caverphone.caverphone(inputText));
        
        // Edge case: empty input
        inputText = "";
        expectedOutput = "1111111111";
        assertEquals(expectedOutput, caverphone.caverphone(inputText));
        
        // Error scenario: null input
        inputText = null;
        expectedOutput = "1111111111";
        assertEquals(expectedOutput, caverphone.caverphone(inputText));
    }
    
    @Test
    public void testEncode_Object() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        String inputText = "hello";
        String expectedOutput = "HL11111111";
        assertEquals(expectedOutput, caverphone.encode(inputText));
        
        // Error scenario: non-String object
        Object nonStringObject = new Object();
        try {
            caverphone.encode(nonStringObject);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        
        String inputText = "hello";
        String expectedOutput = "HL11111111";
        assertEquals(expectedOutput, caverphone.encode(inputText));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        String inputText1 = "hello";
        String inputText2 = "HELLO";
        assertTrue(caverphone.isCaverphoneEqual(inputText1, inputText2));
        
        inputText1 = "hello";
        inputText2 = "world";
        assertFalse(caverphone.isCaverphoneEqual(inputText1, inputText2));
        
        // Edge case: empty input
        inputText1 = "";
        inputText2 = "";
        assertTrue(caverphone.isCaverphoneEqual(inputText1, inputText2));
        
        // Error scenario: null input
        inputText1 = null;
        inputText2 = null;
        assertTrue(caverphone.isCaverphoneEqual(inputText1, inputText2));
    }
}