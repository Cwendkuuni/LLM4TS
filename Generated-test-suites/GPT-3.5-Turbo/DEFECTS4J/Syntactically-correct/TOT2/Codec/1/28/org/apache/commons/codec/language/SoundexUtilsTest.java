import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Abc123Def$%";
        String expected = "ABCDEF";
        assertEquals(expected, SoundexUtils.clean(input));
        
        input = "";
        expected = "";
        assertEquals(expected, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new CustomEncoder(); // Assuming a custom encoder implementation
        String s1 = "Test1";
        String s2 = "Test2";
        int expected = 4;
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            assertEquals(expected, result);
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "ABCD";
        String es2 = "ABCD";
        int expected = 4;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = "";
        es2 = "ABCD";
        expected = 0;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }

    private static class CustomEncoder implements StringEncoder {
        @Override
        public String encode(String str) throws EncoderException {
            return str.toUpperCase();
        }
    }
}