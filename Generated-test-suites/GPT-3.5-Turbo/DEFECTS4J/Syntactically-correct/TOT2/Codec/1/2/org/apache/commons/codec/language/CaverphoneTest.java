import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    // Test caverphone method with typical input
    @Test
    public void testCaverphoneTypicalInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("Expected caverphone code not matched", "HL11111", result);
    }

    // Test caverphone method with edge case input
    @Test
    public void testCaverphoneEdgeCaseInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(",");
        assertEquals("Expected caverphone code not matched", "1111111111", result);
    }

    // Test encode method with typical input
    @Test
    public void testEncodeTypicalInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("world");
        assertTrue(result instanceof String);
        assertEquals("Expected encoded string not matched", "WALT111111", result);
    }

    // Test encode method with incorrect input type
    @Test(expected = EncoderException.class)
    public void testEncodeIncorrectInputType() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    // Test isCaverphoneEqual method with typical inputs
    @Test
    public void testIsCaverphoneEqualTypicalInputs() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "HELLO");
        assertTrue(result);
    }

    // Test isCaverphoneEqual method with different caverphones
    @Test
    public void testIsCaverphoneEqualDifferentCaverphones() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }

}