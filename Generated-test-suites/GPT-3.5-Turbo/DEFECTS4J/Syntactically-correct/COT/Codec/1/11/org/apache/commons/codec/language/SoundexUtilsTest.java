
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "aBcD1234";
        String expectedOutput = "ABCD";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNull() {
        SoundexUtils.clean(null);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoderMock encoder = new StringEncoderMock();
        String s1 = "abc";
        String s2 = "def";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "5678";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    private static class StringEncoderMock implements org.apache.commons.codec.StringEncoder {

        @Override
        public String encode(String source) throws EncoderException {
            return source;
        }
    }
}

