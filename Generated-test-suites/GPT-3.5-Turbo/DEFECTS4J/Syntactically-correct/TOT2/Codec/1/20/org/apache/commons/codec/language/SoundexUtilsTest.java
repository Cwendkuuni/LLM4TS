import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.RefinedSoundex;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("TEST", SoundexUtils.clean("tE!sT"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(null, SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new Soundex();
        try {
            assertEquals(2, SoundexUtils.difference(encoder, "test", "tent"));
            assertEquals(4, SoundexUtils.difference(encoder, "test", "test"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "DCBA"));
        assertEquals(2, SoundexUtils.differenceEncoded("WXYZ", "WXZZ"));
    }
}