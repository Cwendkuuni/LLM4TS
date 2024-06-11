
import static org.junit.Assert.*;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("CLEAN", SoundexUtils.clean("cl#$ean"));
        assertEquals("UPPERCASELETTERS", SoundexUtils.clean("UpPeRcAsElEtTeRs"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(null, "test", "test"));
        assertEquals(0, SoundexUtils.difference(null, "test", "rest"));
        assertEquals(1, SoundexUtils.difference(null, "test", "teat"));
        assertEquals(0, SoundexUtils.difference(null, "test", null));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("T200", "T200"));
        assertEquals(0, SoundexUtils.differenceEncoded("T200", "R100"));
        assertEquals(1, SoundexUtils.differenceEncoded("T200", "T210"));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "T200"));
        assertEquals(0, SoundexUtils.differenceEncoded("T200", null));
    }

}
