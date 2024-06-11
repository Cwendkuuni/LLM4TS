import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertEquals("1111111111", caverphone.caverphone(null)); // Null case
        assertEquals("1111111111", caverphone.caverphone("")); // Empty string case
        assertEquals("31111", caverphone.caverphone("test")); // Standard case
        assertEquals("1111111111", caverphone.caverphone("Caverphone")); // Case sensitivity
        assertEquals("31111", caverphone.caverphone("caverphone")); // Case sensitivity

        // Edge cases
        assertEquals("A111111111", caverphone.caverphone("enough")); // Start option - enough
        assertEquals("3A11111111", caverphone.caverphone("ynough")); // Start option - enough with y
        assertEquals("2n11111111", caverphone.caverphone("gnough")); // Start option - gn
        assertEquals("m211111111", caverphone.caverphone("mbough")); // Start option - mb
        assertEquals("sA11111111", caverphone.caverphone("shrough")); // Start option - sh
        assertEquals("WA11111111", caverphone.caverphone("whough")); // Start option - wh
        assertEquals("A111111111", caverphone.caverphone("w")); // Start option - w
        assertEquals("T22222", caverphone.caverphone("tch")); // Replacement - tch
        assertEquals("k111", caverphone.caverphone("cq")); // Replacement - cq
        assertEquals("31111", caverphone.caverphone("ci")); // Replacement - ci
        assertEquals("se11111", caverphone.caverphone("ce")); // Replacement - ce
        assertEquals("sy11111", caverphone.caverphone("cy")); // Replacement - cy
        assertEquals("k111", caverphone.caverphone("c")); // Replacement - c
        assertEquals("k111", caverphone.caverphone("q")); // Replacement - q
        assertEquals("k111", caverphone.caverphone("x")); // Replacement - x
        assertEquals("f111", caverphone.caverphone("v")); // Replacement - v
        assertEquals("K222", caverphone.caverphone("dg")); // Replacement - dg
        assertEquals("sio11111", caverphone.caverphone("tio")); // Replacement - tio
        assertEquals("p111", caverphone.caverphone("b")); // Replacement - b
        assertEquals("s2111111", caverphone.caverphone("sh")); // Replacement - sh
        assertEquals("s111111", caverphone.caverphone("z")); // Replacement - z
        assertEquals("3111111111", caverphone.caverphone("why")); // Replacement - why
        assertEquals("2", caverphone.caverphone("2")); // Removal - 2
        assertEquals("A", caverphone.caverphone("3")); // Removal - 3
        assertEquals("s", caverphone.caverphone("ss")); // Removal - s
        assertEquals("T", caverphone.caverphone("ttt")); // Removal - t
        assertEquals("P", caverphone.caverphone("ppp")); // Removal - p
        assertEquals("K", caverphone.caverphone("kkkk")); // Removal - k
        assertEquals("F", caverphone.caverphone("ffff")); // Removal - f
        assertEquals("M", caverphone.caverphone("mmmm")); // Removal - m
        assertEquals("N", caverphone.caverphone("nnnn")); // Removal - n
        assertEquals("W3", caverphone.caverphone("w3")); // Removal - w3
        assertEquals("W3", caverphone.caverphone("www")); // Removal - w
        assertEquals("223", caverphone.caverphone("gh")); // Removal - gh
        assertEquals("k2", caverphone.caverphone("g")); // Removal - g
        assertEquals("S", caverphone.caverphone("s")); // Removal - s
        assertEquals("T2", caverphone.caverphone("tt")); // Removal - t
        assertEquals("P", caverphone.caverphone("p")); // Removal - p
        assertEquals("K", caverphone.caverphone("k")); // Removal - k
        assertEquals("F", caverphone.caverphone("f")); // Removal - f
        assertEquals("M", caverphone.caverphone("m")); // Removal - m
        assertEquals("N", caverphone.caverphone("n")); // Removal - n
        assertEquals("A", caverphone.caverphone("h")); // Removal - h
        assertEquals("R3", caverphone.caverphone("r3")); // Removal - r3
        assertEquals("R3", caverphone.caverphone("rrr")); // Removal - r
        assertEquals("L3", caverphone.caverphone("l3")); // Removal - l3
        assertEquals("L3", caverphone.caverphone("lll")); // Removal - l

        // Error scenario
        try {
            caverphone.caverphone(new Object()); // Object of wrong type
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception caught as expected
        }
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertEquals("1111111111", caverphone.encode(null)); // Null case
        assertEquals("1111111111", caverphone.encode("")); // Empty string case
        assertEquals("31111", caverphone.encode("test")); // Standard case
        assertEquals("31111", caverphone.encode("Caverphone")); // Case sensitivity
        assertEquals("31111", caverphone.encode("caverphone")); // Case sensitivity

        // Error scenario
        try {
            caverphone.encode(new Object()); // Object of wrong type
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception caught as expected
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual(null, null)); // Both null case
        assertFalse(caverphone.isCaverphoneEqual(null, "")); // One null case
        assertFalse(caverphone.isCaverphoneEqual("", null)); // One null case

        // Typical use cases
        assertTrue(caverphone.isCaverphoneEqual("test", "test")); // Equal case
        assertFalse(caverphone.isCaverphoneEqual("test", "Caverphone")); // Case sensitivity
        assertFalse(caverphone.isCaverphoneEqual("caverphone", "test")); // Case sensitivity

        assertFalse(caverphone.isCaverphoneEqual("test", "foo")); // Not equal case
    }
}