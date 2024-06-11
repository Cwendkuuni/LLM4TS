import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        String s1 = "Hello";
        String s2 = "Halo";
        assertEquals(3, soundex.difference(s1, s2));
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        String input = "Test";
        assertEquals("T230", soundex.encode(input));
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String input = "Test";
        assertEquals("T230", soundex.encode(input));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("T230", soundex.soundex("Test"));
    }

    @Test
    public void testSoundexWithEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.soundex(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCodeIllegalArgumentException() {
        Soundex soundex = new Soundex();
        String str = "ab";
        soundex.getMappingCode(str, 1);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testUS_ENGLISHMapping() {
        char[] expectedMapping = {'0', '1', '2', '3', '0', '1', '2', '0', '0', '2', '2', '4', '5', '5', '0', '1', '2', '6', '2', '3', '0', '2', '1', '0', '2', '0', '2'};
        assertArrayEquals(expectedMapping, Soundex.US_ENGLISH_MAPPING);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }
}