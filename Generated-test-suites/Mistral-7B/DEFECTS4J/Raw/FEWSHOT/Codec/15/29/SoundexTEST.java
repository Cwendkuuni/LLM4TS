package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.Soundex.SoundexUtils;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        assertEquals(1, soundex.difference("apple", "appetizer"));
        assertEquals(3, soundex.difference("racecar", "rainbow"));
        assertEquals(4, soundex.difference("kangaroo", "kangaroo"));
        assertThrows(EncoderException.class, () -> soundex.difference(null, "foo", "bar"));
        assertThrows(EncoderException.class, () -> soundex.difference("foo", null, "bar"));
        assertThrows(EncoderException.class, () -> soundex.difference("foo", "bar", null));
    }

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("A100", soundex.encode("Apple"));
        assertEquals("A261", soundex.encode("Apricot"));
        assertEquals("A262", soundex.encode("April"));
        assertEquals("K3213", soundex.encode("Kangaroo"));
        assertEquals("A250", soundex.encode("Almond"));
        assertEquals("", soundex.encode(""));
        assertThrows(EncoderException.class, () -> soundex.encode(123));
        assertThrows(EncoderException.class, () -> soundex.encode(null));
        assertThrows(EncoderException.class, () -> soundex.encode('♫'));
    }

    @Test
    public void testEncodeMapping() {
        Soundex soundex = new Soundex("A1B2C3D4E5F6G7H8I9J0K1L1M1N1O1P1Q1R1S1T1U1V1W1X1Y1Z1");
        assertEquals("A1", soundex.map('A'));
        assertEquals("B2", soundex.map('B'));
        assertEquals("C3", soundex.map('C'));
        assertEquals("D4", soundex.map('D'));
        assertEquals("E5", soundex.map('E'));
        assertEquals("F6", soundex.map('F'));
        assertEquals("G7", soundex.map('G'));
        assertEquals("H8", soundex.map('H'));
        assertEquals("I9", soundex.map('I'));
        assertEquals("J0", soundex.map('J'));
        assertEquals("K1", soundex.map('K'));
        assertEquals("L1", soundex.map('L'));
        assertEquals("M1", soundex.map('M'));
        assertEquals("N1", soundex.map('N'));
        assertEquals("O1", soundex.map('O'));
        assertEquals("P1", soundex.map('P'));
        assertEquals("Q1", soundex.map('Q'));
        assertEquals("R1", soundex.map('R'));
        assertEquals("S1", soundex.map('S'));
        assertEquals("T1", soundex.map('T'));
        assertEquals("U1", soundex.map('U'));
        assertEquals("V1", soundex.map('V'));
        assertEquals("W1", soundex.map('W'));
        assertEquals("X1", soundex.map('X'));
        assertEquals("Y1", soundex.map('Y'));
        assertEquals("Z1", soundex.map('Z'));
        assertThrows(IllegalArgumentException.class, () -> soundex.map('!'));
    }

    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        assertArrayEquals(Soundex.US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A100", soundex.soundex("Apple"));
        assertEquals("A261", soundex.soundex("Apricot"));
        assertEquals("A262", soundex.soundex("April"));
        assertEquals("K3213", soundex.soundex("Kangaroo"));
        assertEquals("A250", soundex.soundex("Almond"));
        assertEquals("", soundex.soundex(""));
        assertNull(soundex.soundex(null));
        assertThrows(EncoderException.class, () -> soundex.soundex("♫"));
    }

}