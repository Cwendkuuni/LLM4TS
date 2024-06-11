import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = SoundexUtils.clean("Hello World!");
        assertEquals("HELLOWORLD", str);

        str = SoundexUtils.clean("This is a Test");
        assertEquals("THISISATEST", str);

        str = SoundexUtils.clean("");
        assertEquals("", str);

        str = SoundexUtils.clean(null);
        assertNull(str);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        int diff = SoundexUtils.difference(encoder, "Hello", "Hello");
        assertEquals(4, diff);

        diff = SoundexUtils.difference(encoder, "Hello", "World");
        assertEquals(0, diff);

        diff = SoundexUtils.difference(encoder, "", "Hello");
        assertEquals(0, diff);

        diff = SoundexUtils.difference(encoder, null, "Hello");
        assertEquals(0, diff);
    }

    @Test
    public void testDifferenceEncoded() {
        int diff = SoundexUtils.differenceEncoded("H400", "H400");
        assertEquals(4, diff);

        diff = SoundexUtils.differenceEncoded("H400", "W600");
        assertEquals(0, diff);

        diff = SoundexUtils.differenceEncoded("", "H400");
        assertEquals(0, diff);

        diff = SoundexUtils.differenceEncoded(null, "H400");
        assertEquals(0, diff);
    }
}