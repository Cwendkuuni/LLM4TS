package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("1111111111", caverphoneEncoder.caverphone(""));
    }

    @Test
    public void testCaverphoneNullString() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertNull(caverphoneEncoder.caverphone(null));
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("1111111111", caverphoneEncoder.caverphone("foo"));
        assertEquals("1111111111", caverphoneEncoder.caverphone("BAR"));
        assertEquals("1111111111", caverphoneEncoder.caverphone("abc"));
    }

    @Test
    public void testCaverphoneRemoveNonAlphabets() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("1111111111", caverphoneEncoder.caverphone("foo1Bar2"));
        assertEquals("1111111111", caverphoneEncoder.caverphone("AbCdEfGhIj"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("1111111111", caverphoneEncoder.caverphone("fooBare"));
        assertEquals("1111111111", caverphoneEncoder.caverphone("AbCde"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("cou2f", caverphoneEncoder.caverphone("cough"));
        assertEquals("rou2f", caverphoneEncoder.caverphone("rough"));
        assertEquals("tou2f", caverphoneEncoder.caverphone("tough"));
        assertEquals("enou2f", caverphoneEncoder.caverphone("enough"));
        assertEquals("trou2f", caverphoneEncoder.caverphone("trough"));
        assertEquals("2n", caverphoneEncoder.caverphone("gn"));
        assertEquals("m2", caverphoneEncoder.caverphone("mb"));
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("2q", caverphoneEncoder.caverphone("cq"));
        assertEquals("si", caverphoneEncoder.caverphone("ci"));
        assertEquals("se", caverphoneEncoder.caverphone("ce"));
        assertEquals("sy", caverphoneEncoder.caverphone("cy"));
        assertEquals("2ch", caverphoneEncoder.caverphone("tch"));
        assertEquals("k", caverphoneEncoder.caverphone("c"));
        assertEquals("k", caverphoneEncoder.caverphone("q"));
        assertEquals("k", caverphoneEncoder.caverphone("x"));
        assertEquals("f", caverphoneEncoder.caverphone("v"));
        assertEquals("2g", caverphoneEncoder.caverphone("dg"));
        assertEquals("sio", caverphoneEncoder.caverphone("tio"));
        assertEquals("sia", caverphoneEncoder.caverphone("tia"));
        assertEquals("t", caverphoneEncoder.caverphone("d"));
        assertEquals("fh", caverphoneEncoder.caverphone("ph"));
        assertEquals("p", caverphoneEncoder.caverphone("b"));
        assertEquals("s2", caverphoneEncoder.caverphone("sh"));
        assertEquals("s", caverphoneEncoder.caverphone("z"));
        assertEquals("A", caverphoneEncoder.caverphone("^h"));
        assertEquals("2", caverphoneEncoder.caverphone("h"));
        assertEquals("R3", caverphoneEncoder.caverphone("r3"));
        assertEquals("3", caverphoneEncoder.caverphone("r$"));
        //assertEquals("Ry", caverphoneEncoder.caverphone("ry"));    // 1.0 only
        assertEquals("2", caverphoneEncoder.caverphone("r"));
        assertEquals("L3", caverphoneEncoder.caverphone("l3"));
        assertEquals("3", caverphoneEncoder.caverphone("l$"));
        //assertEquals("Ly", caverphoneEncoder.caverphone("ly"));    // 1.0 only
        assertEquals("2", caverphoneEncoder.caverphone("l"));
        //assertEquals("y", caverphoneEncoder.caverphone("j"));      // 1.0 only
        //assertEquals("Y3", caverphoneEncoder.caverphone("y3"));    // 1.0 only
        //assertEquals("Y", caverphoneEncoder.caverphone("y"));      // 1.0 only
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("", caverphoneEncoder.caverphone("2"));
        assertEquals("A", caverphoneEncoder.caverphone("3$"));
        assertEquals("", caverphoneEncoder.caverphone("3"));
    }

    @Test
    public void testCaverphoneAddZeros() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals("11111111111111", caverphoneEncoder.caverphone("foo"));
        assertEquals("11111111111111", caverphoneEncoder.caverphone("BAR"));
        assertEquals("11111111111111", caverphoneEncoder.caverphone("abc"));
    }

    @Test
    public void testCaverphoneCodeLength() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals(10, caverphoneEncoder.caverphone("foo").length());
        assertEquals(10, caverphoneEncoder.caverphone("BAR").length());
        assertEquals(10, caverphoneEncoder.caverphone("abc").length());
    }

    @Test
    public void testCaverphoneEncodeObject() throws EncoderException {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals(caverphoneEncoder.caverphone("foo"), caverphoneEncoder.encode("foo"));
    }

    @Test
    public void testCaverphoneEncodeString() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertEquals(caverphoneEncoder.caverphone("foo"), caverphoneEncoder.encode("foo"));
    }

    @Test
    public void testCaverphoneIsEqual() {
        Caverphone caverphoneEncoder = new Caverphone();
        assertTrue(caverphoneEncoder.isCaverphoneEqual("foo", "foo"));
        assertFalse(caverphoneEncoder.isCaverphoneEqual("foo", "bar"));
    }
}