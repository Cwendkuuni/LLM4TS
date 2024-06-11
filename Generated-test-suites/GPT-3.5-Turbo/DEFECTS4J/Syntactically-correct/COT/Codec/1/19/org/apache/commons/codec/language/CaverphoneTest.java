import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        String result = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", result);
        
        result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
        
        result = caverphone.caverphone("Test String");
        Assert.assertEquals("2STN", result);
        
        result = caverphone.caverphone("Another Test");
        Assert.assertEquals("AN0THAST", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_TypeMismatch() throws EncoderException {
        caverphone.encode(123);
    }

    @Test
    public void testEncode() {
        String result = caverphone.encode(null);
        Assert.assertEquals("1111111111", result);

        result = caverphone.encode("");
        Assert.assertEquals("1111111111", result);

        result = caverphone.encode("Test String");
        Assert.assertEquals("2STN", result);

        result = caverphone.encode("Another Test");
        Assert.assertEquals("AN0THAST", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("", "");
        Assert.assertTrue(result);

        result = caverphone.isCaverphoneEqual("Test String", "Test String");
        Assert.assertTrue(result);

        result = caverphone.isCaverphoneEqual("Different Strings", "Different Strings");
        Assert.assertTrue(result);

        result = caverphone.isCaverphoneEqual("Test String", "Test Strin");
        Assert.assertFalse(result);

        result = caverphone.isCaverphoneEqual("Different Strings", "Different Strin");
        Assert.assertFalse(result);
    }
}