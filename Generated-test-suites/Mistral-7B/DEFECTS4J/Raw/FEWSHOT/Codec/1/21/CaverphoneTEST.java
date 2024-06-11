package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("a", caverphone.caverphone("a"));
        assertEquals("A", caverphone.caverphone("A"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("af2t2s2s2f2f", caverphone.caverphone("afternoon"));
        assertEquals("A1F1T1S1S1F1F1", caverphone.caverphone("AFTERNOON"));
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));
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
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("Y3", caverphone.caverphone("^y3"));
        assertEquals("A", caverphone.caverphone("^y"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s+"));
        assertEquals("T", caverphone.caverphone("t+"));
        assertEquals("P", caverphone.caverphone("p+"));
        assertEquals("K", caverphone.caverphone("k+"));
        assertEquals("F", caverphone.caverphone("f+"));
        assertEquals("M", caverphone.caverphone("m+"));
        assertEquals("N", caverphone.caverphone("n+"));
        assertEquals("W3", caverphone.caverphone("w3"));
        // Uncomment the following lines if you have JUnit 4.11 or higher
        // assertEquals("Wy", caverphone.caverphone("wy"));
        // assertEquals("Wy", caverphone.caverphone("Why"));
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w$"));
        // Uncomment the following lines if you have JUnit 4.11 or higher
        // assertEquals("Why", caverphone.caverphone("why"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        // Uncomment the following lines if you have JUnit 4.11 or higher
        // assertEquals("Ry", caverphone.caverphone("ry"));
        // assertEquals("Ry", caverphone.caverphone("Ry"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$"));
        // Uncomment the following lines if you have JUnit 4.11 or higher
        // assertEquals("Ly", caverphone.caverphone("ly"));
        // assertEquals("Ly", caverphone.caverphone("Ly"));
        assertEquals("2", caverphone.caverphone("l"));
        // Uncomment the following lines if you have JUnit 4.11 or higher
        // assertEquals("y", caverphone.caverphone("j"));
        // assertEquals("Y3", caverphone.caverphone("^y3"));
        // assertEquals("A", caverphone.caverphone("^y"));
        // assertEquals("3", caverphone.caverphone("y"));

    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneHandlesAppendix() {
        Caverphone caverphone = new Caverphone();
        assertEquals("111111111111111111", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void testCaverphoneHandlesCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("af2t2s2s2f2f", caverphone.caverphone("afternoon"));
        assertEquals("A1F1T1S1S1F1F1", caverphone.caverphone("AFTERNOON"));
    }

    @Test
    public void testCaverphoneHandlesCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "afternoon";
        String str2 = "afternoon";
        String str3 = "afternoon";
        String str4 = "AFTERNOON";
        String str5 = "evening";
        String str6 = "EVENING";
        String str7 = "evening";
        String str8 = "EVENING";
        assertEquals(true, caverphone.isCaverphoneEqual(str1, str2));
        assertEquals(true, caverphone.isCaverphoneEqual(str1, str3));
        assertEquals(true, caverphone.isCaverphoneEqual(str4, str1));
        assertEquals(true, caverphone.isCaverphoneEqual(str4, str3));
        assertEquals(true, caverphone.isCaverphoneEqual(str4, str5));
        assertEquals(true, caverphone.isCaverphoneEqual(str6, str1));
        assertEquals(true, caverphone.isCaverphoneEqual(str6, str3));
        assertEquals(true, caverphone.isCaverphoneEqual(str6, str4));
        assertEquals(true, caverphone.isCaverphoneEqual(str6, str7));
        assertEquals(true, caverphone.isCaverphoneEqual(str7, str1));
        assertEquals(true, caverphone.isCaverphoneEqual(str7, str3));
        assertEquals(true, caverphone.isCaverphoneEqual(str7, str4));
        assertEquals(true, caverphone.isCaverphoneEqual(str7, str6));
        assertEquals(false, caverphone.isCaverphoneEqual("afternoon", "evening"));
        assertEquals(false, caverphone.isCaverphoneEqual("evening", "afternoon"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneThrowsEncoderExceptionForNonString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

}