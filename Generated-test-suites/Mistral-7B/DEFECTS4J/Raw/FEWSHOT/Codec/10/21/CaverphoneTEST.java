package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphoneEmptyString() {
        Caverphone cp = new Caverphone();
        assertEquals("1111111111", cp.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleCharacter() {
        Caverphone cp = new Caverphone();
        assertEquals("A", cp.caverphone("a"));
    }

    @Test
    public void testCaverphoneLowercase() {
        Caverphone cp = new Caverphone();
        assertEquals("2222222222", cp.caverphone("apple"));
        assertEquals("2222222222", cp.caverphone("banana"));
        assertEquals("2222222222", cp.caverphone("cherry"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone cp = new Caverphone();
        assertEquals("cou2f", cp.caverphone("cough"));
        assertEquals("cou2f", cp.caverphone("Cough"));
        assertEquals("cou2f", cp.caverphone("COUGH"));
        assertEquals("cou2f", cp.caverphone("COUGH"));
        assertEquals("rou2f", cp.caverphone("rough"));
        assertEquals("rou2f", cp.caverphone("Rough"));
        assertEquals("rou2f", cp.caverphone("ROUGH"));
        assertEquals("rou2f", cp.caverphone("ROUGH"));
        assertEquals("tou2f", cp.caverphone("tough"));
        assertEquals("tou2f", cp.caverphone("TOUGH"));
        assertEquals("tou2f", cp.caverphone("TOUGH"));
        assertEquals("tou2f", cp.caverphone("TOUGH"));
        assertEquals("enou2f", cp.caverphone("enough"));
        assertEquals("enou2f", cp.caverphone("Enough"));
        assertEquals("enou2f", cp.caverphone("ENOUGH"));
        assertEquals("enou2f", cp.caverphone("ENough"));
        assertEquals("trou2f", cp.caverphone("trough"));
        assertEquals("trou2f", cp.caverphone("Trough"));
        assertEquals("trou2f", cp.caverphone("TRough"));
        assertEquals("trou2f", cp.caverphone("TRough"));
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone cp = new Caverphone();
        assertEquals("2q", cp.caverphone("cq"));
        assertEquals("si", cp.caverphone("ci"));
        assertEquals("se", cp.caverphone("ce"));
        assertEquals("sy", cp.caverphone("cy"));
        assertEquals("2ch", cp.caverphone("tch"));
        assertEquals("k", cp.caverphone("c"));
        assertEquals("k", cp.caverphone("q"));
        assertEquals("k", cp.caverphone("x"));
        assertEquals("f", cp.caverphone("v"));
        assertEquals("2g", cp.caverphone("dg"));
        assertEquals("sio", cp.caverphone("tio"));
        assertEquals("sia", cp.caverphone("tia"));
        assertEquals("t", cp.caverphone("d"));
        assertEquals("fh", cp.caverphone("ph"));
        assertEquals("p", cp.caverphone("b"));
        assertEquals("s2", cp.caverphone("sh"));
        assertEquals("s", cp.caverphone("z"));
        assertEquals("A", cp.caverphone("h"));
        assertEquals("A", cp.caverphone("3gh3"));
        assertEquals("k", cp.caverphone("gh"));
        assertEquals("S", cp.caverphone("s+"));
        assertEquals("T", cp.caverphone("t+"));
        assertEquals("P", cp.caverphone("p+"));
        assertEquals("K", cp.caverphone("k+"));
        assertEquals("F", cp.caverphone("f+"));
        assertEquals("M", cp.caverphone("m+"));
        assertEquals("N", cp.caverphone("n+"));
        assertEquals("W3", cp.caverphone("w3"));
        // Uncomment the following lines for JUnit 4.12 or earlier
        // assertEquals("Wy", cp.caverphone("wy"));
        // assertEquals("Why", cp.caverphone("why"));
        assertEquals("Wh3", cp.caverphone("wh3"));
        assertEquals("3", cp.caverphone("w"));
        assertEquals("A", cp.caverphone("w"));
        assertEquals("A", cp.caverphone("h"));
        assertEquals("A", cp.caverphone("r3"));
        assertEquals("A", cp.caverphone("r"));
        assertEquals("L3", cp.caverphone("l3"));
        assertEquals("L3", cp.caverphone("l"));
        // Uncomment the following lines for JUnit 4.12 or earlier
        // assertEquals("Ly", cp.caverphone("ly"));
        // assertEquals("Ly", cp.caverphone("ly"));
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone cp = new Caverphone();
        assertEquals("", cp.caverphone("2"));
        assertEquals("A", cp.caverphone("3$"));
        assertEquals("", cp.caverphone("3"));
    }

    @Test
    public void testCaverphoneEnd() {
        Caverphone cp = new Caverphone();
        assertEquals("11111111111111", cp.caverphone("apple banana cherry"));
    }

    @Test
    public void testCaverphoneCaseSensitive() {
        Caverphone cp = new Caverphone();
        String str1 = "Apple Banana Cherry";
        String str2 = "apple banana cherry";
        assertEquals(cp.caverphone(str1), cp.caverphone(str2));
        assertNotEquals(str1, str2);
    }
}