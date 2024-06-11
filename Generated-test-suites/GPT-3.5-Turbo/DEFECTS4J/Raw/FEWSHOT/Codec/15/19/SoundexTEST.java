
package org.apache.commons.codec.language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("A261", soundex.encode("Ashcroft"));
        assertEquals("B620", soundex.encode("Bush"));
        assertEquals("C412", soundex.encode("Clinton"));
        assertEquals("D625", soundex.encode("Dole"));
        assertEquals("E650", soundex.encode("Edwards"));
    }

    @Test
    public void testDifference() throws Exception {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Ashcroft", "A261"));
        assertEquals(2, soundex.difference("Bush", "B620"));
        assertEquals(4, soundex.difference("Clinton", "C412"));
        assertEquals(2, soundex.difference("Dole", "D625"));
        assertEquals(2, soundex.difference("Edwards", "E650"));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A261", soundex.soundex("Ashcroft"));
        assertEquals("B620", soundex.soundex("Bush"));
        assertEquals("C412", soundex.soundex("Clinton"));
        assertEquals("D625", soundex.soundex("Dole"));
        assertEquals("E650", soundex.soundex("Edwards"));
    }

    @Test
    public void testEncodeWithCustomMapping() {
        char[] mapping = "01230120022455012623010202".toCharArray();
        Soundex soundex = new Soundex(mapping);
        assertEquals("A926", soundex.encode("Ashcroft"));
        assertEquals("B300", soundex.encode("Bush"));
        assertEquals("C543", soundex.encode("Clinton"));
        assertEquals("D400", soundex.encode("Dole"));
        assertEquals("E623", soundex.encode("Edwards"));
    }

    @Test
    public void testSoundexWithCustomMapping() {
        char[] mapping = "01230120022455012623010202".toCharArray();
        Soundex soundex = new Soundex(mapping);
        assertEquals("A926", soundex.soundex("Ashcroft"));
        assertEquals("B300", soundex.soundex("Bush"));
        assertEquals("C543", soundex.soundex("Clinton"));
        assertEquals("D400", soundex.soundex("Dole"));
        assertEquals("E623", soundex.soundex("Edwards"));
    }
}
