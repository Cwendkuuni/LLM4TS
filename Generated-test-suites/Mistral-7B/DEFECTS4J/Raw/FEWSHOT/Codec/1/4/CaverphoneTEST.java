package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2222222222", caverphone.caverphone("hello"));
    }

    @Test
    public void testCaverphoneRemoveNonAZ() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2222222222", caverphone.caverphone("hello world"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2222222222", caverphone.caverphone("hello world"));
        assertEquals("222222222", caverphone.caverphone("hello end"));
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("m2", caverphone.caverphone("mb"));
        assertEquals("A", caverphone.caverphone("gn"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2q", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        assertEquals("", caverphone.caverphone("3"));
        assertEquals("", caverphone.caverphone("3$"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneHandlesEndAdditions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111111111111", caverphone.caverphone("hello world"));
    }

    @Test
    public void testCaverphoneEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("2222222222", caverphone.encode("hello"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneEncodeNonString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object());
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String caverphone1 = caverphone.caverphone("hello");
        String caverphone2 = caverphone.caverphone("hello");
        assertEquals(caverphone1, caverphone2);
    }

    @Test
    public void testCaverphoneIsCaverphoneNotEqual() {
        Caverphone caverphone = new Caverphone();
        String caverphone1 = caverphone.caverphone("hello");
        String caverphone2 = caverphone.caverphone("world");
        assertNotEquals(caverphone1, caverphone2);
    }
}