import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setup() {
        caverphone = new Caverphone();
    }

    // caverphone method test cases
    @Test
    public void testCaverphone_validString_returnsCaverphoneCode() {
        // Typical use case
        String input = "hello";
        String expectedOutput = "HL11111111";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_nullString_returnsDefaultCaverphoneCode() {
        // Edge case: Null string input
        String input = null;
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCaverphone_emptyString_returnsDefaultCaverphoneCode() {
        // Edge case: Empty string input
        String input = "";
        String expectedOutput = "1111111111";
        String actualOutput = caverphone.caverphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // encode method test cases
    @Test
    public void testEncode_validString_returnsCaverphoneCodeAsString() {
        // Typical use case
        String input = "hello";
        String expectedOutput = "HL11111111";
        String actualOutput = caverphone.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_nonStringInput_throwsEncoderException() {
        // Error scenario: Non-string input
        Object input = new Object();
        caverphone.encode(input);
    }

    // isCaverphoneEqual method test cases
    @Test
    public void testIsCaverphoneEqual_identicalStrings_returnsTrue() {
        // Typical use case: Identical strings
        String str1 = "hello";
        String str2 = "hello";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings_returnsFalse() {
        // Typical use case: Different strings
        String str1 = "hello";
        String str2 = "world";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqual_caseInsensitiveStrings_returnsTrue() {
        // Typical use case: Case-insensitive strings
        String str1 = "hello";
        String str2 = "HELLO";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqual_nullStrings_returnsFalse() {
        // Edge case: Null strings
        String str1 = null;
        String str2 = null;
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqual_nullAndNonNullStrings_returnsFalse() {
        // Edge case: One null string and one non-null string
        String str1 = "hello";
        String str2 = null;
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }

}