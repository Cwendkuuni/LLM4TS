import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test with empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        // Test with string containing only non-alphabetic characters
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()"));
        // Test with lowercase letters
        assertEquals("tmt1111111", caverphone.caverphone("test"));
        assertEquals("ct31101111", caverphone.caverphone("crazy"));
        // Test with uppercase letters
        assertEquals("tmt1111111", caverphone.caverphone("TEST"));
        assertEquals("ct31101111", caverphone.caverphone("CRAZY"));
        // Test with numbers
        assertEquals("ct31101111", caverphone.caverphone("craz7"));
        assertEquals("tmt1111111", caverphone.caverphone("t3st"));
        // Test with special characters
        assertEquals("ct31101111", caverphone.caverphone("craj^^"));
        assertEquals("tmt1111111", caverphone.caverphone("tes^t"));

        // Add more test cases to cover all possible scenarios

    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Test with empty string
        assertEquals("1111111111", caverphone.encode(""));
        // Test with string containing only non-alphabetic characters
        assertEquals("1111111111", caverphone.encode("!@#$%^&*()"));
        // Test with lowercase letters
        assertEquals("tmt1111111", caverphone.encode("test"));
        assertEquals("ct31101111", caverphone.encode("crazy"));
        // Test with uppercase letters
        assertEquals("tmt1111111", caverphone.encode("TEST"));
        assertEquals("ct31101111", caverphone.encode("CRAZY"));
        // Test with numbers
        assertEquals("ct31101111", caverphone.encode("craz7"));
        assertEquals("tmt1111111", caverphone.encode("t3st"));
        // Test with special characters
        assertEquals("ct31101111", caverphone.encode("craj^^"));
        assertEquals("tmt1111111", caverphone.encode("tes^t"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test with empty strings
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        // Test with different strings
        assertEquals(false, caverphone.isCaverphoneEqual("test", "crazy"));
        assertEquals(false, caverphone.isCaverphoneEqual("TEST", "CRAZY"));
        // Test with same strings
        assertEquals(true, caverphone.isCaverphoneEqual("test", "test"));
        assertEquals(true, caverphone.isCaverphoneEqual("CRAZY", "CRAZY"));
    }
}