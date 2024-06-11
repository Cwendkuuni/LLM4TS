
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String str = "Hello, World!";
        String expected = "HELLOWORLD";
        String actual = SoundexUtils.clean(str);
        assertEquals("Clean method should return the expected result", expected, actual);
    }

    @Test
    public void testCleanNull() {
        String str = null;
        String expected = null;
        String actual = SoundexUtils.clean(str);
        assertEquals("Clean method should return null if input is null", expected, actual);
    }

    @Test
    public void testCleanEmpty() {
        String str = "";
        String expected = "";
        String actual = SoundexUtils.clean(str);
        assertEquals("Clean method should return an empty string if input is empty", expected, actual);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Halo";
        int expected = 2;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals("Difference method should return the expected number of matching characters", expected, actual);
    }

    @Test
    public void testDifferenceNull() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = null;
        String s2 = "Halo";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals("Difference method should return 0 if one of the input strings is null", expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceEncoderException() throws EncoderException {
        StringEncoder encoder = new DummyEncoder(); // Replace DummyEncoder with a real encoder
        String s1 = "Hello";
        String s2 = "Halo";
        SoundexUtils.difference(encoder, s1, s2);
    }

    private class DummyEncoder implements StringEncoder {
        @Override
        public String encode(String source) throws EncoderException {
            throw new EncoderException("Dummy encoder always throws an exception");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "4321";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals("DifferenceEncoded method should return the expected number of matching characters", expected, actual);
    }

    @Test
    public void testDifferenceEncodedNull() {
        String es1 = null;
        String es2 = "4321";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals("DifferenceEncoded method should return 0 if one of the input strings is null", expected, actual);
    }

    @Test
    public void testDifferenceEncodedEmpty() {
        String es1 = "";
        String es2 = "4321";
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals("DifferenceEncoded method should return 0 if one of the input strings is empty", expected, actual);
    }

    @Test
    public void testDifferenceEncodedSameLength() {
        String es1 = "1234";
        String es2 = "1234";
        int expected = 4;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals("DifferenceEncoded method should return the length of the strings if they are the same", expected, actual);
    }

    @Test
    public void testDifferenceEncodedShorter() {
        String es1 = "123";
        String es2 = "1234";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals("DifferenceEncoded method should return the length of the shorter string", expected, actual);
    }

    @Test
    public void testDifferenceEncodedLonger() {
        String es1 = "1234";
        String es2 = "123";
        int expected = 3;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals("DifferenceEncoded method should return the length of the shorter string", expected, actual);
    }

}
