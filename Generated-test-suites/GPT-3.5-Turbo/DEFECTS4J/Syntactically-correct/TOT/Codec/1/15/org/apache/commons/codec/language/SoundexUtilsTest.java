import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void clean_shouldReturnNull_whenInputIsNull() {
        String result = SoundexUtils.clean(null);
        Assert.assertNull(result);
    }

    @Test
    public void clean_shouldReturnEmptyString_whenInputIsEmpty() {
        String result = SoundexUtils.clean("");
        Assert.assertEquals("", result);
    }

    @Test
    public void clean_shouldReturnCleanString_whenInputHasSpecialCharacters() {
        String result = SoundexUtils.clean("123!@#$%^&*()");
        Assert.assertEquals("", result);
    }

    @Test
    public void clean_shouldReturnUpperCaseString_whenInputHasLowerCaseLetters() {
        String result = SoundexUtils.clean("abc");
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void clean_shouldReturnUpperCaseString_whenInputHasUpperCaseLetters() {
        String result = SoundexUtils.clean("XYZ");
        Assert.assertEquals("XYZ", result);
    }

    @Test
    public void difference_shouldReturnZero_whenInputIsNull() throws EncoderException {
        int result = SoundexUtils.difference(null, null, null);
        Assert.assertEquals(0, result);
    }

    @Test
    public void difference_shouldReturnZero_whenOneInputIsNull() throws EncoderException {
        StringEncoder encoder = new TestEncoder();
        int result = SoundexUtils.difference(encoder, null, "test");
        Assert.assertEquals(0, result);
    }

    @Test
    public void difference_shouldReturnZero_whenBothInputsAreNull() throws EncoderException {
        StringEncoder encoder = new TestEncoder();
        int result = SoundexUtils.difference(encoder, null, null);
        Assert.assertEquals(0, result);
    }

    @Test
    public void difference_shouldReturnCorrectDifference_whenBothInputsAreNotEmpty() throws EncoderException {
        StringEncoder encoder = new TestEncoder();
        int result = SoundexUtils.difference(encoder, "abcd", "abef");
        Assert.assertEquals(2, result);
    }

    @Test(expected = EncoderException.class)
    public void difference_shouldThrowEncoderException_whenEncoderThrowsException() throws EncoderException {
        StringEncoder encoder = new TestEncoder();
        SoundexUtils.difference(encoder, "test", "test");
    }

    @Test
    public void differenceEncoded_shouldReturnZero_whenInputIsNull() {
        int result = SoundexUtils.differenceEncoded(null, null);
        Assert.assertEquals(0, result);
    }

    @Test
    public void differenceEncoded_shouldReturnZero_whenOneInputIsNull() {
        int result = SoundexUtils.differenceEncoded(null, "test");
        Assert.assertEquals(0, result);
    }

    @Test
    public void differenceEncoded_shouldReturnZero_whenBothInputsAreNull() {
        int result = SoundexUtils.differenceEncoded(null, null);
        Assert.assertEquals(0, result);
    }

    @Test
    public void differenceEncoded_shouldReturnCorrectDifference_whenBothInputsAreNotEmpty() {
        int result = SoundexUtils.differenceEncoded("abcd", "abef");
        Assert.assertEquals(2, result);
    }

    @Test
    public void differenceEncoded_shouldReturnCorrectDifference_whenInputsAreDifferentLength() {
        int result = SoundexUtils.differenceEncoded("abcd", "ab");
        Assert.assertEquals(2, result);
    }
  
    private class TestEncoder implements StringEncoder {
        @Override
        public String encode(String str) throws EncoderException {
            if (str.equals("test")) {
                throw new EncoderException();
            }
            return str;
        }
    }
}