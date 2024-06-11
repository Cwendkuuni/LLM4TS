package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("1111111111", caverphone.caverphone("123"));
        assertEquals("1111111111", caverphone.caverphone("A B C"));
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()_+"));
        assertEquals("1111111111", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("1111111111", caverphone.caverphone("Caversham Project"));

        assertEquals("kfkfkkfkff", caverphone.caverphone("Cough"));
        assertEquals("rkfkfkfkff", caverphone.caverphone("Rough"));
        assertEquals("tsfkfkfkff", caverphone.caverphone("Tough"));
        assertEquals("nffkfkfkff", caverphone.caverphone("Enough"));
        assertEquals("nkfkfkfkff", caverphone.caverphone("Trough"));
        assertEquals("ynn", caverphone.caverphone("Gn"));
        assertEquals("m2", caverphone.caverphone("Mb"));

        assertEquals("2qffkfkfkff", caverphone.caverphone("Cq"));
        assertEquals("sisfkfkfkff", caverphone.caverphone("Ci"));
        assertEquals("sesfkfkfkff", caverphone.caverphone("Ce"));
        assertEquals("sysfkfkfkff", caverphone.caverphone("Cy"));
        assertEquals("2chfkfkfkff", caverphone.caverphone("Tch"));
        assertEquals("kk", caverphone.caverphone("C"));
        assertEquals("kk", caverphone.caverphone("Q"));
        assertEquals("kk", caverphone.caverphone("X"));
        assertEquals("f", caverphone.caverphone("V"));
        assertEquals("2g", caverphone.caverphone("Dg"));
        assertEquals("siofkfkfkff", caverphone.caverphone("Tio"));
        assertEquals("siafkfkfkff", caverphone.caverphone("Tia"));
        assertEquals("t", caverphone.caverphone("D"));
        assertEquals("fh", caverphone.caverphone("Ph"));
        assertEquals("p", caverphone.caverphone("B"));
        assertEquals("s2", caverphone.caverphone("Sh"));
        assertEquals("s", caverphone.caverphone("Z"));
        assertEquals("A", caverphone.caverphone("A"));
        assertEquals("3", caverphone.caverphone("Eiou"));
        assertEquals("y", caverphone.caverphone("J"));
        assertEquals("A3", caverphone.caverphone("Y3"));
        assertEquals("A", caverphone.caverphone("Y"));
        assertEquals("3", caverphone.caverphone("Y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("Gh"));
        assertEquals("k", caverphone.caverphone("G"));
        assertEquals("S", caverphone.caverphone("S"));
        assertEquals("T", caverphone.caverphone("T"));
        assertEquals("P", caverphone.caverphone("P"));
        assertEquals("K", caverphone.caverphone("K"));
        assertEquals("F", caverphone.caverphone("F"));
        assertEquals("M", caverphone.caverphone("M"));
        assertEquals("N", caverphone.caverphone("N"));
        assertEquals("W3", caverphone.caverphone("W3"));
        assertEquals("Wh3", caverphone.caverphone("Wh3"));
        assertEquals("3", caverphone.caverphone("W"));
        assertEquals("2", caverphone.caverphone("H"));
        assertEquals("A3", caverphone.caverphone("R3"));
        assertEquals("3", caverphone.caverphone("R"));
        assertEquals("A3", caverphone.caverphone("L3"));
        assertEquals("3", caverphone.caverphone("L"));

        assertEquals("111111!", caverphone.caverphone("!!!!!"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("1111111111", caverphone.encode(" "));
        assertEquals("1111111111", caverphone.encode("123"));
        assertEquals("1111111111", caverphone.encode("A B C"));
        assertEquals("1111111111", caverphone.encode("!@#$%^&*()_+"));
        assertEquals("1111111111", caverphone.encode("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("1111111111", caverphone.encode("Caversham Project"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals(true, caverphone.isCaverphoneEqual(null, null));
        assertEquals(true, caverphone.isCaverphoneEqual("", ""));
        assertEquals(true, caverphone.isCaverphoneEqual(" ", " "));
        assertEquals(true, caverphone.isCaverphoneEqual("123", "123"));
        assertEquals(true, caverphone.isCaverphoneEqual("A B C", "A B C"));
        assertEquals(true, caverphone.isCaverphoneEqual("!@#$%^&*()_+", "!@#$%^&*()_+"));
        assertEquals(true, caverphone.isCaverphoneEqual("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"));
        assertEquals(true, caverphone.isCaverphoneEqual("Caversham Project", "Caversham Project"));

        assertEquals(false, caverphone.isCaverphoneEqual(null, ""));
        assertEquals(false, caverphone.isCaverphoneEqual("", " "));
        assertEquals(false, caverphone.isCaverphoneEqual("123", "!@#$%^&*()_+"));
        assertEquals(false, caverphone.isCaverphoneEqual("A B C", "abcdefghijklmnopqrstuvwxyz"));
        assertEquals(false, caverphone.isCaverphoneEqual("!@#$%^&*()_+", "Caversham Project"));
        assertEquals(false, caverphone.isCaverphoneEqual("abcdefghijklmnopqrstuvwxyz", "Caversham Project"));
    }
    
}