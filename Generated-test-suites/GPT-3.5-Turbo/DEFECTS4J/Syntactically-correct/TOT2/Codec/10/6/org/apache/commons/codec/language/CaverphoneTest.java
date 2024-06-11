import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {

    // Test caverphone method with a simple input
    @Test
    public void testCaverphoneSimpleInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("Expected caverphone code for 'hello'", "H1111111111", result);
    }

    // Test caverphone method with null input
    @Test
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("Expected caverphone code for null input", "1111111111", result);
    }

    // Test encode method with string input
    @Test
    public void testEncodeStringInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("world");
        assertEquals("Expected encoded caverphone code for 'world'", "W1111111111", result);
    }

    // Test encode method with non-string input
    @Test(expected = EncoderException.class)
    public void testEncodeNonStringInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(12345);
    }

    // Test isCaverphoneEqual method with equal caverphones
    @Test
    public void testIsCaverphoneEqualTrue() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "hallow");
        assertTrue("Expected caverphones to be equal", result);
    }

    // Test isCaverphoneEqual method with unequal caverphones
    @Test
    public void testIsCaverphoneEqualFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("world", "giraffe");
        assertFalse("Expected caverphones to be unequal", result);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}