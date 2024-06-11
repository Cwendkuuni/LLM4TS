import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test the clean method with empty string.
     */
    @Test
    public void testCleanEmptyString() {
        String result = SoundexUtils.clean("");
        Assert.assertEquals("", result);
    }

    /**
     * Test the clean method with null string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullString() {
        SoundexUtils.clean(null);
    }

    /**
     * Test the clean method with single letter string.
     */
    @Test
    public void testCleanSingleLetterString() {
        String result = SoundexUtils.clean("A");
        Assert.assertEquals("A", result);
    }

    /**
     * Test the clean method with multiple letters string.
     */
    @Test
    public void testCleanMultipleLettersString() {
        String result = SoundexUtils.clean("Apple");
        Assert.assertEquals("APPL", result);
    }

    /**
     * Test the difference method with empty strings.
     */
    @Test
    public void testDifferenceEmptyStrings() {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        int result = SoundexUtils.difference(encoder, "", "");
        Assert.assertEquals(0, result);
    }

    /**
     * Test the difference method with null strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceNullStrings() {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        SoundexUtils.difference(encoder, null, null);
    }

    /**
     * Test the difference method with identical strings.
     */
    @Test
    public void testDifferenceIdenticalStrings() {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Apple";
        String es1 = encoder.encode(s1);
        String es2 = encoder.encode(s2);
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(4, result);
    }

    /**
     * Test the difference method with different strings.
     */
    @Test
    public void testDifferenceDifferentStrings() {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Banana";
        String es1 = encoder.encode(s1);
        String es2 = encoder.encode(s2);
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, result);
    }

    /**
     * Test the difference method with Soundex encoded strings.
     */
    @Test
    public void testDifferenceEncodedStrings() {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Apple";
        String es1 = encoder.encode(s1);
        String es2 = encoder.encode(s2);
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(4, result);

        s1 = "Banana";
        es1 = encoder.encode(s1);
        result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, result);

        s1 = "Banana";
        s2 = "Apple";
        es1 = encoder.encode(s1);
        es2 = encoder.encode(s2);
        result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, result);

        s1 = "apple";
        es1 = encoder.encode(s1);
        result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, result);

        s1 = "Apple";
        s2 = "Apple";
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        es1 = encoder.encode(s1);
        es2 = encoder.encode(s2);
        result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(4, result);

        s1 = "Apple";
        s2 = "Apfel";
        es1 = encoder.encode(s1);
        es2 = encoder.encode(s2);
        result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(3, result);
    }

    /**
     * Test the difference method with encoding exception.
     */
    @Test(expected = EncoderException.class)
    public void testDifferenceEncodingException() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Metaphone();
        String s1 = "Apple";
        String s2 = "Apple";
        String es1 = encoder.encode(s1);
        String es2 = encoder.encode(s2);
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.fail("The method should not be able to encode strings using Metaphone encoder");
    }
}