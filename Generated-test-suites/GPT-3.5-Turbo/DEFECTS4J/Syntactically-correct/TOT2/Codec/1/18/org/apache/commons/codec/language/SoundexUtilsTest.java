import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "t1-e2_S3t!"; // input with special characters and numbers
        String expected = "TEST"; // expected output after cleaning
        String cleaned = SoundexUtils.clean(input);
        
        assertEquals(expected, cleaned);
    }

    @Test
    public void testCleanEmptyString() {
        String input = ""; // empty string input
        String expected = ""; // expected output empty string
        String cleaned = SoundexUtils.clean(input);
        
        assertEquals(expected, cleaned);
    }

    @Test
    public void testCleanNullString() {
        String input = null; // null input
        String cleaned = SoundexUtils.clean(input);
        
        assertEquals(null, cleaned);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "test1";
        String s2 = "test12";
        
        int diff = SoundexUtils.difference(encoder, s1, s2);
        
        assertEquals(4, diff);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncoderException() throws EncoderException {
        StringEncoder encoder = new FaultyEncoder();
        String s1 = "test1";
        String s2 = "test2";
         
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "1235";
        
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(3, diff);
    }

    @Test
    public void testDifferenceEncodedDifferentLength() {
        String es1 = "123";
        String es2 = "12345"; // different lengths
        
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(3, diff);
    }

    @Test
    public void testDifferenceEncodedNullInput() {
        String es1 = null; 
        String es2 = "1234"; 
        
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(0, diff);
    }
}