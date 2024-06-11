import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        // Testing encoding of strings
        String encoded1 = soundex.encode("Apache");
        assert(encoded1.equals("A120"));
        
        String encoded2 = soundex.encode("Commons");
        assert(encoded2.equals("C552"));
        
        String encoded3 = soundex.encode("Codec");
        assert(encoded3.equals("C230"));
        
        String encoded4 = soundex.encode("Language");
        assert(encoded4.equals("L525"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Testing difference between two encoded strings
        int difference1 = soundex.difference("Apache", "Apacho");
        assert(difference1 == 3);
        
        int difference2 = soundex.difference("Commons", "Commos");
        assert(difference2 == 2);
        
        int difference3 = soundex.difference("Codec", "Codex");
        assert(difference3 == 3);
        
        int difference4 = soundex.difference("Language", "Langiwge");
        assert(difference4 == 2);
    }
}