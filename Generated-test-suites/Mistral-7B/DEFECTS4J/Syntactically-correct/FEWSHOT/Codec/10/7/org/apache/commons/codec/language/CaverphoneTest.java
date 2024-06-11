package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleCharacter() {
        Caverphone encoder = new Caverphone();
        assertEquals("A", encoder.caverphone("a"));
    }

    @Test
    public void testCaverphoneLowercase() {
        Caverphone encoder = new Caverphone();
        assertEquals("t", encoder.caverphone("t"));
        assertEquals("fh", encoder.caverphone("th"));
        assertEquals("k", encoder.caverphone("c"));
        assertEquals("s2", encoder.caverphone("sh"));
        assertEquals("s", encoder.caverphone("z"));
        assertEquals("A", encoder.caverphone("e"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone encoder = new Caverphone();
        assertEquals("cou2f", encoder.caverphone("cough"));
        assertEquals("rou2f", encoder.caverphone("rough"));
        assertEquals("tou2f", encoder.caverphone("tough"));
        assertEquals("enou2f", encoder.caverphone("enough"));
        assertEquals("trou2f", encoder.caverphone("trough")); // 2.0 only, mistakenly in place of "enough"
        assertEquals("2n", encoder.caverphone("gn"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
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
        assertEquals("A", encoder.caverphone("h"));
        assertEquals("2", encoder.caverphone("h"));
        assertEquals("R3", encoder.caverphone("r3"));
        assertEquals("3", encoder.caverphone("r"));
        assertEquals("L3", encoder.caverphone("l3"));
        assertEquals("3", encoder.caverphone("l"));
        //assertEquals("Wy", encoder.caverphone("wy")); // 1.0 only
        assertEquals("Wh3", encoder.caverphone("wh3"));
        assertEquals("3", encoder.caverphone("w"));
        assertEquals("A", encoder.caverphone("w")); // 2.0 only
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
        //assertEquals("Wy", encoder.caverphone("wy")); // 1.0 only
        assertEquals("Wh3", encoder.caverphone("wh3"));
        assertEquals("3", encoder.caverphone("w"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone encoder = new Caverphone();
        assertEquals("", encoder.caverphone("2"));
        assertEquals("A", encoder.caverphone("3$")); // 2.0 only
        assertEquals("", encoder.caverphone("3"));
    }

    @Test
    public void testCaverphoneAddsTrailingZeros() {
        Caverphone encoder = new Caverphone();
        assertEquals("t1111011111", encoder.caverphone("t"));
    }

    @Test
    public void testCaverphoneEncodesObject() throws EncoderException {
        Caverphone encoder = new Caverphone();
        String result = encoder.encode("test");
        assertEquals(13, result.length());
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneThrowsEncoderExceptionForInvalidInput() throws EncoderException {
        Caverphone encoder = new Caverphone();
        encoder.encode(123);
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        String txt1 = "test";
        String txt2 = "tesT";
        String txt3 = "test";
        String txt4 = "Test";

        assertEquals(encoder.caverphone(txt1), encoder.caverphone(txt3));
        assertNotEquals(encoder.caverphone(txt1), encoder.caverphone(txt2));
        assertNotEquals(encoder.caverphone(txt1), encoder.caverphone(txt4));
    }
}