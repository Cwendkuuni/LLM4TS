
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();
    
    @Test
    public void testCaverphone() {
        String result = caverphone.caverphone("test");
        Assert.assertEquals("1111111111", result);
    }
    
    @Test
    public void testCaverphoneWithEmptyString() {
        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
    }
    
    @Test
    public void testCaverphoneWithNullString() {
        String result = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", result);
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Object result = caverphone.encode("test");
        Assert.assertEquals("1111111111", result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidObject() throws EncoderException {
        Object result = caverphone.encode(123);
    }
    
    @Test
    public void testEncodeString() {
        String result = caverphone.encode("test");
        Assert.assertEquals("1111111111", result);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("test", "TEST");
        Assert.assertEquals(true, result);
    }

}
