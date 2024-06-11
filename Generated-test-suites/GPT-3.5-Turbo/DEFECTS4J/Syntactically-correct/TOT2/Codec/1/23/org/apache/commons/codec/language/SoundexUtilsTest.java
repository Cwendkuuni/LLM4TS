import static org.junit.Assert.*;

import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Hello123World";
        String expectedOutput = "HELLOWORLD";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_NullInput() {
        String input = null;
        String expectedOutput = null;
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new YourEncoderImplementation();
        String s1 = "Hello";
        String s2 = "Hell";
        int expectedDifference = 4;
        int actualDifference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedDifference, actualDifference);
    }

    @Test
    public void testDifference_NullEncoder() {
        StringEncoder encoder = null;
        String s1 = "Hello";
        String s2 = "Hell";
        assertThrows(EncoderException.class, () -> {
            SoundexUtils.difference(encoder, s1, s2);
        });
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "ABC";
        String es2 = "ABD";
        int expectedDifference = 2;
        int actualDifference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDifference, actualDifference);
    }

    @Test
    public void testDifferenceEncoded_NullStrings() {
        String es1 = null;
        String es2 = "ABD";
        int expectedDifference = 0;
        int actualDifference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedDifference, actualDifference);
    }
}