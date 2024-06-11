import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.language.Caverphone;

import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        // Typical use case
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));

        // Edge case
        assertEquals("1111111111", caverphone.caverphone("Aa123*@#")); // All non-alphabetic characters
        assertEquals("1111111111", caverphone.caverphone("caughtroughtoughenoughtroughgngsmb")); // Longest valid input

        // Error scenario
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()_+=-")); // All special characters
        assertEquals("1111111111", caverphone.caverphone(" ")); // Space character
    }

    @Test
    public void testEncode() {
        // Typical use case
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode("abc123"));

        // Edge case
        assertEquals("SEMKISOME3", caverphone.encode("semkisome")); // Longest valid input

        // Error scenario
        try {
            caverphone.encode(123); // Invalid input type
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        // Typical use case
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));

        // Edge case
        assertTrue(caverphone.isCaverphoneEqual("abc", "ABC")); // Case-insensitive comparison

        // Error scenario
        assertFalse(caverphone.isCaverphoneEqual("abc", null));
        assertFalse(caverphone.isCaverphoneEqual(null, "abc"));
    }

}