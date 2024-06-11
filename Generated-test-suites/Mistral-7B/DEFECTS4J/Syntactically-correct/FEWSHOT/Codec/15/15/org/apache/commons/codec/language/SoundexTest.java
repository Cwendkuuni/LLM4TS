package org.apache.commons.codec.language;

import static org.junit.Assert.*;

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
        assertEquals(0, soundex.difference("Bar", "BAR"));
        assertEquals(1, soundex.difference("apple", "appel"));
        assertEquals(1, soundex.difference("Banana", "bananA"));
        assertEquals(1, soundex.difference("Caramel", "caramel"));
        assertEquals(2, soundex.difference("father", "fathar"));
        assertEquals(2, soundex.difference("Gospell", "gospel"));
        assertEquals(3, soundex.difference("hEllo", "hello"));
        assertEquals(3, soundex.difference("index", "indent"));
        assertEquals(3, soundex.difference("listener", "listen"));
        assertEquals(3, soundex.difference("station", "statioN"));
        assertEquals(4, soundex.difference("abraham", "abram"));
        assertEquals(4, soundex.difference("Baker", "baker"));
        assertEquals(4, soundex.difference("father", "fadder"));
        assertEquals(4, soundex.difference("Garage", "garage"));
        assertEquals(4, soundex.difference("Grapes", "grapes"));
        assertEquals(4, soundex.difference("Howard", "hodges"));
        assertEquals(4, soundex.difference("James", "jimmy"));
        assertEquals(4, soundex.difference("lewis", "lusky"));
        assertEquals(4, soundex.difference("Moses", "moses"));
        assertEquals(4, soundex.difference("Napoleon", "napoloeon"));
        assertEquals(4, soundex.difference("Quincy", "quincy"));
        assertEquals(4, soundex.difference("Robert", "robert"));
        assertEquals(4, soundex.difference("Sally", "sally"));
        assertEquals(4, soundex.difference("Tina", "tina"));
        assertEquals(4, soundex.difference("Victor", "victor"));
        assertEquals(4, soundex.difference("Walter", "walter"));
        assertEquals(4, soundex.difference("William", "william"));
        assertEquals(4, soundex.difference("Winston", "winston"));
        assertEquals(4, soundex.difference("York", "york"));
        assertEquals(4, soundex.difference("Zachary", "zachary"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.encode("Apple"));
        assertEquals("B132", soundex.encode("Banana"));
        assertEquals("F120", soundex.encode("Father"));
        assertEquals("G200", soundex.encode("Gospel"));
        assertEquals("H201", soundex.encode("Hello"));
        assertEquals("I301", soundex.encode("Index"));
        assertEquals("L121", soundex.encode("Listener"));
        assertEquals("S125", soundex.encode("Station"));
        assertEquals("A101", soundex.encode("Abraham"));
        assertEquals("B001", soundex.encode("Baker"));
        assertEquals("F110", soundex.encode("Fadder"));
        assertEquals("G100", soundex.encode("Garage"));
        assertEquals("G201", soundex.encode("Grapes"));
        assertEquals("H202", soundex.encode("Hodges"));
        assertEquals("J111", soundex.encode("James"));
        assertEquals("L111", soundex.encode("Lewis"));
        assertEquals("M000", soundex.encode("Moses"));
        assertEquals("N0001", soundex.encode("Napoleon"));
        assertEquals("Q111", soundex.encode("Quincy"));
        assertEquals("R000", soundex.encode("Robert"));
        assertEquals("S111", soundex.encode("Sally"));
        assertEquals("T001", soundex.encode("Tina"));
        assertEquals("V001", soundex.encode("Victor"));
        assertEquals("W001", soundex.encode("Walter"));
        assertEquals("W111", soundex.encode("William"));
        assertEquals("W112", soundex.encode("Winston"));
        assertEquals("Y011", soundex.encode("York"));
        assertEquals("Z011", soundex.encode("Zachary"));
    }

    @Test
    public void testEncodeWithCustomMapping() throws EncoderException {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING.substring(1)); // Remove the leading zero
        assertEquals("A123", soundex.encode("Apple"));
        assertEquals("B132", soundex.encode("Banana"));
        assertEquals("F120", soundex.encode("Father"));
        assertEquals("G200", soundex.encode("Gospel"));
        assertEquals("H201", soundex.encode("Hello"));
        assertEquals("I301", soundex.encode("Index"));
        assertEquals("L121", soundex.encode("Listener"));
        assertEquals("S125", soundex.encode("Station"));
        assertEquals("A101", soundex.encode("Abraham"));
        assertEquals("B001", soundex.encode("Baker"));
        assertEquals("F110", soundex.encode("Fadder"));
        assertEquals("G100", soundex.encode("Garage"));
        assertEquals("G201", soundex.encode("Grapes"));
        assertEquals("H202", soundex.encode("Hodges"));
        assertEquals("J111", soundex.encode("James"));
        assertEquals("L111", soundex.encode("Lewis"));
        assertEquals("M000", soundex.encode("Moses"));
        assertEquals("N0001", soundex.encode("Napoleon"));
        assertEquals("Q111", soundex.encode("Quincy"));
        assertEquals("R000", soundex.encode("Robert"));
        assertEquals("S111", soundex.encode("Sally"));
        assertEquals("T001", soundex.encode("Tina"));
        assertEquals("V001", soundex.encode("Victor"));
        assertEquals("W001", soundex.encode("Walter"));
        assertEquals("W111", soundex.encode("William"));
        assertEquals("W112", soundex.encode("Winston"));
        assertEquals("Y011", soundex.encode("York"));
        assertEquals("Z011", soundex.encode("Zachary"));
    }

}