package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testNullInput() {
        Metaphone metaphone = new Metaphone();
        assertNull(metaphone.metaphone(null));
    }

    @Test
    public void testEmptyInput() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));
        // test for uppercase character
        assertEquals("A", metaphone.metaphone("a"));
    }

    @Test
    public void testInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.metaphone("KN"));
        assertEquals("AE", metaphone.metaphone("AE"));
        assertEquals("WR", metaphone.metaphone("WR"));
        assertEquals("WH", metaphone.metaphone("WH"));
        assertEquals("S", metaphone.metaphone("X"));
    }

    @Test
    public void testInitialK() {
        Metaphone metaphone = new Metaphone();
        assertEquals("K", metaphone.metaphone("K"));
    }

    @Test
    public void testInitialC() {
        Metaphone metaphone = new Metaphone();
        assertEquals("S", metaphone.metaphone("C"));
    }

    @Test
    public void testInitialW() {
        Metaphone metaphone = new Metaphone();
        assertEquals("W", metaphone.metaphone("W"));
    }

    @Test
    public void testInitialG() {
        Metaphone metaphone = new Metaphone();
        assertEquals("G", metaphone.metaphone("G"));
    }

    @Test
    public void testInitialX() {
        Metaphone metaphone = new Metaphone();
        assertEquals("S", metaphone.metaphone("X"));
    }

    @Test
    public void testDoubleCharacter() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AA", metaphone.metaphone("AA"));
        assertEquals("BB", metaphone.metaphone("BB"));
        assertEquals("CC", metaphone.metaphone("CC"));
        // test for uppercase character
        assertEquals("AA", metaphone.metaphone("aa"));
        assertEquals("BB", metaphone.metaphone("bb"));
        assertEquals("CC", metaphone.metaphone("cc"));
    }

    @Test
    public void testInitialKN() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.metaphone("KN"));
    }

    @Test
    public void testInitialAE() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AE", metaphone.metaphone("AE"));
    }

    @Test
    public void testInitialWR() {
        Metaphone metaphone = new Metaphone();
        assertEquals("R", metaphone.metaphone("WR"));
        assertEquals("W", metaphone.metaphone("WR"));
    }

    @Test
    public void testInitialWH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("W", metaphone.metaphone("WH"));
    }

    @Test
    public void testInitialCIA() {
        Metaphone metaphone = new Metaphone();
        assertEquals("X", metaphone.metaphone("CIA"));
    }

    @Test
    public void testInitialSCI() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone("SCI"));
    }

    @Test
    public void testInitialSCY() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone("SCY"));
    }

    @Test
    public void testInitialSCE() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone("SCE"));
    }

    @Test
    public void testInitialCH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("K", metaphone.metaphone("CH"));
    }

    @Test
    public void testInitialCHvowel() {
        Metaphone metaphone = new Metaphone();
        assertEquals("X", metaphone.metaphone("CHe"));
    }

    @Test
    public void testInitialDGE() {
        Metaphone metaphone = new Metaphone();
        assertEquals("J", metaphone.metaphone("DGE"));
    }

    @Test
    public void testInitialDGI() {
        Metaphone metaphone = new Metaphone();
        assertEquals("J", metaphone.metaphone("DGI"));
    }

    @Test
    public void testInitialDGY() {
        Metaphone metaphone = new Metaphone();
        assertEquals("J", metaphone.metaphone("DGY"));
    }

    @Test
    public void testSilentG() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone("GN"));
        assertEquals("", metaphone.metaphone("GNED"));
    }

    @Test
    public void testInitialGH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("K", metaphone.metaphone("GH"));
    }

    @Test
    public void testInitialGHvowel() {
        Metaphone metaphone = new Metaphone();
        assertEquals("X", metaphone.metaphone("GHi"));
    }

    @Test
    public void testInitialTH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("0", metaphone.metaphone("TH"));
    }

    @Test
    public void testMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertEquals(true, metaphone.isMetaphoneEqual("KN", "KN"));
        assertEquals(true, metaphone.isMetaphoneEqual("AE", "AE"));
        assertEquals(true, metaphone.isMetaphoneEqual("WR", "WR"));
        assertEquals(true, metaphone.isMetaphoneEqual("WH", "WH"));
        assertEquals(true, metaphone.isMetaphoneEqual("CIA", "CIA"));
        assertEquals(true, metaphone.isMetaphoneEqual("SCI", "SCI"));
        assertEquals(true, metaphone.isMetaphoneEqual("SCY", "SCY"));
        assertEquals(true, metaphone.isMetaphoneEqual("SCE", "SCE"));
        assertEquals(true, metaphone.isMetaphoneEqual("CH", "CH"));
        assertEquals(true, metaphone.isMetaphoneEqual("CHe", "CHe"));
        assertEquals(true, metaphone.isMetaphoneEqual("DGE", "DGE"));
        assertEquals(true, metaphone.isMetaphoneEqual("DGI", "DGI"));
        assertEquals(true, metaphone.isMetaphoneEqual("DGY", "DGY"));
        assertEquals(true, metaphone.isMetaphoneEqual("GN", "GN"));
        assertEquals(true, metaphone.isMetaphoneEqual("GNED", "GNED"));
        assertEquals(true, metaphone.isMetaphoneEqual("GH", "GH"));
        assertEquals(true, metaphone.isMetaphoneEqual("GHi", "GHi"));
        assertEquals(true, metaphone.isMetaphoneEqual("TH", "TH"));

        assertEquals(false, metaphone.isMetaphoneEqual("KN", "K"));
        assertEquals(false, metaphone.isMetaphoneEqual("AE", "E"));
        assertEquals(false, metaphone.isMetaphoneEqual("WR", "W"));
        assertEquals(false, metaphone.isMetaphoneEqual("WH", "H"));
        assertEquals(false, metaphone.isMetaphoneEqual("CIA", "CI"));
        assertEquals(false, metaphone.isMetaphoneEqual("SCI", "S"));
        assertEquals(false, metaphone.isMetaphoneEqual("SCY", "S"));
        assertEquals(false, metaphone.isMetaphoneEqual("SCE", "S"));
        assertEquals(false, metaphone.isMetaphoneEqual("CH", "C"));
        assertEquals(false, metaphone.isMetaphoneEqual("CHe", "Ce"));
        assertEquals(false, metaphone.isMetaphoneEqual("DGE", "D"));
        assertEquals(false, metaphone.isMetaphoneEqual("DGI", "D"));
        assertEquals(false, metaphone.isMetaphoneEqual("DGY", "D"));
        assertEquals(false, metaphone.isMetaphoneEqual("GN", "G"));
        assertEquals(false, metaphone.isMetaphoneEqual("GNED", "GN"));
        assertEquals(false, metaphone.isMetaphoneEqual("GH", "G"));
        assertEquals(false, metaphone.isMetaphoneEqual("GHi", "H"));
        assertEquals(false, metaphone.isMetaphoneEqual("TH", "T"));
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());

        Metaphone metaphone2 = new Metaphone();
        metaphone2.setMaxCodeLen(5);
        assertEquals(5, metaphone2.getMaxCodeLen());
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.encode("KN"));
        assertEquals("AE", metaphone.encode("AE"));
        assertEquals("WR", metaphone.encode("WR"));
        assertEquals("WH", metaphone.encode("WH"));
        assertEquals("X", metaphone.encode("CIA"));
        assertEquals("", metaphone.encode("SCI"));
        assertEquals("", metaphone.encode("SCY"));
        assertEquals("", metaphone.encode("SCE"));
        assertEquals("K", metaphone.encode("CH"));
        assertEquals("X", metaphone.encode("CHe"));
        assertEquals("J", metaphone.encode("DGE"));
        assertEquals("J", metaphone.encode("DGI"));
        assertEquals("J", metaphone.encode("DGY"));
        assertEquals("", metaphone.encode("GN"));
        assertEquals("", metaphone.encode("GNED"));
        assertEquals("K", metaphone.encode("GH"));
        assertEquals("X", metaphone.encode("GHi"));
        assertEquals("0", metaphone.encode("TH"));

        assertThrows(EncoderException.class, () -> metaphone.encode(null));
        assertThrows(EncoderException.class, () -> metaphone.encode(""));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.encode((Object) "KN"));
        assertEquals("AE", metaphone.encode((Object) "AE"));
        assertEquals("WR", metaphone.encode((Object) "WR"));
        assertEquals("WH", metaphone.encode((Object) "WH"));
        assertEquals("X", metaphone.encode((Object) "CIA"));
        assertEquals("", metaphone.encode((Object) "SCI"));
        assertEquals("", metaphone.encode((Object) "SCY"));
        assertEquals("", metaphone.encode((Object) "SCE"));
        assertEquals("K", metaphone.encode((Object) "CH"));
        assertEquals("X", metaphone.encode((Object) "CHe"));
        assertEquals("J", metaphone.encode((Object) "DGE"));
        assertEquals("J", metaphone.encode((Object) "DGI"));
        assertEquals("J", metaphone.encode((Object) "DGY"));
        assertEquals("", metaphone.encode((Object) "GN"));
        assertEquals("", metaphone.encode((Object) "GNED"));
        assertEquals("K", metaphone.encode((Object) "GH"));
        assertEquals("X", metaphone.encode((Object) "GHi"));
        assertEquals("0", metaphone.encode((Object) "TH"));

        assertThrows(EncoderException.class, () -> metaphone.encode(null));
        assertThrows(EncoderException.class, () -> metaphone.encode(""));
    }
}