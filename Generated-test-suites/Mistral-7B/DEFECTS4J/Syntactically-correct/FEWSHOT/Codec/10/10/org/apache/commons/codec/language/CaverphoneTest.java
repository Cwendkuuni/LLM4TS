package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNull() {
        Caverphone caverphone = new Caverphone();
        assertThrows(EncoderException.class, () -> caverphone.encode(null));
    }

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("12222222222233", caverphone.encode("Apple"));
        assertEquals("12222222222233", caverphone.encode("apple"));
    }

    @Test
    public void testCaverphoneRemoveSpecialChars() {
        Caverphone caverphone = new Caverphone();
        assertEquals("M2F22M2M2M2M2N2NN2N2M2M2M2M2M2M2", caverphone.encode("Mellow Yellow"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("M2F22M2M2M2M2N2NN2N2M2M2M2M2M2M2", caverphone.encode("Mellow Yellow"));
        assertEquals("M2F22M2M2M2M2N2NN2N2M2M2M2M2M2M2", caverphone.encode("Mellow Yellowy"));
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.encode("Cough"));
        assertEquals("cou2f", caverphone.encode("cough"));
        assertEquals("rou2f", caverphone.encode("Rough"));
        assertEquals("rou2f", caverphone.encode("rough"));
        assertEquals("tou2f", caverphone.encode("Tough"));
        assertEquals("tou2f", caverphone.encode("tough"));
        assertEquals("enou2f", caverphone.encode("Enough"));
        assertEquals("enou2f", caverphone.encode("enough"));
        assertEquals("trou2f", caverphone.encode("Trough"));
        assertEquals("trou2f", caverphone.encode("trough"));
        assertEquals("2n", caverphone.encode("gn"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2q", caverphone.encode("cq"));
        assertEquals("si", caverphone.encode("ci"));
        assertEquals("se", caverphone.encode("ce"));
        assertEquals("sy", caverphone.encode("cy"));
        assertEquals("2ch", caverphone.encode("tch"));
        assertEquals("k", caverphone.encode("c"));
        assertEquals("k", caverphone.encode("q"));
        assertEquals("k", caverphone.encode("x"));
        assertEquals("f", caverphone.encode("v"));
        assertEquals("2g", caverphone.encode("dg"));
        assertEquals("sio", caverphone.encode("tio"));
        assertEquals("sia", caverphone.encode("tia"));
        assertEquals("t", caverphone.encode("d"));
        assertEquals("fh", caverphone.encode("ph"));
        assertEquals("p", caverphone.encode("b"));
        assertEquals("s2", caverphone.encode("sh"));
        assertEquals("s", caverphone.encode("z"));
        assertEquals("A", caverphone.encode("^[aeiou]"));
        assertEquals("3", caverphone.encode("[aeiou]"));
        assertEquals("y", caverphone.encode("j"));
        assertEquals("Y3", caverphone.encode("^y3"));
        assertEquals("A", caverphone.encode("^y"));
        assertEquals("3", caverphone.encode("y"));
        assertEquals("3kh3", caverphone.encode("3gh3"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("k", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s+"));
        assertEquals("T", caverphone.encode("t+"));
        assertEquals("P", caverphone.encode("p+"));
        assertEquals("K", caverphone.encode("k+"));
        assertEquals("F", caverphone.encode("f+"));
        assertEquals("M", caverphone.encode("m+"));
        assertEquals("N", caverphone.encode("n+"));
        assertEquals("W3", caverphone.encode("w3"));
        // assertEquals("Wy", caverphone.encode("wy"));        // 1.0 only
        assertEquals("Wh3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("^h"));
        assertEquals("2", caverphone.encode("h"));
        assertEquals("R3", caverphone.encode("r3"));
        assertEquals("3", caverphone.encode("r$"));      // 2.0 only
        // assertEquals("Ry", caverphone.encode("ry"));        // 1.0 only
        assertEquals("2", caverphone.encode("r"));
        assertEquals("L3", caverphone.encode("l3"));
        assertEquals("3", caverphone.encode("l$"));      // 2.0 only
        // assertEquals("Ly", caverphone.encode("ly"));        // 1.0 only
        assertEquals("2", caverphone.encode("l"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.encode("2"));
        assertEquals("A", caverphone.encode("3$"));       // 2.0 only
        assertEquals("", caverphone.encode("3"));
    }

    @Test
    public void testCaverphoneHandlesAddition() {
        Caverphone caverphone = new Caverphone();
        assertEquals("111111111111111111", caverphone.encode("AppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleAppleApple"));
    }

    @Test
    public void testCaverphoneEncodesObject() {
        Caverphone caverphone = new Caverphone();
        assertEquals("12222222222233", caverphone.encode("Apple"));
        assertEquals("12222222222233", caverphone.encode("apple"));
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertEquals(true, caverphone.isCaverphoneEqual("Apple", "apple"));
        assertEquals(false, caverphone.isCaverphoneEqual("Apple", "Banana"));
    }

    @Test
    public void testCaverphoneEncodeDifferentEncodings() {
        Caverphone caverphone = new Caverphone();
        assertEquals("5353121312131213131333", caverphone.encode("Mary"));
        assertEquals("5353531213121312131333", caverphone.encode("mary"));
        assertEquals(false, caverphone.isCaverphoneEqual("Mary", "mary"));
    }
}