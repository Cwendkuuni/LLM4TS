import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("John", "Jon");
        assertEquals(4, difference);
    }

    @Test
    public void testEncodeObject() throws Exception {
        Soundex soundex = new Soundex();
        Object result = soundex.encode("Hello");
        assertEquals("H400", result);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("World");
        assertEquals("W643", result);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Hello World");
        assertEquals("H640", result);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }
}