
package org.apache.commons.codec.language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("S532", soundex.soundex("Soundex"));
        assertEquals("A536", soundex.soundex("Apache"));
        assertEquals("C532", soundex.soundex("Codec"));
        assertEquals("L252", soundex.soundex("Language"));
        assertEquals("T200", soundex.soundex("Test"));
        assertEquals("E252", soundex.soundex("Encoder"));
        assertEquals("S532", soundex.soundex("String"));
        assertEquals("U200", soundex.soundex("Util"));
        assertEquals("S532", soundex.soundex("SoundexUtils"));
        assertEquals("T553", soundex.soundex("TestUtils"));
    }

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S532", soundex.encode("Soundex"));
        assertEquals("A536", soundex.encode("Apache"));
        assertEquals("C532", soundex.encode("Codec"));
        assertEquals("L252", soundex.encode("Language"));
        assertEquals("T200", soundex.encode("Test"));
        assertEquals("E252", soundex.encode("Encoder"));
        assertEquals("S532", soundex.encode("String"));
        assertEquals("U200", soundex.encode("Util"));
        assertEquals("S532", soundex.encode("SoundexUtils"));
        assertEquals("T553", soundex.encode("TestUtils"));
    }

}
