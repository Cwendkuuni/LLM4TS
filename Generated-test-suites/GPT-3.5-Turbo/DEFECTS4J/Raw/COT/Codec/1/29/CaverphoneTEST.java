
import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

    @Test
    public void testCaverphone_basicFunctionality() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("example");
        Assert.assertEquals("AXMPL1111", result);
    }

    @Test
    public void testEncode_basicFunctionality() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("example");
        Assert.assertEquals("AXMPL1111", result);
    }

    @Test
    public void testEncode_exceptionHandling() {
        Caverphone caverphone = new Caverphone();
        try {
            Object result = caverphone.encode(123);
            Assert.fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testIsCaverphoneEqual_identicalStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example1", "example2");
        Assert.assertFalse(result);
    }

    @Test
    public void testCaverphone_edgeCases() {
        Caverphone caverphone = new Caverphone();
        String result1 = caverphone.caverphone("");
        String result2 = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", result1);
        Assert.assertEquals("1111111111", result2);
    }

    @Test
    public void testCaverphone_specificCases() {
        Caverphone caverphone = new Caverphone();
        String result1 = caverphone.caverphone("enough");
        String result2 = caverphone.caverphone("trough");
        Assert.assertEquals("3NU22", result1);
        Assert.assertEquals("TRU2F", result2);
    }

    @Test
    public void testEncode_specificCases() {
        Caverphone caverphone = new Caverphone();
        String result1 = caverphone.encode("enough");
        String result2 = caverphone.encode("trough");
        Assert.assertEquals("3NU22", result1);
        Assert.assertEquals("TRU2F", result2);
    }
}

