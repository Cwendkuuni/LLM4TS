import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case
        String input = "hello";
        String expected = "3133133133";
        String actual = caverphone.caverphone(input);
        Assert.assertEquals(expected, actual);
        
        // Edge case: empty input
        input = "";
        expected = "1111111111";
        actual = caverphone.caverphone(input);
        Assert.assertEquals(expected, actual);

        // Edge case: only non-alphabetic characters
        input = "123.!@#";
        expected = "1111111111";
        actual = caverphone.caverphone(input);
        Assert.assertEquals(expected, actual);
        
        // Edge case: input with special characters
        input = "h@llo";
        expected = "3133133133";
        actual = caverphone.caverphone(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        // Test encoding with non-string object
        Object input = new Object();
        caverphone.encode(input);
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case
        String input = "hello";
        String expected = "3133133133";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
        
        // Edge case: empty input
        input = "";
        expected = "1111111111";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);

        // Edge case: only non-alphabetic characters
        input = "123.!@#";
        expected = "1111111111";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
        
        // Edge case: input with special characters
        input = "h@llo";
        expected = "3133133133";
        actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case: equal caverphones
        String str1 = "hello";
        String str2 = "hullu";
        Assert.assertTrue(caverphone.isCaverphoneEqual(str1, str2));
        
        // Basic test case: different caverphones
        str1 = "hello";
        str2 = "world";
        Assert.assertFalse(caverphone.isCaverphoneEqual(str1, str2));
        
        // Edge case: empty input
        str1 = "";
        str2 = "";
        Assert.assertTrue(caverphone.isCaverphoneEqual(str1, str2));

        // Edge case: input with special characters
        str1 = "h@llo";
        str2 = "hullu";
        Assert.assertTrue(caverphone.isCaverphoneEqual(str1, str2));
        
        // Edge case: input with different case
        str1 = "HELLO";
        str2 = "hello";
        Assert.assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }
}