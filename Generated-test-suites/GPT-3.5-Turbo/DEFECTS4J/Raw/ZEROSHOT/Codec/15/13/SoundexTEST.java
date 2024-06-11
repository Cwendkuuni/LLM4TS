import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex = new Soundex();

    @Test
    public void testDifference() throws Exception {
        // Test when both strings are empty
        int difference = soundex.difference("", "");
        assertEquals(4, difference);

        // Test when one string is empty
        difference = soundex.difference("hello", "");
        assertEquals(0, difference);

        // Test when both strings are equal
        difference = soundex.difference("hello", "hello");
        assertEquals(4, difference);

        // Test when only one character is different
        difference = soundex.difference("hello", "hella");
        assertEquals(3, difference);

        // Test when multiple characters are different
        difference = soundex.difference("hello", "hallo");
        assertEquals(2, difference);

        // Test when all characters are different
        difference = soundex.difference("hello", "bye");
        assertEquals(0, difference);
    }

    @Test
    public void testEncode() throws Exception {
        // Test encoding null
        String encoded = soundex.encode(null);
        assertNull(encoded);

        // Test encoding empty string
        encoded = soundex.encode("");
        assertEquals("0000", encoded);

        // Test encoding lower case string
        encoded = soundex.encode("hello");
        assertEquals("H400", encoded);

        // Test encoding upper case string
        encoded = soundex.encode("HELLO");
        assertEquals("H400", encoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeInvalidCharacter() throws Exception {
        soundex.encode("abc$");
    }

    @Test
    public void testSoundex() throws Exception {
        // Test soundex with null string
        String encoded = soundex.soundex(null);
        assertNull(encoded);

        // Test soundex with empty string
        encoded = soundex.soundex("");
        assertEquals("0000", encoded);

        // Test soundex with lower case string
        encoded = soundex.soundex("hello");
        assertEquals("H400", encoded);

        // Test soundex with upper case string
        encoded = soundex.soundex("HELLO");
        assertEquals("H400", encoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexInvalidCharacter() throws Exception {
        soundex.soundex("abc$");
    }
}