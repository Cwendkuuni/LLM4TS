import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone_emptyString_return1111111111() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercaseString_convertToLowerCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("TEST");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCaverphone_nonAlphabetCharacters_removeNonAlphabetCharacters() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("t3@4!s#t");
        Assert.assertEquals("tst", result);
    }

    // Additional test cases for other methods in the Caverphone class

    @Test
    public void testEncode_objectNotString_throwEncoderException() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(1234);
            Assert.fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_validString_returnEncodedCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("test");
        Assert.assertEquals("tst", result);
    }

    @Test
    public void testIsCaverphoneEqual_identicalStrings_returnTrue() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings_returnFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("test", "example");
        Assert.assertFalse(result);
    }

}