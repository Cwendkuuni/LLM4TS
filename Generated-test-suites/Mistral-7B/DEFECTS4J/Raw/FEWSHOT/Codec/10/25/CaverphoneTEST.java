package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals("A2A2A2A2A2A2", caverphone.caverphone("A B c d E f g h I J"));
    }

    @Test
    public void testCaverphoneRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A2A2A2A2A2A2", caverphone.caverphone("A B c dE f g h I J 1 2 3"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A2A2A2A2A2A", caverphone.caverphone("A B c dE f g h I J e"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("trou2f", caverphone.caverphone("trough"));
        assertEquals("2n", caverphone.caverphone("gn"));
    }

    @Test
    public void testCaverphoneHandlingReplacements() {
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
        assertEquals("3", caverphone.caverphone("r$"));
        assertEquals("A", caverphone.caverphone("^y"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s+"));
        assertEquals("T", caverphone.caverphone("t+"));
        assertEquals("P", caverphone.caverphone("p+"));
        assertEquals("K", caverphone.caverphone("k+"));
        assertEquals("F", caverphone.caverphone("f+"));
        assertEquals("M", caverphone.caverphone("m+"));
        assertEquals("N", caverphone.caverphone("n+"));
        assertEquals("W3", caverphone.caverphone("w3"));
        //assertEquals("Wy", caverphone.caverphone("wy"));    // 1.0 only
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w$"));
        //assertEquals("Why", caverphone.caverphone("why"));  // 1.0 only
        assertEquals("2", caverphone.caverphone("w"));
    }

    @Test
    public void testCaverphoneHandlingRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneAddOnes() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111111111111", caverphone.caverphone("12345"));
    }

    @Test
    public void testCaverphoneFirstSixCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone("1234567890"));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        String str = "1234567890";
        try {
            assertEquals(caverphone.encode(str), caverphone.caverphone(str));
        } catch (EncoderException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "1234567890";
        String str2 = "1234567890";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));

        String str3 = "1234567891";
        assertFalse(caverphone.isCaverphoneEqual(str1, str3));
    }
}