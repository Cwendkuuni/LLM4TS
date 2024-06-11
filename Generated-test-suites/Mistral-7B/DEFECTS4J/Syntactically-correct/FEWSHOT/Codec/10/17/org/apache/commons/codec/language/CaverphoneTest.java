package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.caverphone(null));
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("fkfkfkfkfkfkfk", caverphone.caverphone("foobar"));
        assertEquals("kkktttpkpkkpkpkp", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("cou2f", caverphone.caverphone("cougH"));
        assertEquals("cou2f", caverphone.caverphone("cough "));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("rou2f", caverphone.caverphone("rOUGH"));
        assertEquals("rou2f", caverphone.caverphone("rough "));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("tou2f", caverphone.caverphone("tOUGH"));
        assertEquals("tou2f", caverphone.caverphone("tough "));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        // TODO: test --enough and enOUGH
        assertEquals("enou2f", caverphone.caverphone("enough "));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("trou2f", caverphone.caverphone("tROUGH"));
        assertEquals("trou2f", caverphone.caverphone("trough "));
        // TODO: test --enough again
        assertEquals("m2", caverphone.caverphone("mb"));
    }

    @Test
    public void testCaverphoneReplacements() {
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
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("R3", caverphone.caverphone("r 3"));
        assertEquals("R3", caverphone.caverphone("r3 "));
        assertEquals("3", caverphone.caverphone("r$"));
        // TODO: test Ry, Ry, Why, Why
        assertEquals("2", caverphone.caverphone("w"));
        // TODO: test Y3, y, y3, y, y, y3
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r"));
        assertEquals("3", caverphone.caverphone("r$"));
        // TODO: test Ly, Ly, Ly
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("L3", caverphone.caverphone("l "));
        assertEquals("L3", caverphone.caverphone("l3 "));
        // TODO: test Ly, Ly
        // TODO: test Why, Why
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneHandlers() {
        Caverphone caverphone = new Caverphone();
        assertEquals("S", caverphone.caverphone("s+"));
        assertEquals("T", caverphone.caverphone("t+"));
        assertEquals("P", caverphone.caverphone("p+"));
        assertEquals("K", caverphone.caverphone("k+"));
        assertEquals("F", caverphone.caverphone("f+"));
        assertEquals("M", caverphone.caverphone("m+"));
        assertEquals("N", caverphone.caverphone("n+"));
        // TODO: test W3, Wy, Wh3
    }

    @Test
    public void testCaverphoneComparison() {
        Caverphone caverphone = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "The quick brown fox jumps over the lazy dog";
        String str3 = "The quick brown fox jumps over the lazy cat";       
        assertEquals(true, caverphone.isCaverphoneEqual(str1, str1));
        assertEquals(true, caverphone.isCaverphoneEqual(str1, str2));
        assertFalse(caverphone.isCaverphoneEqual(str1, str3));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneInvalidInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object() {});
    }
}