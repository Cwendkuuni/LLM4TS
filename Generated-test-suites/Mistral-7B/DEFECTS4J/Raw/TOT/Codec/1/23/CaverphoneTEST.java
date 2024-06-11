import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodesEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String result = caverphone.encode("");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEncodesSingleLetterString() {
        Caverphone caverphone = new Caverphone();
        String expected = "A";
        String result = caverphone.encode("a");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneEncodesMultipleLettersString() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String expected = "H2L2O2";
        String result = caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneThrowsExceptionWhenStringIsNull() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }

    @Test
    public void testCaverphoneEncodesStringWithSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        String input = "hello@world!";
        String expected = "H2L2O2Fw3o2ld3R!";
        String result = caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneIsEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "world";
        String str3 = "hello";
        String str4 = "HELLO";

        String result1 = caverphone.encode(str1);
        String result2 = caverphone.encode(str2);
        String result3 = caverphone.encode(str3);

        Assert.assertFalse(caverphone.isCaverphoneEqual(str1, str2));
        Assert.assertTrue(caverphone.isCaverphoneEqual(str1, str3));
        Assert.assertFalse(caverphone.isCaverphoneEqual(str1, str4));
    }

    @Test
    public void testCaverphoneEncodesCaseInsensitive() {
        Caverphone caverphone = new Caverphone();
        String input = "HELLO WORLD";
        String expected = "H2L2O2Fw3o2ld3R";
        String result = caverphone.encode(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneHandlesLongStrings() {
        Caverphone caverphone = new Caverphone();
        String longInput = StringUtils.repeat("x", 100);
        String expected = StringUtils.repeat("F", 100);
        String result = caverphone.encode(longInput);
        assertEquals(expected, result);
    }

}