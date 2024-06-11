package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        Caverphone encoder = new Caverphone();
        encoder.encode(null);
    }

    @Test
    public void testEmptyInput() {
        Caverphone encoder = new Caverphone();
        assertEquals("1111111111", encoder.encode(""));
    }

    @Test
    public void testLowercaseInput() {
        Caverphone encoder = new Caverphone();
        assertEquals("AAAABBBBCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", encoder.encode("The quick brown fox jumps over the lazy dog"));
    }

    @Test
    public void testHandleVariousStartOptions() {
        Caverphone encoder = new Caverphone();
        assertEquals("cou2f", encoder.encode("cough"));
        assertEquals("rou2f", encoder.encode("rough"));
        assertEquals("tou2f", encoder.encode("tough"));
        assertEquals("enou2f", encoder.encode("enough"));
        assertEquals("trou2f", encoder.encode("trough"));
        assertEquals("2n", encoder.encode("gn"));
    }

    @Test
    public void testHandleReplacements() {
        Caverphone encoder = new Caverphone();
        assertEquals("2q", encoder.encode("cq"));
        assertEquals("si", encoder.encode("ci"));
        assertEquals("se", encoder.encode("ce"));
        assertEquals("sy", encoder.encode("cy"));
        assertEquals("2ch", encoder.encode("tch"));
        assertEquals("k", encoder.encode("c"));
        assertEquals("k", encoder.encode("q"));
        assertEquals("k", encoder.encode("x"));
        assertEquals("f", encoder.encode("v"));
        assertEquals("2g", encoder.encode("dg"));
        assertEquals("sio", encoder.encode("tio"));
        assertEquals("sia", encoder.encode("tia"));
        assertEquals("t", encoder.encode("d"));
        assertEquals("fh", encoder.encode("ph"));
        assertEquals("p", encoder.encode("b"));
        assertEquals("s2", encoder.encode("sh"));
        assertEquals("s", encoder.encode("z"));
        assertEquals("A", encoder.encode("^[aeiou]"));
        assertEquals("3", encoder.encode("[aeiou]"));
        // Uncomment the following lines to test JVM 1.8 support for Unicode character 'y' and its variants
        //assertEquals("Y3", encoder.encode("^y3"));
        //assertEquals("A", encoder.encode("^y"));
        //assertEquals("3", encoder.encode("y"));
        assertEquals("3kh3", encoder.encode("3gh3"));
        assertEquals("22", encoder.encode("gh"));
        assertEquals("k", encoder.encode("g"));
        assertEquals("S", encoder.encode("s+"));
        assertEquals("T", encoder.encode("t+"));
        assertEquals("P", encoder.encode("p+"));
        assertEquals("K", encoder.encode("k+"));
        assertEquals("F", encoder.encode("f+"));
        assertEquals("M", encoder.encode("m+"));
        assertEquals("N", encoder.encode("n+"));
        assertEquals("W3", encoder.encode("w3"));
        // Uncomment the following lines to test JVM 1.8 support for Unicode character 'Wy'
        //assertEquals("Wy", encoder.encode("wy"));
        assertEquals("Wh3", encoder.encode("wh3"));
        assertEquals("3", encoder.encode("w"));
        assertEquals("A", encoder.encode("^h"));
        assertEquals("2", encoder.encode("h"));
        assertEquals("R3", encoder.encode("r3"));
        assertEquals("3", encoder.encode("r$"));
        // Uncomment the following lines to test JVM 1.8 support for Unicode character 'Ry'
        //assertEquals("Ry", encoder.encode("ry"));
        assertEquals("2", encoder.encode("r"));
        assertEquals("L3", encoder.encode("l3"));
        assertEquals("3", encoder.encode("l$"));
        // Uncomment the following lines to test JVM 1.8 support for Unicode character 'Ly'
        //assertEquals("Ly", encoder.encode("ly"));
        assertEquals("2", encoder.encode("l"));
        // Uncomment the following lines to test JVM 1.8 support for Unicode character 'y'
        //assertEquals("y", encoder.encode("j"));
        //assertEquals("Y3", encoder.encode("Y3"));
        //assertEquals("2", encoder.encode("y"));

        // Test handling of multiple consecutive characters
        assertEquals("AATTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", encoder.encode("the quick brown fox jumps over the lazy dog"));
    }

    @Test
    public void testHandleRemovals() {
        Caverphone encoder = new Caverphone();
        assertEquals("", encoder.encode("2"));
        assertEquals("A", encoder.encode("3$"));
        assertEquals("", encoder.encode("3"));
    }

    @Test
    public void testHandleAddition() {
        Caverphone encoder = new Caverphone();
        assertEquals("11111111111111", encoder.encode("The quick brown fox jumps over the lazy dog"));
    }

    @Test
    public void testHandleSubstring() {
        Caverphone encoder = new Caverphone();
        assertEquals("AAAABBBBCCCCCCCCCCCCC", encoder.encode("The quick brown fox jumps over the lazy dog").substring(0, 10));
    }

    @Test
    public void testHandleEqualStrings() {
        Caverphone encoder = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "The quick brown fox jumps over the lazy dog";
        assertEquals(encoder.encode(str1), encoder.encode(str2));
    }

    @Test
    public void testHandleNotEqualStrings() {
        Caverphone encoder = new Caverphone();
        String str1 = "The quick brown fox jumps over the lazy dog";
        String str2 = "The quick brown fox jumps over the lazy cat";
        assertNotEquals(encoder.encode(str1), encoder.encode(str2));
    }
}