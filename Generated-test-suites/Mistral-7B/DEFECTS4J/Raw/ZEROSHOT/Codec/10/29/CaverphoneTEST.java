package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Caverphone.*;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        String encoded = encode("");
        assertEquals("1111111111", encoded);
    }

    @Test
    public void testSingleCharacterString() {
        String encoded = encode("a");
        assertEquals("2", encoded);
    }

    @Test
    public void testMultipleCharactersString() {
        String encoded = encode("hello");
        assertEquals("h2e2l2o", encoded);
    }

    @Test
    public void testSpecialCharsString() {
        String encoded = encode("hello#world");
        assertEquals("h2e2l2o2w2l2d", encoded);
    }

    @Test
    public void testStartsWithPatterns() {
        String encoded = encode("cough");
        assertEquals("cou2f", encoded);

        encoded = encode("rough");
        assertEquals("rou2f", encoded);

        encoded = encode("tough");
        assertEquals("tou2f", encoded);

        encoded = encode("enough");
        assertEquals("enou2f", encoded);

        encoded = encode("trough");
        assertEquals("trou2f", encoded);

        encoded = encode("gn");
        assertEquals("2n", encoded);
    }

    @Test
    public void testMiddleReplacements() {
        String encoded = encode("cq");
        assertEquals("2q", encoded);

        encoded = encode("ci");
        assertEquals("si", encoded);

        encoded = encode("ce");
        assertEquals("se", encoded);

        encoded = encode("cy");
        assertEquals("sy", encoded);

        encoded = encode("tch");
        assertEquals("2ch", encoded);

        encoded = encode("c");
        assertEquals("k", encoded);

        encoded = encode("q");
        assertEquals("k", encoded);

        encoded = encode("x");
        assertEquals("k", encoded);

        encoded = encode("v");
        assertEquals("f", encoded);

        encoded = encode("dg");
        assertEquals("2g", encoded);

        encoded = encode("tio");
        assertEquals("sio", encoded);

        encoded = encode("tia");
        assertEquals("sia", encoded);

        encoded = encode("d");
        assertEquals("t", encoded);

        encoded = encode("ph");
        assertEquals("fh", encoded);

        encoded = encode("b");
        assertEquals("p", encoded);

        encoded = encode("sh");
        assertEquals("s2", encoded);

        encoded = encode("z");
        assertEquals("s", encoded);

        encoded = encode("w3");
        assertEquals("W3", encoded);

        encoded = encode("wh3");
        assertEquals("Wh3", encoded);

        encoded = encode("w");
        assertEquals("2", encoded);

        encoded = encode("h");
        assertEquals("2", encoded);

        encoded = encode("r3");
        assertEquals("R3", encoded);

        encoded = encode("r");
        assertEquals("2", encoded);

        encoded = encode("l3");
        assertEquals("L3", encoded);

        encoded = encode("l");
        assertEquals("2", encoded);

        encoded = encode("j");
        assertEquals("y", encoded);

        encoded = encode("y3");
        assertEquals("Y3", encoded);

        encoded = encode("y");
        assertEquals("3", encoded);

        encoded = encode("3gh3");
        assertEquals("3kh3", encoded);

        encoded = encode("gh");
        assertEquals("22", encoded);

        encoded = encode("g");
        assertEquals("k", encoded);

        encoded = encode("s+");
        assertEquals("S", encoded);

        encoded = encode("t+");
        assertEquals("T", encoded);

        encoded = encode("p+");
        assertEquals("P", encoded);

        encoded = encode("k+");
        assertEquals("K", encoded);

        encoded = encode("f+");
        assertEquals("F", encoded);

        encoded = encode("m+");
        assertEquals("M", encoded);

        encoded = encode("n+");
        assertEquals("N", encoded);
    }

    @Test
    public void testEndReplacements() {
        String encoded = encode("2");
        assertEquals("", encoded);

        encoded = encode("3$");
        assertEquals("A", encoded);

        encoded = encode("3");
        assertEquals("", encoded);

        encoded = encode("1111111111");
        assertEquals("111111", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        encode(null);
    }

    @Test
    public void testIsCaverphoneEqual() {
        String str1 = "hello";
        String str2 = "hello";
        assertTrue(isCaverphoneEqual(str1, str2));

        str1 = "hello";
        str2 = "world";
        assertFalse(isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testCaverphoneEncodeDecode() {
        String encoded = encode("hello");
        String decoded = encode(decode(encoded));
        assertEquals(encoded, decoded);
    }
}