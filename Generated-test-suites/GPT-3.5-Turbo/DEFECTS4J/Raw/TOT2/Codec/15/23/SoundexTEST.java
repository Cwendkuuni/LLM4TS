import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            int difference = soundex.difference("hello", "halo");
            assertEquals(3, difference);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        try {
            Object encoded = soundex.encode("test");
            assertEquals("T230", encoded);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("world");
        assertEquals("W643", encoded);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Soundex");
        assertEquals("S532", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCodeException() {
        Soundex soundex = new Soundex();
        soundex.getMappingCode("test", 10); // testing out of bound Index
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapException() {
        Soundex soundex = new Soundex();
        soundex.map('!');
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

}