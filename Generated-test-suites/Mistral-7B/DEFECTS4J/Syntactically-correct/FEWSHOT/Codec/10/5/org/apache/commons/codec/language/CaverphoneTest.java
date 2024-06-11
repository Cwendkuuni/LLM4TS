package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphoneEncode() {
        Caverphone caverphone = new Caverphone();

        // Test empty string
        assertEquals("1111111111", caverphone.encode(""));

        // Test lowercase string
        assertEquals("QZVWX231210", caverphone.encode("apple"));
        assertEquals("GHBPR231210", caverphone.encode("banana"));
        assertEquals("SBYNQ231210", caverphone.encode("cat"));
        assertEquals("JPYFJ231210", caverphone.encode("dog"));
        assertEquals("ZMFLZ231210", caverphone.encode("elephant"));

        // Test various start options
        assertEquals("cou2f2mSioTsioT", caverphone.encode("cough"));
        assertEquals("rou2f2mSioTsioT", caverphone.encode("rough"));
        assertEquals("tou2f2mSioTsioT", caverphone.encode("tough"));
        assertEquals("enou2f2mSioTsioT", caverphone.encode("enough"));
        assertEquals("trou2f2mSioTsioT", caverphone.encode("trough"));
        assertEquals("2n2n", caverphone.encode("gn"));

        // Test various replacements
        assertEquals("2q2q", caverphone.encode("cq"));
        assertEquals("si3si3", caverphone.encode("ci"));
        assertEquals("se3se3", caverphone.encode("ce"));
        assertEquals("sy3sy3", caverphone.encode("cy"));
        assertEquals("2ch2ch", caverphone.encode("tch"));
        assertEquals("k3k3", caverphone.encode("c"));
        assertEquals("k3k3", caverphone.encode("q"));
        assertEquals("k3k3", caverphone.encode("x"));
        assertEquals("f3f3", caverphone.encode("v"));
        assertEquals("2g2g", caverphone.encode("dg"));
        assertEquals("sioTsioT", caverphone.encode("tio"));
        assertEquals("siaTsiaT", caverphone.encode("tia"));
        assertEquals("t3t3", caverphone.encode("d"));
        assertEquals("fh3fh3", caverphone.encode("ph"));
        assertEquals("p3p3", caverphone.encode("b"));
        assertEquals("s2s2", caverphone.encode("sh"));
        assertEquals("s3s3", caverphone.encode("z"));
        assertEquals("A3A3", caverphone.encode("h"));
        assertEquals("2A2A", caverphone.encode("h"));
        assertEquals("R3R3", caverphone.encode("r3"));
        assertEquals("33", caverphone.encode("r$"));
        //assertEquals("Wy3", caverphone.encode("wy")); // 1.0 only
        assertEquals("Wh3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w$")); // 2.0 only
        //assertEquals("Why", caverphone.encode("why")); // 1.0 only
        assertEquals("2", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("^h"));
        assertEquals("A", caverphone.encode("h"));
        assertEquals("R3", caverphone.encode("r3"));
        assertEquals("3", caverphone.encode("r$")); // 2.0 only
        //assertEquals("Ry", caverphone.encode("ry")); // 1.0 only
        assertEquals("2", caverphone.encode("r"));
        assertEquals("L3", caverphone.encode("l3"));
        assertEquals("3", caverphone.encode("l$")); // 2.0 only
        //assertEquals("Ly", caverphone.encode("ly")); // 1.0 only
        assertEquals("2", caverphone.encode("l"));
        //assertEquals("y", caverphone.encode("j")); // 1.0 only
        //assertEquals("Y3", caverphone.encode("y3")); // 1.0 only
        //assertEquals("y", caverphone.encode("y")); // 1.0 only
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
        //assertEquals("Wy", caverphone.encode("wy")); // 1.0 only
        assertEquals("Wh3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("^h"));
        assertEquals("2", caverphone.encode("h"));
        assertEquals("R3", caverphone.encode("r3"));
        assertEquals("3", caverphone.encode("r$")); // 2.0 only
        //assertEquals("Ry", caverphone.encode("ry")); // 1.0 only
        assertEquals("2", caverphone.encode("r"));
        assertEquals("L3", caverphone.encode("l3"));
        assertEquals("3", caverphone.encode("l$")); // 2.0 only
        //assertEquals("Ly", caverphone.encode("ly")); // 1.0 only
        assertEquals("2", caverphone.encode("l"));
        //assertEquals("y", caverphone.encode("j")); // 1.0 only
        //assertEquals("Y3", caverphone.encode("y3")); // 1.0 only
        //assertEquals("y", caverphone.encode("y")); // 1.0 only

        // Test various removals
        assertEquals("", caverphone.encode("2"));
        assertEquals("A", caverphone.encode("3$")); // 2.0 only
        assertEquals("", caverphone.encode("3"));
        assertEquals("11111111111111", caverphone.encode("111111111111"));

        // Test encode method
        StringEncoder encoder = new Caverphone();
        assertEquals("QZVWX231210", encoder.encode("apple"));
        assertEquals("GHBPR231210", encoder.encode("banana"));
        assertEquals("SBYNQ231210", encoder.encode("cat"));
        assertEquals("JPYFJ231210", encoder.encode("dog"));
        assertEquals("ZMFLZ231210", encoder.encode("elephant"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneEncodeException() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(new Object());
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertEquals(true, caverphone.isCaverphoneEqual("apple", "apple"));
        assertEquals(true, caverphone.isCaverphoneEqual("apple", "Apple"));
        assertEquals(true, caverphone.isCaverphoneEqual("banana", "Banana"));
        assertEquals(true, caverphone.isCaverphoneEqual("banana", "BanAnA"));
        assertEquals(false, caverphone.isCaverphoneEqual("apple", "banana"));
        assertEquals(false, caverphone.isCaverphoneEqual("apple", "dog"));
    }
}