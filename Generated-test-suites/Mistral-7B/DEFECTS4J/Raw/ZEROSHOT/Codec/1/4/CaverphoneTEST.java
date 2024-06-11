import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncoder() throws Exception {
        Caverphone encoder = new Caverphone();

        // Test empty string
        Assert.assertEquals("1111111111", encoder.encode(""));

        // Test single character
        Assert.assertEquals("A", encoder.encode("a"));
        Assert.assertEquals("2", encoder.encode("e"));

        // Test simple words
        Assert.assertEquals("2222223333", encoder.encode("the apple"));
        Assert.assertEquals("3333KF3333", encoder.encode("the quick brown fox"));

        // Test words with special characters
        Assert.assertEquals("2F2F2F3333", encoder.encode("the quick brown fox jumps over the lazy dog."));
        Assert.assertEquals("K2F2KFKF2FK", encoder.encode("The quick Brown fOx jumps OVer The LAZY Dog."));

        // Test words with numbers
        Assert.assertEquals("2222333333", encoder.encode("one two three four five"));
        Assert.assertEquals("3333333333", encoder.encode("five four three two one"));

        // Test words with multiple letters
        Assert.assertEquals("2T2T2T2T2T2T2T", encoder.encode("the quick brown fox jumps over the lazy dog jumped over the quick brown fox"));

        // Test words with multiple vowels
        Assert.assertEquals("A2A2A2A2A2A2A2A2", encoder.encode("the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog"));

        // Test words with multiple consonants
        Assert.assertEquals("2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T", encoder.encode("the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog"));

        // Test words with initial consonant clusters
        Assert.assertEquals("T2F2T2F2T2F2T2F2T2F2T2F2T2F2", encoder.encode("the quick brown fox jumped over the lazy dog jumped over the quick brown fox jumped over the lazy dog"));

        // Test words with final consonant clusters
        Assert.assertEquals("3333KF3333KF3333KF3333", encoder.encode("the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog jumps over the quick brown fox"));

        // Test words with initial vowel
        Assert.assertEquals("A3A3A3A3A3A3A3A3A3A3A3A3A3A3", encoder.encode("the apple is a fruit"));

        // Test words with final vowel
        Assert.assertEquals("2222223333A", encoder.encode("the quick brown fox jumps over the lazy dog."));

        // Test words with initial 'the'
        Assert.assertEquals("2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T", encoder.encode("the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog"));

        // Test words with leading and trailing spaces
        Assert.assertEquals("2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T", encoder.encode(" the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog "));

        // Test words with multiple spaces
        Assert.assertEquals("2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T", encoder.encode("the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog jumps over the quick brown fox jumps over the lazy dog"));

        // Test words with special characters in the middle
        Assert.assertEquals("2F2K2F2K2F2K2F2K2F2K2F2K2F2", encoder.encode("the quick brown fox jumps over the %#& lazy dog jumps over the quick brown fox jumps over the %#& lazy dog"));

        // Test words with digits in the middle
        Assert.assertEquals("2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T3333333", encoder.encode("the quick brown fox jumps over the 123 lazy dog jumps over the 456 quick brown fox jumps over the 789 lazy dog"));
    }

    @Test
    public void testCaverphoneDecoder() throws Exception {
        Caverphone decoder = new Caverphone();
        StringDecoder stringDecoder = new StringDecoder();

        String encoded = "2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T2T3333333";
        String decoded = decoder.decode(encoded);
        String decodedDecoded = stringDecoder.decode(decoded);

        Assert.assertEquals("the quick brown fox jumps over the 123 lazy dog jumps over the 456 quick brown fox jumps over the 789 lazy dog", decodedDecoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeException() {
        Caverphone encoder = new Caverphone();
        encoder.encode(new Object() {});
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        String str1 = "the quick brown fox jumps over the lazy dog";
        String str2 = "the quick brown fox jumps over the lazy dog";
        String str3 = "the quick brown fox jumps over the lazy cat";

        Assert.assertTrue(encoder.isCaverphoneEqual(str1, str1));
        Assert.assertTrue(encoder.isCaverphoneEqual(str1, str2));
        Assert.assertFalse(encoder.isCaverphoneEqual(str1, str3));
    }

}