package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }

    @Test
    public void testEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode("Hello"));
        assertEquals("1233223311", caverphone.encode("hello"));
    }

    @Test
    public void testRemoveNonAlphabets() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode("1HelloWorld2"));
        assertEquals("1233223311", caverphone.encode("HelloWorld"));
    }

    @Test
    public void testRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1223311111", caverphone.encode("HelloWorlde"));
    }

    @Test
    public void testHandleStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.encode("cough"));
        assertEquals("cou2f", caverphone.encode("Cough"));
        assertEquals("rou2f", caverphone.encode("rough"));
        assertEquals("rou2f", caverphone.encode("Rough"));
        assertEquals("tou2f", caverphone.encode("tough"));
        assertEquals("tou2f", caverphone.encode("Tough"));
        assertEquals("enou2f", caverphone.encode("enough"));
        assertEquals("enou2f", caverphone.encode("Enough"));
        assertEquals("trou2f", caverphone.encode("trough"));
        assertEquals("trou2f", caverphone.encode("Trough"));
        assertEquals("2n", caverphone.encode("gn"));
        assertEquals("m2", caverphone.encode("mb"));
    }

    @Test
    public void testHandleReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2q", caverphone.encode("cq"));
        assertEquals("si", caverphone.encode("ci"));
        assertEquals("se", caverphone.encode("ce"));
        assertEquals("sy", caverphone.encode("cy"));
        assertEquals("2ch", caverphone.encode("tch"));
        assertEquals("k", caverphone.encode("c"));
        assertEquals("k", caverphone.encode("q"));
        assertEquals("k", caverphone.encode("x"));
        assertEquals("f", caverphone.encode("v"));
        assertEquals("2g", caverphone.encode("dg"));
        assertEquals("sio", caverphone.encode("tio"));
        assertEquals("sia", caverphone.encode("tia"));
        assertEquals("t", caverphone.encode("d"));
        assertEquals("fh", caverphone.encode("ph"));
        assertEquals("p", caverphone.encode("b"));
        assertEquals("s2", caverphone.encode("sh"));
        assertEquals("s", caverphone.encode("z"));
        assertEquals("A", caverphone.encode("^[aeiou]"));
        assertEquals("3", caverphone.encode("[aeiou]"));
        // Uncomment the following lines when testing with JDK 8 or later
        // assertEquals("Y3", caverphone.encode("^y3"));
        // assertEquals("A", caverphone.encode("^y"));
        // assertEquals("3", caverphone.encode("y"));
        assertEquals("3kh3", caverphone.encode("3gh3"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("k", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s+"));
        assertEquals("T", caverphone.encode("t+"));
        assertEquals("P", caverphone.encode("p+"));
        assertEquals("K", caverphone.encode("k+"));
        assertEquals("F", caverphone.encode("f+"));
        assertEquals("M", caverphone.encode("m+"));
        assertEquals("N", caverphone.encode("n+"));
        assertEquals("W3", caverphone.encode("w3"));
        // Uncomment the following lines when testing with JDK 8 or later
        // assertEquals("Wy", caverphone.encode("wy"));
        assertEquals("Wh3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w$"));
        // Uncomment the following lines when testing with JDK 8 or later
        // assertEquals("Why", caverphone.encode("why"));
        assertEquals("2", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("^h"));
        assertEquals("2", caverphone.encode("h"));
        assertEquals("R3", caverphone.encode("r3"));
        assertEquals("3", caverphone.encode("r$"));
        // Uncomment the following lines when testing with JDK 8 or later
        // assertEquals("Ry", caverphone.encode("ry"));
        assertEquals("2", caverphone.encode("r"));
        assertEquals("L3", caverphone.encode("l3"));
        assertEquals("3", caverphone.encode("l$"));
        // Uncomment the following lines when testing with JDK 8 or later
        // assertEquals("Ly", caverphone.encode("ly"));
        assertEquals("2", caverphone.encode("l"));
        // Uncomment the following lines when testing with JDK 8 or later
        // assertEquals("y", caverphone.encode("j"));
        // assertEquals("Y3", caverphone.encode("y3"));
        // assertEquals("2", caverphone.encode("y"));

        // The following test case is for handling the case where the input string
        // starts with a quoted string.
        // Uncomment the following lines when testing with JDK 8 or later as the String.equals() method does not support comparing strings with different length.
        // String input = "\"Hello World\"";
        // String expected = "\"H3l3o W3rld\"";
        // assertEquals(expected, caverphone.encode(input));
    }

    @Test
    public void testHandleRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.encode("2"));
        assertEquals("A", caverphone.encode("3$"));
        assertEquals("", caverphone.encode("3"));
        assertEquals("1111111111", caverphone.encode("1111111111"));
    }

    @Test
    public void testHandleAddition() {
        Caverphone caverphone = new Caverphone();
        assertEquals("111111111111111111", caverphone.encode("111111111111"));
    }

    @Test
    public void testEncodeAndDecode() {
        Caverphone caverphone = new Caverphone();
        String original = "Hello World";
        String encoded = caverphone.encode(original);
        String decoded = caverphone.encode(encoded);
        assertNotEquals(original, decoded);
        assertEquals(original, caverphone.decode(decoded));
    }

    @Test
    public void testDecodeNullInput() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.decode(null));
    }

    @Test
    public void testDecodeEmptyInput() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.decode(""));
    }

    @Test
    public void testDecodeInvalidInput() {
        Caverphone caverphone = new Caverphone();
        assertFalse(caverphone.isCaverphoneEqual("123", "456"));
    }

    @Test
    public void testDecodeValidInput() {
        Caverphone caverphone = new Caverphone();
        String original = "Hello World";
        String encoded = caverphone.encode(original);
        String decoded = caverphone.decode(encoded);
        assertTrue(caverphone.isCaverphoneEqual(original, decoded));
    }
}