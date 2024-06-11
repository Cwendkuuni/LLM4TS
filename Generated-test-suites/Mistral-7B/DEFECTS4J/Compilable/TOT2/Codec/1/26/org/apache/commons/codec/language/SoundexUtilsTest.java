package org.apache.commons.codec.language;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "AbCdEfGhIjKlMnOpQrStUvWxYz";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }
}