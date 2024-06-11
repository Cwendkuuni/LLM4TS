import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifferenceWithSimilarStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "John";
        String s2 = "Joan";
        int expected = 4;
        int actual = soundex.difference(s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDifferenceWithDifferentStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "John";
        String s2 = "Mike";
        int expected = 0;
        int actual = soundex.difference(s1, s2);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNonStringObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object obj = new Object();
        soundex.difference(obj, "John");
    }

    @Test
    public void testEncodeWithValidString() {
        Soundex soundex = new Soundex();
        String str = "John";
        String expected = "J500";
        String actual = soundex.encode(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodeWithEmptyString() {
        Soundex soundex = new Soundex();
        String str = "";
        String expected = "";
        String actual = soundex.encode(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodeWithWhiteSpaceString() {
        Soundex soundex = new Soundex();
        String str = "   ";
        String expected = "";
        String actual = soundex.encode(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSoundexWithValidString() {
        Soundex soundex = new Soundex();
        String str = "John";
        String expected = "J500";
        String actual = soundex.soundex(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSoundexWithEmptyString() {
        Soundex soundex = new Soundex();
        String str = "";
        String expected = "";
        String actual = soundex.soundex(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSoundexWithWhiteSpaceString() {
        Soundex soundex = new Soundex();
        String str = "   ";
        String expected = "";
        String actual = soundex.soundex(str);
        Assert.assertEquals(expected, actual);
    }

}