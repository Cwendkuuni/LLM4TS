package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmpty() {
        assertEquals("1111111111", new Caverphone().caverphone(""));
    }

    @Test
    public void testCaverphoneLowercase() {
        assertEquals("8526326232261", new Caverphone().caverphone("hello world"));
    }

    @Test
    public void testCaverphoneUppercase() {
        assertEquals("8526326232261", new Caverphone().caverphone("HELLO WORLD"));
    }

    @Test
    public void testCaverphoneSpecialCharacters() {
        assertEquals("2KkF2Kf", new Caverphone().caverphone("C0ff33 2mK@"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        assertEquals("cou2f", new Caverphone().caverphone("cough"));
        assertEquals("rou2f", new Caverphone().caverphone("rough"));
        assertEquals("tou2f", new Caverphone().caverphone("tough"));
        assertEquals("enou2f", new Caverphone().caverphone("enough"));
        assertEquals("trou2f", new Caverphone().caverphone("trough"));
        assertEquals("2n", new Caverphone().caverphone("gn"));
        assertEquals("m2", new Caverphone().caverphone("mb"));
    }

    @Test
    public void testCaverphoneReplacements() {
        assertEquals("2q2q", new Caverphone().caverphone("cqcq"));
        assertEquals("si", new Caverphone().caverphone("ci"));
        assertEquals("se", new Caverphone().caverphone("ce"));
        assertEquals("sy", new Caverphone().caverphone("cy"));
        assertEquals("2ch2ch", new Caverphone().caverphone("tch"));
        assertEquals("k", new Caverphone().caverphone("c"));
        assertEquals("k", new Caverphone().caverphone("q"));
        assertEquals("k", new Caverphone().caverphone("x"));
        assertEquals("f", new Caverphone().caverphone("v"));
        assertEquals("2g2g", new Caverphone().caverphone("dg"));
        assertEquals("sio", new Caverphone().caverphone("tio"));
        assertEquals("sia", new Caverphone().caverphone("tia"));
        assertEquals("t", new Caverphone().caverphone("d"));
        assertEquals("fh", new Caverphone().caverphone("ph"));
        assertEquals("p", new Caverphone().caverphone("b"));
        assertEquals("s2", new Caverphone().caverphone("sh"));
        assertEquals("s", new Caverphone().caverphone("z"));
        assertEquals("A", new Caverphone().caverphone("^[aeiou]"));
        assertEquals("3", new Caverphone().caverphone("[aeiou]"));
        assertEquals("y", new Caverphone().caverphone("j"));
        assertEquals("Y3", new Caverphone().caverphone("^y3"));
        assertEquals("A", new Caverphone().caverphone("^y"));
        assertEquals("3", new Caverphone().caverphone("y"));
        assertEquals("3kh3", new Caverphone().caverphone("3gh3"));
        assertEquals("22", new Caverphone().caverphone("gh"));
        assertEquals("k", new Caverphone().caverphone("g"));
        assertEquals("S", new Caverphone().caverphone("s+"));
        assertEquals("T", new Caverphone().caverphone("t+"));
        assertEquals("P", new Caverphone().caverphone("p+"));
        assertEquals("K", new Caverphone().caverphone("k+"));
        assertEquals("F", new Caverphone().caverphone("f+"));
        assertEquals("M", new Caverphone().caverphone("m+"));
        assertEquals("N", new Caverphone().caverphone("n+"));
        assertEquals("W3", new Caverphone().caverphone("w3"));
        //uncomment the following lines if testing for 1.0 version
        //assertEquals("Wy", new Caverphone().caverphone("wy"));
        //assertEquals("Wy", new Caverphone().caverphone("Why"));
        assertEquals("Wh3", new Caverphone().caverphone("wh3"));
        assertEquals("3", new Caverphone().caverphone("w$"));
        //uncomment the following line if testing for 1.0 version
        //assertEquals("Why", new Caverphone().caverphone("why"));
        assertEquals("2", new Caverphone().caverphone("w"));
        assertEquals("A", new Caverphone().caverphone("^h"));
        assertEquals("2", new Caverphone().caverphone("h"));
        assertEquals("R3", new Caverphone().caverphone("r3"));
        assertEquals("3", new Caverphone().caverphone("r$"));
        //uncomment the following line if testing for 1.0 version
        //assertEquals("Ry", new Caverphone().caverphone("ry"));
        assertEquals("2", new Caverphone().caverphone("r"));
        assertEquals("L3", new Caverphone().caverphone("l3"));
        assertEquals("3", new Caverphone().caverphone("l$"));
        //uncomment the following line if testing for 1.0 version
        //assertEquals("Ly", new Caverphone().caverphone("ly"));
        assertEquals("2", new Caverphone().caverphone("l"));
        //uncomment the following lines if testing for 1.0 version
        //assertEquals("y", new Caverphone().caverphone("j"));
        //assertEquals("Y3", new Caverphone().caverphone("y3"));
        //assertEquals("2", new Caverphone().caverphone("y"));

    }

    @Test
    public void testCaverphoneRemovals() {
        assertEquals("", new Caverphone().caverphone("2"));
        assertEquals("A", new Caverphone().caverphone("3$"));
        assertEquals("", new Caverphone().caverphone("3"));
    }

    @Test
    public void testCaverphoneEndOptions() {
        assertEquals("11111111111111", new Caverphone().caverphone("hello world 1234567890"));
    }

    @Test
    public void testCaverphoneEquals() {
        String str1 = "hello world";
        String str2 = "HELLO WORLD";
        String str3 = "hello world 1234567890";

        assertEquals(new Caverphone().caverphone(str1), new Caverphone().caverphone(str2));
        assertNotEquals(new Caverphone().caverphone(str1), new Caverphone().caverphone(str3));
    }
}