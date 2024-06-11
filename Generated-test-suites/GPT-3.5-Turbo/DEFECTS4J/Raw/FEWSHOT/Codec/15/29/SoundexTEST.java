
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S532", soundex.encode("Soundex"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Soundex", "S532"));
    }

    @Test
    public void testEncodeNull() {
        Soundex soundex = new Soundex();
        assertNull(soundex.encode(null));
    }
}
