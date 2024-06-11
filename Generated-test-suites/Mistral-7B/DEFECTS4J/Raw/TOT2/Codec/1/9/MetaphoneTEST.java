import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.language.Metaphone;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String txt = "Apple";
        String expected = "AA0P";
        String actual = metaphone.metaphone(txt);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DecoderException.class)
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        String txt = "";
        metaphone.metaphone(txt);
    }

    @Test(expected = DecoderException.class)
    public void testNullString() {
        Metaphone metaphone = new Metaphone();
        String txt = null;
        metaphone.metaphone(txt);
    }

    @Test
    public void testStringWithOnlyNumbers() {
        Metaphone metaphone = new Metaphone();
        String txt = "12345";
        String expected = "";
        String actual = metaphone.metaphone(txt);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSpecialCharacters() {
        Metaphone metaphone = new Metaphone();
        String txt = "Apple#";
        String expected = "AA0P";
        String actual = metaphone.metaphone(txt);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLongString() {
        Metaphone metaphone = new Metaphone();
        String txt = "The quick brown fox jumps over the lazy dog";
        String expected = "T255K2R2 J525P5 MP355 S255T";
        String actual = metaphone.metaphone(txt);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMultipleWords() {
        Metaphone metaphone = new Metaphone();
        String txt = "The quick brown fox jumps over the lazy dog";
        String expected = "T255K2R2 J525P5 MP355 S255T";
        String actual = metaphone.metaphone(txt);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMaxCodeLength() {
        Metaphone metaphone = new Metaphone();
        String txt = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        String expected = "T255K2R2 J525P5 MP355 S255T. T255K2R2 J525P5 MP355 S255T";
        String actual = metaphone.metaphone(txt);
        Assert.assertEquals(expected.substring(0, 10), actual.substring(0, 10));
    }

    @Test(expected = DecoderException.class)
    public void testInvalidInput() {
        Metaphone metaphone = new Metaphone();
        String txt = "AppleAppleApple";
        metaphone.metaphone(txt);
    }
}