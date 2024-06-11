package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {

    private static final Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphoneNull() {
        try {
            caverphone.encode(null);
            fail("Expected EncoderException for null input");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testCaverphoneEmpty() {
        assertEquals("1111111111", caverphone.encode(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        assertEquals("A", caverphone.encode("a"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        assertEquals("KF22S2T2KF", caverphone.encode("CavershamProject"));
        assertEquals("2KF2S2T2KF", caverphone.encode("cavershamproject"));
        assertEquals("KF22S2T2KF", caverphone.encode("CaVersHamProjEct"));
        assertEquals("KF22S2T2KF", caverphone.encode("Caversham Project"));
        assertEquals("KF22S2T2KF", caverphone.encode("CAVERSHAM PROJECT"));
    }

    @Test
    public void testCaverphoneSpecificCases() {
        assertEquals("A", caverphone.encode("a"));
        assertEquals("CKF3S2T2PKF", caverphone.encode("Cough"));
        assertEquals("CR2F2S2T2PF", caverphone.encode("Rough"));
        assertEquals("C2F2S2T2PF", caverphone.encode("tough"));
        assertEquals("C2F2S2T2PF", caverphone.encode("enough"));
        assertEquals("CR2F2S2T2PF", caverphone.encode("trough"));
        assertEquals("CN22S2T2KF", caverphone.encode("gn"));
        assertEquals("M2S", caverphone.encode("mb"));

        assertEquals("2Q", caverphone.encode("cq"));
        assertEquals("SI", caverphone.encode("ci"));
        assertEquals("SE", caverphone.encode("ce"));
        assertEquals("SY", caverphone.encode("cy"));
        assertEquals("2CH", caverphone.encode("tch"));
        assertEquals("K", caverphone.encode("c"));
        assertEquals("K", caverphone.encode("q"));
        assertEquals("K", caverphone.encode("x"));
        assertEquals("F", caverphone.encode("v"));
        assertEquals("2G", caverphone.encode("dg"));
        assertEquals("S2IO", caverphone.encode("tio"));
        assertEquals("SIA", caverphone.encode("tia"));
        assertEquals("T", caverphone.encode("d"));
        assertEquals("FH", caverphone.encode("ph"));
        assertEquals("P", caverphone.encode("b"));
        assertEquals("S2", caverphone.encode("sh"));
        assertEquals("S", caverphone.encode("z"));
        assertEquals("A", caverphone.encode("h"));
        assertEquals("3", caverphone.encode("^[aeiou]"));
        assertEquals("Y3", caverphone.encode("^y3"));
        assertEquals("A", caverphone.encode("^y"));
        assertEquals("3", caverphone.encode("y"));
        assertEquals("3KH3", caverphone.encode("3gh3"));
        assertEquals("22", caverphone.encode("gh"));
        assertEquals("K", caverphone.encode("g"));
        assertEquals("S", caverphone.encode("s+"));
        assertEquals("S", caverphone.encode("t+"));
        assertEquals("P", caverphone.encode("p+"));
        assertEquals("K", caverphone.encode("k+"));
        assertEquals("F", caverphone.encode("f+"));
        assertEquals("M", caverphone.encode("m+"));
        assertEquals("N", caverphone.encode("n+"));
        assertEquals("W3", caverphone.encode("w3"));
        // assertEquals("Wy", caverphone.encode("wy"));
        assertEquals("WH3", caverphone.encode("wh3"));
        assertEquals("3", caverphone.encode("w$"));
        // assertEquals("Why", caverphone.encode("why"));
        assertEquals("2", caverphone.encode("w"));
        assertEquals("A", caverphone.encode("^h"));
        assertEquals("2", caverphone.encode("h"));
        assertEquals("R3", caverphone.encode("r3"));
        assertEquals("3", caverphone.encode("r$"));
        // assertEquals("Ry", caverphone.encode("ry"));
        assertEquals("2", caverphone.encode("r"));
        assertEquals("L3", caverphone.encode("l3"));
        assertEquals("3", caverphone.encode("l$"));
        // assertEquals("Ly", caverphone.encode("ly"));
        assertEquals("2", caverphone.encode("l"));
        // assertEquals("j", caverphone.encode("j"));
        // assertEquals("Y3", caverphone.encode("y3"));
        // assertEquals("y", caverphone.encode("y"));

        assertEquals("", caverphone.encode("2"));
        assertEquals("A", caverphone.encode("3$"));
        assertEquals("", caverphone.encode("3"));

        assertEquals("11111111111111", caverphone.encode("Caversham Project"));
        assertEquals("111111111111", caverphone.encode("Caversham Project").substring(0, 16));
    }

    @Test
    public void testCaverphoneEncodeDecode() {
        String encoded = caverphone.encode("Caversham Project");
        String decoded = caverphone.encode(caverphone.decode(encoded));
        assertEquals(caverphone.encode("Caversham Project"), decoded);
    }

    @Test
    public void testCaverphoneEncodeDecodeCaseInsensitive() {
        String encoded = caverphone.encode("Caversham Project");
        String decoded = caverphone.encode(caverphone.decode(encoded.toLowerCase()));
        assertEquals(caverphone.encode("Caversham Project"), decoded);

        String encoded2 = caverphone.encode("CAVERSHAM PROJECT");
        String decoded2 = caverphone.encode(caverphone.decode(encoded2.toUpperCase()));
        assertEquals(caverphone.encode("CAVERSHAM PROJECT"), decoded2);
    }

    @Test
    public void testCaverphoneEncodeDifferentLetters() {
        assertEquals("11111111111111", caverphone.encode("Caversham Project"));
        assertEquals("11111111111111", caverphone.encode("CaversHamProjeCt"));
        assertNotEquals("11111111111111", caverphone.encode("Caversham Project"));
        assertNotEquals("11111111111111", caverphone.encode("Caversham Projecta"));
        assertNotEquals("11111111111111", caverphone.encode("Caversham Project!"));
    }

    @Test
    public void testCaverphoneEncodeDifferentLength() {
        assertEquals("11111111111111", caverphone.encode("Caversham Project"));
        assertEquals("111111111111", caverphone.encode("Caversham Proj"));
        assertNotEquals("11111111111111", caverphone.encode("Caversham Proj"));
    }
}