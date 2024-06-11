import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    private Soundex soundex = new Soundex();

    @Test
    public void testEncode_normalCase() {
        String encoded = soundex.encode("Apache");
        Assert.assertEquals("A120", encoded);
    }

    @Test
    public void testEncode_emptyString() {
        String encoded = soundex.encode("");
        Assert.assertEquals("", encoded);
    }

    @Test
    public void testEncode_lowerCase() {
        String encoded = soundex.encode("apache");
        Assert.assertEquals("A120", encoded);
    }

    @Test
    public void testEncode_singleCharacter() {
        String encoded = soundex.encode("A");
        Assert.assertEquals("A000", encoded);
    }

    @Test
    public void testEncode_multipleWhitespace() {
        String encoded = soundex.encode("A   pache");
        Assert.assertEquals("A120", encoded);
    }

    @Test
    public void testEncode_specialCharacters() {
        String encoded = soundex.encode("@!$#");
        Assert.assertEquals("", encoded);
    }

    @Test
    public void testEncode_numbers() {
        String encoded = soundex.encode("1234");
        Assert.assertEquals("", encoded);
    }

    @Test
    public void testEncode_hyphen() {
        String encoded = soundex.encode("A-pache");
        Assert.assertEquals("A120", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_nonString() {
        soundex.encode(123);
    }

    @Test
    public void testDifference_sameStrings() {
        int difference = soundex.difference("Apache", "Apache");
        Assert.assertEquals(4, difference);
    }

    @Test
    public void testDifference_differentStrings() {
        int difference = soundex.difference("Apache", "Epwke");
        Assert.assertEquals(0, difference);
    }

    @Test
    public void testDifference_emptyStrings() {
        int difference = soundex.difference("", "");
        Assert.assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_nonString() {
        soundex.difference(123, "Apache");
    }
}