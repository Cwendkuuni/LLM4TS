package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        assertEquals("1111111111", new Caverphone().caverphone(""));
    }

    @Test
    public void testCaverphoneNullString() {
        assertNull(new Caverphone().caverphone(null));
    }

    @Test
    public void testCaverphoneLowerCase() {
        assertEquals("1111111111", new Caverphone().caverphone("hello world"));
        assertEquals("1111111111", new Caverphone().caverphone("HELLO WORLD"));
    }

    @Test
    public void testCaverphoneRemoveNonLetters() {
        assertEquals("1111111111", new Caverphone().caverphone("123hello world456"));
        assertEquals("1111111111", new Caverphone().caverphone("123hello_world456"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        assertEquals("enou2f111111", new Caverphone().caverphone("enough"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        assertEquals("cou2f111111", new Caverphone().caverphone("cough"));
        assertEquals("rou2f111111", new Caverphone().caverphone("rough"));
        assertEquals("tou2f111111", new Caverphone().caverphone("tough"));
        assertEquals("enou2f111111", new Caverphone().caverphone("enough"));
        assertEquals("trou2f111111", new Caverphone().caverphone("trough"));
        assertEquals("gn111111", new Caverphone().caverphone("gn"));
    }

    @Test
    public void testCaverphoneHandleReplacements() {
        assertEquals("2q111111", new Caverphone().caverphone("cq"));
        assertEquals("si111111", new Caverphone().caverphone("ci"));
        assertEquals("se111111", new Caverphone().caverphone("ce"));
        assertEquals("sy111111", new Caverphone().caverphone("cy"));
        assertEquals("2ch111111", new Caverphone().caverphone("tch"));
        assertEquals("k111111", new Caverphone().caverphone("c"));
        assertEquals("k111111", new Caverphone().caverphone("q"));
        assertEquals("k111111", new Caverphone().caverphone("x"));
        assertEquals("f111111", new Caverphone().caverphone("v"));
        assertEquals("2g111111", new Caverphone().caverphone("dg"));
        assertEquals("sio111111", new Caverphone().caverphone("tio"));
        assertEquals("sia111111", new Caverphone().caverphone("tia"));
        assertEquals("t111111", new Caverphone().caverphone("d"));
        assertEquals("fh111111", new Caverphone().caverphone("ph"));
        assertEquals("p111111", new Caverphone().caverphone("b"));
        assertEquals("s211111", new Caverphone().caverphone("sh"));
        assertEquals("s111111", new Caverphone().caverphone("z"));
        assertEquals("A111111", new Caverphone().caverphone(""));
        assertEquals("333333", new Caverphone().caverphone("eeeee"));
        assertEquals("311111", new Caverphone().caverphone("y3"));
        assertEquals("A311111", new Caverphone().caverphone("Y3"));
        assertEquals("A111111", new Caverphone().caverphone("A"));
        assertEquals("311111", new Caverphone().caverphone("w3"));
        assertEquals("Wh311111", new Caverphone().caverphone("wh3"));
        assertEquals("311111", new Caverphone().caverphone("w"));
        assertEquals("A111111", new Caverphone().caverphone("h"));
        assertEquals("211111", new Caverphone().caverphone("h"));
        assertEquals("R311111", new Caverphone().caverphone("r3"));
        assertEquals("311111", new Caverphone().caverphone("r"));
        assertEquals("L311111", new Caverphone().caverphone("l3"));
        assertEquals("311111", new Caverphone().caverphone("l"));
    }

    @Test
    public void testCaverphoneHandleRemovals() {
        assertEquals("1111111111", new Caverphone().caverphone("2"));
        assertEquals("A111111", new Caverphone().caverphone("3$"));
        assertEquals("1111111111", new Caverphone().caverphone("3"));
    }

    @Test
    public void testCaverphoneAddZeros() {
        assertEquals("111111m2T2P2K2F2M2N2W3Wh33", new Caverphone().caverphone("mthephoneislongandnarrow"));
    }

    @Test
    public void testCaverphoneCompareStrings() {
        String str1 = "mthephoneislongandnarrow";
        String str2 = "thephoneismlongandnarrow";       
        assertEquals(new Caverphone().isCaverphoneEqual(str1, str2), false);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneEncodeNullObject() {
        new Caverphone().encode(null);
    }

    @Test
    public void testCaverphoneEncodeObjectToString() {
        assertEquals("1111111111", new Caverphone().encode(""));
    }

}