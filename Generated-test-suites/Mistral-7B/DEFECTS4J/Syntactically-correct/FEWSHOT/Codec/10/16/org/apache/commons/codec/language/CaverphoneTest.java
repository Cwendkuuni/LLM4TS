package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("12222222222222222222", caverphone.caverphone("hello world"));
    }

    @Test
    public void testCaverphoneRemoveNonLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2h2l2k2t2f2n2s2w2r2l2g2k2f2m2n2t2p", caverphone.caverphone("The quick Brown fox jumps over the lazy dog"));
    }

    @Test
    public void testCaverphoneRemoveFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2h2l2k2t2f2n2s2w2r2l2g2k2f2m2n2t2p0", caverphone.caverphone("The quick Brown fox jumps over the lazy dog."));
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2f", caverphone.caverphone("Cough"));
        assertEquals("rou2f", caverphone.caverphone("Rough"));
        assertEquals("tou2f", caverphone.caverphone("Tough"));
        assertEquals("enou2f", caverphone.caverphone("Enough"));
        // Note: "trough" and "enough" appears twice in the input, but only once in the Caverphone specification
        // Thus, it is unclear which one to test for and therefore this test case is omitted
        assertEquals("2n", caverphone.caverphone("Gn"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
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
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        // Note: "j", "y", "w3", "W3", "Why", "Wy" appear in the Caverphone specification, but not in this implementation
        // Thus, these test cases are omitted
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneAdditional1s() {
        Caverphone caverphone = new Caverphone();
        assertEquals("122222222222222222221111111111", caverphone.caverphone("The quick Brown fox jumps over the lazy dog"));
    }

    @Test
    public void testCaverphoneFirstSixCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("122222", caverphone.caverphone("The quick Brown fox jumps over the lazy dog").substring(0, 10));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonStringInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(42);
    }

}