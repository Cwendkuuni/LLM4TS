import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("TEST", SoundexUtils.clean("t1e2s3t4"));
    }

    @Test
    public void testDifference() {
        try {
            assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "holla"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(3, SoundexUtils.differenceEncoded("1234", "1244"));
    }

    @Test
    public void testCleanEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testDifferenceWithNullEncoder() {
        try {
            assertEquals(1, SoundexUtils.difference(null, "abc", "def"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testDifferenceWithNullStrings() {
        try {
            assertEquals(0, SoundexUtils.difference(new Soundex(), null, "abc"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncodedWithNullStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "abc"));
    }

}