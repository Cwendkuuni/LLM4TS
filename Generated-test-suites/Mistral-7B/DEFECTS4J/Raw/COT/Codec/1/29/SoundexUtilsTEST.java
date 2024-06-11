import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = null;
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "";
        expectedOutput = "";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "hello";
        expectedOutput = "Hello";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "HELLO";
        expectedOutput = "HELLO";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "123hello";
        expectedOutput = "Hello";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "123Hello";
        expectedOutput = "Hello";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "123hello World!";
        expectedOutput = "Hello World!";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "123Hello World!";
        expectedOutput = "Hello World!";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithNullInputs() {
        String encoderType = "Soundex";
        String s1 = null;
        String s2 = null;
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoderType, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithEmptyInputs() {
        String encoderType = "Soundex";
        String s1 = "";
        String s2 = "";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(encoderType, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSameInput() {
        String encoderType = "Soundex";
        String s1 = "apple";
        String s2 = "apple";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(encoderType, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithInvalidEncoderType() {
        String encoderType = "InvalidEncoderType";
        String s1 = "apple";
        String s2 = "banana";
        SoundexUtils.difference(encoderType, s1, s2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithInvalidInput1() {
        String encoderType = "Soundex";
        String s1 = null;
        String s2 = "banana";
        SoundexUtils.difference(encoderType, s1, s2);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithInvalidInput2() {
        String encoderType = "Soundex";
        String s1 = "apple";
        String s2 = null;
        SoundexUtils.difference(encoderType, s1, s2);
    }

    @Test
    public void testDifferenceMethodWithDifferentInputs() {
        String encoderType = "Soundex";
        String s1 = "apple";
        String s2 = "banana";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoderType, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncodedInputs() {
        String encoderType = "Soundex";
        String s1 = "ah000";
        String s2 = "ba111";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.difference(encoderType, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithRefinedSoundexEncodedInputs() {
        String encoderType = "RefinedSoundex";
        String s1 = "apple";
        String s2 = "banana";
        int expectedOutput = 2;
        int actualOutput = SoundexUtils.difference(encoderType, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithNullInputs() {
        String es1 = null;
        String es2 = null;
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithEmptyInputs() {
        String es1 = "";
        String es2 = "";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithSameInputs() {
        String es1 = "ah000";
        String es2 = "ah000";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithDifferentInputs() {
        String es1 = "ah000";
        String es2 = "ba111";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithLongerInput1() {
        String es1 = "ah0000";
        String es2 = "ba111";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncodedMethodWithLongerInput2() {
        String es1 = "ah000";
        String es2 = "ba1112";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }
}