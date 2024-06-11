package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNull() {
        Caverphone encoder = new Caverphone();
        assertNull(encoder.caverphone(null));
    }

    @Test
    public void testCaverphoneEmpty() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.caverphone(""));
    }

    @Test
    public void testCaverphoneLowercase() {
        Caverphone encoder = new Caverphone();
        assertEquals("3222F22F22F22F22F22F22F22F22F22F22F22F22F22F22F22F", encoder.caverphone("hello world"));
    }

    @Test
    public void testCaverphoneNoLetters() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.caverphone("123456"));
    }

    @Test
    public void testCaverphoneStartsWith() {
        Caverphone encoder = new Caverphone();
        assertEquals("cou2f", encoder.caverphone("cough"));
        assertEquals("tou2f", encoder.caverphone("tough"));
        assertEquals("enou2f", encoder.caverphone("enough"));
        assertEquals("trou2f", encoder.caverphone("trough")); // Note: The spec seems to have a typo here, c+p error?
        assertEquals("m2", encoder.caverphone("gn"));
        assertEquals("m2", encoder.caverphone("mb"));
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone encoder = new Caverphone();
        assertEquals("2q", encoder.caverphone("cq"));
        assertEquals("si", encoder.caverphone("ci"));
        assertEquals("se", encoder.caverphone("ce"));
        assertEquals("sy", encoder.caverphone("cy"));
        assertEquals("2ch", encoder.caverphone("tch"));
        assertEquals("k", encoder.caverphone("c"));
        assertEquals("k", encoder.caverphone("q"));
        assertEquals("k", encoder.caverphone("x"));
        assertEquals("f", encoder.caverphone("v"));
        assertEquals("2g", encoder.caverphone("dg"));
        assertEquals("sio", encoder.caverphone("tio"));
        assertEquals("sia", encoder.caverphone("tia"));
        assertEquals("t", encoder.caverphone("d"));
        assertEquals("fh", encoder.caverphone("ph"));
        assertEquals("p", encoder.caverphone("b"));
        assertEquals("s2", encoder.caverphone("sh"));
        assertEquals("s", encoder.caverphone("z"));
        assertEquals("A", encoder.caverphone("^[aeiou]"));
        assertEquals("3", encoder.caverphone("[aeiou]"));
        assertEquals("y", encoder.caverphone("j")); // 2.0 only
        assertEquals("Y3", encoder.caverphone("^y3")); // 2.0 only
        assertEquals("A", encoder.caverphone("^y")); // 2.0 only
        assertEquals("3", encoder.caverphone("y")); // 2.0 only
        assertEquals("3kh3", encoder.caverphone("3gh3"));
        assertEquals("22", encoder.caverphone("gh"));
        assertEquals("k", encoder.caverphone("g"));
        assertEquals("S", encoder.caverphone("s+"));
        assertEquals("T", encoder.caverphone("t+"));
        assertEquals("P", encoder.caverphone("p+"));
        assertEquals("K", encoder.caverphone("k+"));
        assertEquals("F", encoder.caverphone("f+"));
        assertEquals("M", encoder.caverphone("m+"));
        assertEquals("N", encoder.caverphone("n+"));
        assertEquals("W3", encoder.caverphone("w3"));
        // Uncomment the following lines if testing for 1.0 behavior
        // assertEquals("Wy", encoder.caverphone("wy"));
        // assertEquals("Wy", encoder.caverphone("why"));
        assertEquals("Wh3", encoder.caverphone("wh3"));
        assertEquals("3", encoder.caverphone("w"));
        assertEquals("2", encoder.caverphone("h"));
        assertEquals("A", encoder.caverphone("^h"));
        assertEquals("2", encoder.caverphone("h"));
        assertEquals("R3", encoder.caverphone("r3"));
        assertEquals("3", encoder.caverphone("r$")); // 2.0 only
        // Uncomment the following lines if testing for 1.0 behavior
        // assertEquals("Ry", encoder.caverphone("ry"));
        // assertEquals("Ry", encoder.caverphone("ry"));
        assertEquals("2", encoder.caverphone("r"));
        assertEquals("L3", encoder.caverphone("l3"));
        assertEquals("3", encoder.caverphone("l$")); // 2.0 only
        // Uncomment the following lines if testing for 1.0 behavior
        // assertEquals("Ly", encoder.caverphone("ly"));
        // assertEquals("Ly", encoder.caverphone("ly"));
        assertEquals("2", encoder.caverphone("l"));
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone encoder = new Caverphone();
        assertEquals("", encoder.caverphone("2"));
        assertEquals("A", encoder.caverphone("3$")); // 2.0 only
        assertEquals("", encoder.caverphone("3"));
    }

    @Test
    public void testCaverphoneEncoding() throws EncoderException {
        Caverphone encoder = new Caverphone();
        assertEquals("3222F22F22F22F22F22F22F22F22F22F22F22F22F22F22F22F", encoder.encode("hello world"));
    }

    @Test
    public void testCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        String str1 = "hello world";
        String str2 = "hello world";
        String str3 = "world hello";
        String str4 = "123456";

        assertTrue(encoder.isCaverphoneEqual(str1, str1));
        assertTrue(encoder.isCaverphoneEqual(str1, str2));
        assertFalse(encoder.isCaverphoneEqual(str1, str3));
        assertFalse(encoder.isCaverphoneEqual(str1, str4));
    }
}