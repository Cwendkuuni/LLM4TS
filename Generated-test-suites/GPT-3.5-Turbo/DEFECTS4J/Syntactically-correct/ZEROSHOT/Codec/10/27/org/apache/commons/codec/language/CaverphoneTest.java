import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test with null input
        assertEquals("1111111111", caverphone.caverphone(null));

        // Test with empty input
        assertEquals("1111111111", caverphone.caverphone(""));

        // Test with lowercase input
        assertEquals("kafa111111", caverphone.caverphone("kafka"));

        // Test with uppercase input
        assertEquals("kafa111111", caverphone.caverphone("KAFKA"));

        // Test with special characters input
        assertEquals("ldor1111111", caverphone.caverphone("lörëm dïpsüm"));

        // Test with input containing only non-alphabetic characters
        assertEquals("1111111111", caverphone.caverphone("1234567890!@#$%^&*()"));

        // Add more test cases ...
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Test with String input
        assertEquals("kafa111111", caverphone.encode("kafka"));

        // Test with non-String input
        try {
            caverphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception, do nothing
        }

        // Add more test cases ...
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test with two identical strings
        assertTrue(caverphone.isCaverphoneEqual("kafka", "kafka"));

        // Test with two different strings
        assertFalse(caverphone.isCaverphoneEqual("kafka", "kava"));

        // Add more test cases ...
    }
}