import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            int difference = soundex.difference("hello", "halo");
            assertEquals(2, difference);
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        try {
            Object result = soundex.encode("test");
            assertTrue(result instanceof String);
            assertEquals("T230", result);
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("testing");
        assertEquals("T235", result);
    }

    @Test
    public void testMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
        soundex.setMaxLength(6);
        assertEquals(6, soundex.getMaxLength());
    }

    @Test
    public void testSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
        assertEquals("01230120022455012623010202", new String(mapping));
    }

    @Test
    public void testSoundexConstructor() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
        assertEquals(new String(customMapping), new String(mapping));
    }

    @Test
    public void testSoundexConstructorWithMappingString() {
        String customMapping = "ABCDEFGH";
        Soundex soundex = new Soundex(customMapping);
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
        assertEquals(customMapping, new String(mapping));
    }

    @Test
    public void testGetMappingCode() {
        Soundex soundex = new Soundex();
        char code = soundex.getMappingCode("TEST", 0);
        assertEquals('T', code);
    }

    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        char code = soundex.map('A');
        assertEquals('0', code);
    }

    @Test
    public void testSoundexEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Testing");
        assertEquals("T235", result);
    }
}