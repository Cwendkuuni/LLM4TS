import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            int difference = soundex.difference("hello", "halo");
            assertEquals(2, difference);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        try {
            Object encoded = soundex.encode("hello");
            assertEquals("H400", encoded);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("hello");
        assertEquals("H400", encoded);
    }

    @Test
    public void testSoundexWithNullString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode(null);
        assertEquals(null, encoded);
    }

    @Test
    public void testSoundexWithEmptyString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("");
        assertEquals("", encoded);
    }
}