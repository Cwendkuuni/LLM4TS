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
    public void testCaverphoneLowercase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("3232211111", caverphone.caverphone("hello world"));
    }

    @Test
    public void testCaverphoneRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        assertEquals("hlowrd", caverphone.caverphone("hello world"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("hlowrd", caverphone.caverphone("hello world")); // 2.0 only
        assertEquals("hlowrde", caverphone.caverphone("hello world")); // 1.0 only
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough")); // 2.0 only - note the spec says "enough" here again, c+p error I assume
        assertEquals("2n", caverphone.caverphone("gn"));
    }

    @Test
    public void testCaverphoneEndings() {
        Caverphone caverphone = new Caverphone();
        assertEquals("m2", caverphone.caverphone("mb"));
        //txt = txt.replaceAll("wy", "Wy");    // 1.0 only
        //assertEquals("Wy", caverphone.caverphone("why")); // 1.0 only
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("W3", caverphone.caverphone("w3"));
        //assertEquals("Why", caverphone.caverphone("why")); // 1.0 only
        assertEquals("2", caverphone.caverphone("w"));
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
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        //assertEquals("y3", caverphone.caverphone("j"));  // 1.0 only
        //assertEquals("Y3", caverphone.caverphone("Y3")); // 1.0 only
        //assertEquals("A", caverphone.caverphone("y"));  // 1.0 only
        //assertEquals("3", caverphone.caverphone("y")); // 1.0 only
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
        //assertEquals("Wy", caverphone.caverphone("wy")); // 1.0 only
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$")); // 2.0 only
        //assertEquals("Ry", caverphone.caverphone("ry")); // 1.0 only
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$")); // 2.0 only
        //assertEquals("Ly", caverphone.caverphone("ly")); // 1.0 only
        assertEquals("2", caverphone.caverphone("l"));
        //assertEquals("y", caverphone.caverphone("j")); // 1.0 only
        //assertEquals("Y3", caverphone.caverphone("Y3")); // 1.0 only
        //assertEquals("2", caverphone.caverphone("y")); // 1.0 only
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$")); // 2.0 only
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneAddOnes() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111111111111", caverphone.caverphone("test"));
    }

    @Test
    public void testCaverphoneSubstring() {
        Caverphone caverphone = new Caverphone();
        assertEquals("10charcode", caverphone.caverphone("10charcode111111").substring(0, 10)); // 1.0 truncates to 6
    }

    @Test
    public void testCaverphoneEquals() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello world";
        String str2 = "hello world";
        assertEquals(caverphone.caverphone(str1), caverphone.caverphone(str2));
    }

    @Test
    public void testCaverphoneNotEquals() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello world";
        String str2 = "hello worlds";
        assertNotEquals(caverphone.caverphone(str1), caverphone.caverphone(str2));
    }

}