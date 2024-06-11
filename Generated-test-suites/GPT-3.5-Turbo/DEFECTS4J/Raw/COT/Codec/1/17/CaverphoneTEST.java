import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {
    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone_emptyString_returns1111111111() {
        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_lowercaseString_returnsCaverphoneCode() {
        String result = caverphone.caverphone("test");
        Assert.assertEquals("A11111", result);
    }

    @Test
    public void testCaverphone_nonAlphabeticCharacters_removedFromInput() {
        String result = caverphone.caverphone("123test!@#");
        Assert.assertEquals("A11111", result);
    }

    @Test
    public void testCaverphone_startOptionsHandled_correctReplacements() {
        String result = caverphone.caverphone("cough");
        Assert.assertEquals("cou2fA111", result);
        
        result = caverphone.caverphone("enough");
        Assert.assertEquals("enou2fA111", result);
    }

    @Test
    public void testCaverphone_replacementsHandled_correctReplacements() {
        String result = caverphone.caverphone("queens");
        Assert.assertEquals("k33nsA111", result);
        
        result = caverphone.caverphone("tough");
        Assert.assertEquals("tou2fA111", result);
    }

    @Test
    public void testCaverphone_removalsHandled_correctRemovals() {
        String result = caverphone.caverphone("knock");
        Assert.assertEquals("k3nA111", result);
        
        result = caverphone.caverphone("wash");
        Assert.assertEquals("w2shA111", result);
    }

    @Test
    public void testEncode_objectNotString_throwsEncoderException() {
        try {
            caverphone.encode(new Integer(123));
            // If the above line does not throw an exception, fail the test
            Assert.fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_validString_returnsCaverphoneCode() {
        String result = caverphone.encode("test");
        Assert.assertEquals("A11111", result);
    }

    @Test
    public void testIsCaverphoneEqual_identicalStrings_returnsTrue() {
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings_returnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        Assert.assertFalse(result);
    }

}