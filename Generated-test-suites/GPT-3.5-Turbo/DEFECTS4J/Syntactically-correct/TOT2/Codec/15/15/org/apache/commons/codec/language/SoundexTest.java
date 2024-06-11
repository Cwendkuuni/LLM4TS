import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            int difference = soundex.difference("hello", "hallo");
            assertEquals(3, difference);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        try {
            Object encoded = soundex.encode("world");
            assertNotNull(encoded);
            assertTrue(encoded instanceof String);
            assertEquals("W643", encoded);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("test");
        assertEquals("T230", encoded);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String code = soundex.soundex("hello");
        assertEquals("H400", code);
    }

    @Test
    public void testCustomMapping() {
        char[] customMapping = "12345678901234567890123456".toCharArray();
        Soundex soundex = new Soundex(customMapping);
        String code = soundex.encode("custom");
        assertEquals("C567", code);
    }
}