package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodesNullInputToDefaultString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    @Test
    public void testCaverphoneEncodesEmptyInputToDefaultString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneHandlesLowercase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2p2s2t2f2k2t2f2p2k2f2p2k2f2p", caverphone.caverphone("The Quick Brown Fox"));
    }

    @Test
    public void testCaverphoneHandlesRemovalOfNonAlphabeticCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2p2s2t2f2k2t2f2p2k2f2p2k2f2p", caverphone.caverphone("The Qu!ck Brown #Fox"));
    }

    @Test
    public void testCaverphoneHandlesEndingE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("enou2f2p2s2t2f2k2t2f2p", caverphone.caverphone("enough"));
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f2sio2f", caverphone.caverphone("cough"));
        assertEquals("rou2f2sia", caverphone.caverphone("rough"));
        assertEquals("tou2f2sio2f", caverphone.caverphone("tough"));
        assertEquals("enou2f2sio2f", caverphone.caverphone("enough"));
        assertEquals("trou2f2sio2f", caverphone.caverphone("trough"));
        assertEquals("m2", caverphone.caverphone("gn"));
        assertEquals("m2n", caverphone.caverphone("mb"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2q2s", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch2p", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g2g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        // Uncomment the following lines for 1.0 test
        // assertEquals("Wy", caverphone.caverphone("wy"));
        // assertEquals("Why", caverphone.caverphone("why"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("h", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        // Uncomment the following lines for 1.0 test
        // assertEquals("Wy", caverphone.caverphone("wy"));
        // assertEquals("Why", caverphone.caverphone("why"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("A", caverphone.caverphone("h"));
    }

    @Test
    public void testCaverphoneHandles3gh3() {
        Caverphone caverphone = new Caverphone();
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneAddsTen1sOnTheEnd() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2p2s2t2f2k2t2f2p2k2f2p2k2f2p2k2f2p1111111111", caverphone.caverphone("The Quick Brown Fox"));
    }

    @Test
    public void testCaverphoneEncodesObject() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2p2s2t2f2k2t2f2p2k2f2p2k2f2p2k2f2p1111111111", caverphone.encode("The Quick Brown Fox"));
    }

    @Test
    public void testCaverphoneInequalityHandlesDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        assertNotEquals("2p2s2t2f2k2t2f2p2k2f2p2k2f2p2k2f2p1111111111", caverphone.caverphone("The Quick Brown Dog"));
    }
}