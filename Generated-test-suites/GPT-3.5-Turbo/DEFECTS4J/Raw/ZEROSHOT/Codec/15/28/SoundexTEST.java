import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("hello");
        assert encoded.equals("H400");
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("hello", "holla");
        assert difference == 2;
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("hello");
        assert soundexCode.equals("H400");
    }
}