import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexConstructorWithDefaultMapping() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructorWithCustomMapping() {
        char[] customMapping = {'0','1','2','3','4','5','6','7','8','9'};
        Soundex soundex = new Soundex(customMapping);
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructorWithMappingString() {
        String mappingString = "01230120022455012623010202";
        Soundex soundex = new Soundex(mappingString);
        assertNotNull(soundex);
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        String encodedString = soundex.encode("Test");
        assertEquals("T230", encodedString);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String encodedString = soundex.encode("Test");
        assertEquals("T230", encodedString);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Test1", "Test2");
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexMappingCode() {
        Soundex soundex = new Soundex();
        char mappingCode = soundex.getMappingCode("TEST", 2);
        assertEquals('3', mappingCode);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

    @Test
    public void testSoundexSoundex() {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("TestString");
        assertEquals("T232", soundexCode);
    }

}