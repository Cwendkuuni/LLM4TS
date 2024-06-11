package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference(null, null));
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundex.difference("foo", "foo"));
        assertEquals(1, soundex.difference("foo", "bar"));
        assertEquals(3, soundex.difference("foobar", "barfoo"));
        assertEquals(2, soundex.difference("foobar", "baz"));
        assertEquals(4, soundex.difference("apple", "appel")); // Dutch apple
        assertEquals(4, soundex.difference("Apple", "Apple")); // Case difference
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A220", soundex.encode("Apple"));
        assertEquals("B345", soundex.encode("Banana"));
        assertEquals("A100", soundex.encode("App"));
        assertEquals("A123", soundex.encode("AlFA"));
        assertEquals("A530", soundex.encode("Alphonse"));
        assertEquals("A550", soundex.encode("Alphonso"));
        assertEquals("A162", soundex.encode("Alfred"));
        assertEquals("A151", soundex.encode("Alonzo"));
        assertEquals("A130", soundex.encode("Alonzo")); // Different casing
        assertEquals("A130", soundex.encode("Alonzo ")); // Trailing space
        assertEquals("A262", soundex.encode("Anthony"));
        assertEquals("A134", soundex.encode("Andrew"));
        assertEquals("A162", soundex.encode("Albert"));
        assertEquals("A220", soundex.encode("Alvin"));
        assertEquals("A231", soundx.encode("Alonso"));
        assertEquals("A130", soundex.encode("Alonzo")); // Different casing
        assertEquals("A130", soundex.encode("Alonzo ")); // Trailing space
        assertEquals("A200", soundex.encode("Aaron"));
        assertEquals("A123", soundex.encode("Alfa"));
        assertEquals("A000", soundex.encode(""));
        assertEquals("A000", soundex.encode(null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeError() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode("!");
    }

    @Test
    public void testGetMappingCode() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertEquals('0', mapping[0]);
        assertEquals('1', mapping[1]);
        assertEquals('1', mapping[2]);
        assertEquals('0', mapping[3]);
        assertEquals('1', mapping[4]);
        assertEquals('2', mapping[5]);
        assertEquals('0', mapping[6]);
        assertEquals('2', mapping[7]);
        assertEquals('3', mapping[8]);
        assertEquals('0', mapping[9]);
        assertEquals('5', mapping[10]);
        assertEquals('0', mapping[11]);
        assertEquals('3', mapping[12]);
        assertEquals('0', mapping[13]);
        assertEquals('0', mapping[14]);
        assertEquals('0', mapping[15]);
        assertEquals('5', mapping[16]);
        assertEquals('0', mapping[17]);
        assertEquals('0', mapping[18]);
        assertEquals('0', mapping[19]);
        assertEquals('0', mapping[20]);
        assertEquals('0', mapping[21]);
        assertEquals('0', mapping[22]);
        assertEquals('0', mapping[23]);
        assertEquals('0', mapping[24]);
        assertEquals('0', mapping[25]);
    }

    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        assertEquals('0', soundex.map(' '));
        assertEquals('0', soundex.map('\u0000'));
        assertEquals('0', soundex.map('\n'));
        assertEquals('1', soundex.map('A'));
        assertEquals('1', soundex.map('B'));
        assertEquals('1', soundex.map('C'));
        assertEquals('0', soundex.map('D'));
        assertEquals('2', soundex.map('E'));
        assertEquals('0', soundex.map('F'));
        assertEquals('3', soundex.map('G'));
        assertEquals('0', soundex.map('H'));
        assertEquals('3', soundex.map('H')); // Hw sound
        assertEquals('0', soundex.map('I'));
        assertEquals('1', soundex.map('J'));
        assertEquals('1', soundex.map('K'));
        assertEquals('1', soundex.map('L'));
        assertEquals('1', soundex.map('M'));
        assertEquals('2', soundex.map('N'));
        assertEquals('2', soundex.map('O'));
        assertEquals('2', soundex.map('P'));
        assertEquals('2', soundex.map('Q'));
        assertEquals('2', soundex.map('R'));
        assertEquals('2', soundex.map('S'));
        assertEquals('2', soundex.map('T'));
        assertEquals('2', soundex.map('U'));
        assertEquals('2', soundex.map('V'));
        assertEquals('2', soundex.map('W'));
        assertEquals('2', soundex.map('X'));
        assertEquals('2', soundex.map('Y'));
        assertEquals('2', soundex.map('Z'));
        assertEquals('5', soundex.map('Ā')); // Macron accent
    }

}