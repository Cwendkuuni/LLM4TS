import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Hello123World!";
        String expected = "HELLOWORLD";
        assertEquals(expected, SoundexUtils.clean(input));
        
        input = "1234";
        expected = "";
        assertEquals(expected, SoundexUtils.clean(input));
        
        input = null;
        expected = null;
        assertEquals(expected, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hola";
        int expected = 3;
        assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));

        encoder = new RefinedSoundex();
        s1 = "hello";
        s2 = "halo";
        expected = 5;
        assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "123";
        s2 = "456";
        expected = 0;
        assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "123456";
        String es2 = "123457";
        int expected = 5;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));

        es1 = "hello";
        es2 = "hola";
        expected = 3;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = null;
        es2 = "123";
        expected = 0;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }
}