import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("1111111111", caverphone.caverphone(null));
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        Assert.assertEquals("caverphone", caverphone.caverphone("CAVERPHONE"));
        // Add more basic test cases here
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectException() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123); // Invalid object type, should throw EncoderException
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("caverphone", caverphone.encode("CAVERPHONE"));
        // Add more test cases here
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("caverphone", caverphone.encode("CAVERPHONE"));
        // Add more test cases here
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        Assert.assertTrue(caverphone.isCaverphoneEqual("caverphone", "CAVERPHONE"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("caverphone", "CAVER"));
        // Add more test cases here
    }
}