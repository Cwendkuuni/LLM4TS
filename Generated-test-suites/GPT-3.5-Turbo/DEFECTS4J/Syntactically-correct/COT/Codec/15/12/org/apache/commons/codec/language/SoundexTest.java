
import org.junit.Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        StringEncoder encoder = soundex;
        
        String encoded = (String) encoder.encode("Hello");
        assert(encoded.equals("H400"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeWrongType() throws EncoderException {
        Soundex soundex = new Soundex();
        StringEncoder encoder = soundex;
        
        encoder.encode(123);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        int difference = soundex.difference("Hello", "Hello");
        assert(difference == 4);
    }
    
    @Test(expected = EncoderException.class)
    public void testDifferenceWrongParams() throws EncoderException {
        Soundex soundex = new Soundex();
        
        soundex.difference(123, 456);
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        String result = soundex.soundex("Hello");
        assert(result.equals("H400"));
        
        result = soundex.soundex("World");
        assert(result.equals("W643"));
    }
}
