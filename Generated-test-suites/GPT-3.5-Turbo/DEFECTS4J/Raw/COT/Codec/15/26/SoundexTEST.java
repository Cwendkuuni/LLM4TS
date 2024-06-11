
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Apache");
        // The Soundex code for "Apache" should be "A120"
        Assert.assertEquals("A120", result);
    }

    @Test
    public void testEncodeNull() {
        Soundex soundex = new Soundex();
        String result = soundex.encode(null);
        // The Soundex code for null should be null
        Assert.assertNull(result);
    }

    @Test
    public void testEncodeEmptyString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("");
        // The Soundex code for an empty string should be an empty string
        Assert.assertEquals("", result);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("Apache", "Appache");
        // The difference between "Apache" and "Appache" should be 3
        Assert.assertEquals(3, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceInvalidString() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference(123, "Appache");
    }

}
