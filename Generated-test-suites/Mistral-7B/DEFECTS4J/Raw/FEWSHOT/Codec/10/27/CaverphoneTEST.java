package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
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
        assertEquals("1111111111", caverphone.caverphone("example"));
        assertEquals("1111111111", caverphone.caverphone("Example"));
    }

    @Test
    public void testCaverphoneRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        assertEquals("ab", caverphone.caverphone("abXc"));
        assertEquals("ab", caverphone.caverphone("AbXc"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("ab", caverphone.caverphone("abE"));
        assertEquals("ab", caverphone.caverphone("AbE"));
    }

    @Test
    public void testCaverphoneStartingOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("cou2f", caverphone.caverphone("Cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("rou2f", caverphone.caverphone("Rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("tou2f", caverphone.caverphone("Tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("enou2f", caverphone.caverphone("Enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("trou2f", caverphone.caverphone("Trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
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
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        // Uncomment the following lines if J is supported in the 2.0 version
        // assertEquals("y", caverphone.caverphone("j"));
        // assertEquals("Y3", caverphone.caverphone("^y3"));
        // assertEquals("A", caverphone.caverphone("^y"));
        // assertEquals("3", caverphone.caverphone("y"));
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
        // Uncomment the following lines if Wy is supported in the 1.0 version
        // assertEquals("Wy", caverphone.caverphone("wy"));
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w$"));
        // Uncomment the following lines if Why is supported in the 1.0 version
        // assertEquals("Why", caverphone.caverphone("why"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$"));
        // Uncomment the following lines if Ry is supported in the 1.0 version
        // assertEquals("Ry", caverphone.caverphone("ry"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$"));
        // Uncomment the following lines if Ly is supported in the 1.0 version
        // assertEquals("Ly", caverphone.caverphone("ly"));
        assertEquals("2", caverphone.caverphone("l"));
        // Uncomment the following lines if y is supported in the 1.0 version
        // assertEquals("2", caverphone.caverphone("y"));
        // assertEquals("Y3", caverphone.caverphone("y3"));
        // Uncomment the following lines if Y is supported in the 1.0 version
        // assertEquals("A", caverphone.caverphone("Y"));
        // assertEquals("3", caverphone.caverphone("y"));

    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneAppend1111111111() {
        Caverphone caverphone = new Caverphone();
        assertEquals("111111111111111111", caverphone.caverphone("ab"));
    }

    @Test
    public void testCaverphoneTakeFirstTenCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("ab111111", caverphone.caverphone("ab"));
    }
}