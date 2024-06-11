package org.apache.commons.codec.language;

import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmpty() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("A", caverphone.caverphone("a"));
        Assert.assertEquals("A", caverphone.caverphone("A"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("2f2f2f2f2f", caverphone.caverphone("caverphone"));
        Assert.assertEquals("2f2f2f2f2f2f2f2f2f", caverphone.caverphone("thisisatest"));
        Assert.assertEquals("2f2f2f2f2f2f2f2f2f2f2f2f2f", caverphone.caverphone("thisisatestisatest"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("cou2f", caverphone.caverphone("cough"));
        Assert.assertEquals("rou2f", caverphone.caverphone("rough"));
        Assert.assertEquals("tou2f", caverphone.caverphone("tough"));
        Assert.assertEquals("enou2f", caverphone.caverphone("enough"));
        Assert.assertEquals("trou2f", caverphone.caverphone("trough"));
        Assert.assertEquals("2n", caverphone.caverphone("gn"));
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("2q", caverphone.caverphone("cq"));
        Assert.assertEquals("si", caverphone.caverphone("ci"));
        Assert.assertEquals("se", caverphone.caverphone("ce"));
        Assert.assertEquals("sy", caverphone.caverphone("cy"));
        Assert.assertEquals("2ch", caverphone.caverphone("tch"));
        Assert.assertEquals("k", caverphone.caverphone("c"));
        Assert.assertEquals("k", caverphone.caverphone("q"));
        Assert.assertEquals("k", caverphone.caverphone("x"));
        Assert.assertEquals("f", caverphone.caverphone("v"));
        Assert.assertEquals("2g", caverphone.caverphone("dg"));
        Assert.assertEquals("sio", caverphone.caverphone("tio"));
        Assert.assertEquals("sia", caverphone.caverphone("tia"));
        Assert.assertEquals("t", caverphone.caverphone("d"));
        Assert.assertEquals("fh", caverphone.caverphone("ph"));
        Assert.assertEquals("p", caverphone.caverphone("b"));
        Assert.assertEquals("s2", caverphone.caverphone("sh"));
        Assert.assertEquals("s", caverphone.caverphone("z"));
        Assert.assertEquals("A", caverphone.caverphone("^[aeiou]"));
        Assert.assertEquals("3", caverphone.caverphone("[aeiou]"));
        Assert.assertEquals("y", caverphone.caverphone("j"));
        Assert.assertEquals("Y3", caverphone.caverphone("^y3"));
        Assert.assertEquals("A", caverphone.caverphone("^y"));
        Assert.assertEquals("3", caverphone.caverphone("y"));
        Assert.assertEquals("3kh3", caverphone.caverphone("3gh3"));
        Assert.assertEquals("22", caverphone.caverphone("gh"));
        Assert.assertEquals("k", caverphone.caverphone("g"));
        Assert.assertEquals("S", caverphone.caverphone("s+"));
        Assert.assertEquals("T", caverphone.caverphone("t+"));
        Assert.assertEquals("P", caverphone.caverphone("p+"));
        Assert.assertEquals("K", caverphone.caverphone("k+"));
        Assert.assertEquals("F", caverphone.caverphone("f+"));
        Assert.assertEquals("M", caverphone.caverphone("m+"));
        Assert.assertEquals("N", caverphone.caverphone("n+"));
        Assert.assertEquals("W3", caverphone.caverphone("w3"));
        Assert.assertEquals("W3", caverphone.caverphone("w$"));
        Assert.assertEquals("W", caverphone.caverphone("w"));
        Assert.assertEquals("A", caverphone.caverphone("^h"));
        Assert.assertEquals("2", caverphone.caverphone("h"));
        Assert.assertEquals("R3", caverphone.caverphone("r3"));
        Assert.assertEquals("3", caverphone.caverphone("r$"));
        Assert.assertEquals("L3", caverphone.caverphone("l3"));
        Assert.assertEquals("3", caverphone.caverphone("l$"));
        //Assert.assertEquals("Wy", caverphone.caverphone("wy"));   // 1.0 only
        Assert.assertEquals("2", caverphone.caverphone("w"));
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("", caverphone.caverphone("2"));
        Assert.assertEquals("A", caverphone.caverphone("3$"));
        Assert.assertEquals("", caverphone.caverphone("3"));
    }

    @Test (expected = EncoderException.class)
    public void testCaverphoneNonString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object());
    }

    @Test
    public void testCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String code1 = caverphone.caverphone("test");
        String code2 = caverphone.caverphone("test");
        Assert.assertTrue(caverphone.isCaverphoneEqual(code1, code2));
    }

    @Test
    public void testCaverphoneNotEqual() {
        Caverphone caverphone = new Caverphone();
        String code1 = caverphone.caverphone("test1");
        String code2 = caverphone.caverphone("test2");
        Assert.assertFalse(caverphone.isCaverphoneEqual(code1, code2));
    }
}