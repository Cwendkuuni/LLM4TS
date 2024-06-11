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
        assertEquals("2222222222", caverphone.caverphone("hello"));
        assertEquals("2222222222", caverphone.caverphone("HELLO"));
        assertEquals("2222222222", caverphone.caverphone("Hello"));
    }

    @Test
    public void testCaverphoneRemoveNonAlphabet() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2222222222", caverphone.caverphone("hello123"));
        assertEquals("2222222222", caverphone.caverphone("Hello123"));
        assertEquals("2222222222", caverphone.caverphone("Hello123"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough")); // 2.0 only
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
        assertEquals("y", caverphone.caverphone("j")); // 2.0 only
        assertEquals("Y3", caverphone.caverphone("^y3")); // 2.0 only
        assertEquals("A", caverphone.caverphone("^y")); // 2.0 only
        assertEquals("3", caverphone.caverphone("y")); // 2.0 only
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
        assertEquals("3", caverphone.caverphone("w$")); // 2.0 only
        //assertEquals("Why", caverphone.caverphone("why")); // 1.0 only
        assertEquals("2", caverphone.caverphone("w"));
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
        assertEquals("11111111111111", caverphone.caverphone("hello123"));
        assertEquals("11111111111111", caverphone.caverphone("Hello123"));
        assertEquals("11111111111111", caverphone.caverphone("Hello123"));
    }

    @Test
    public void testCaverphoneFirstSixCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone("123456abc").substring(0, 10));
        assertEquals("1111111111", caverphone.caverphone("123456ABC").substring(0, 10)); // 1.0 truncates to 6
        assertEquals("1111111111", caverphone.caverphone("123456ABC").substring(0, 10)); // 1.0 truncates to 6
    }
}