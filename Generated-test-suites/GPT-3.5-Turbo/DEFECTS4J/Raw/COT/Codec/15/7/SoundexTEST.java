import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.soundex("Smith"));
        assertEquals("T522", soundex.soundex("Taylor"));
        assertEquals("H241", soundex.soundex("Harris"));
        assertEquals("W500", soundex.soundex("Williams"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("T522", soundex.encode("Taylor"));
        assertEquals("H241", soundex.encode("Harris"));
        assertEquals("W500", soundex.encode("Williams"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidType() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Smith", "Smyth"));
        assertEquals(4, soundex.difference("Smith", "Smith"));
        assertEquals(2, soundex.difference("Harris", "Harrison"));
        assertEquals(3, soundex.difference("Taylor", "Trailer"));
    }

    @Test
    public void testSoundexWithCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals("1555", soundex.soundex("Smith"));
        assertEquals("2445", soundex.soundex("Taylor"));
        assertEquals("7882", soundex.soundex("Harris"));
        assertEquals("8888", soundex.soundex("Williams"));
    }

    @Test
    public void testEncodeWithCustomMapping() throws EncoderException {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals("1555", soundex.encode("Smith"));
        assertEquals("2445", soundex.encode("Taylor"));
        assertEquals("7882", soundex.encode("Harris"));
        assertEquals("8888", soundex.encode("Williams"));
    }

    @Test
    public void testSoundexWithMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(3);
        assertEquals("S53", soundex.soundex("Smith"));
        assertEquals("T52", soundex.soundex("Taylor"));
        assertEquals("H24", soundex.soundex("Harris"));
        assertEquals("W50", soundex.soundex("Williams"));
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(3);
        assertEquals(3, soundex.getMaxLength());
    }

}