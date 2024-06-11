import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        Assert.assertEquals("1111111111", caverphone.caverphone(null));
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
        Assert.assertEquals("1111111111", caverphone.caverphone("     "));
        Assert.assertEquals("A3", caverphone.caverphone("a"));
        Assert.assertEquals("A3", caverphone.caverphone("A"));
        Assert.assertEquals("K3322333", caverphone.caverphone("coffee"));
        Assert.assertEquals("S2M1111111", caverphone.caverphone("apache"));
        Assert.assertEquals("KKT2F", caverphone.caverphone("knight"));
        Assert.assertEquals("A221111111", caverphone.caverphone("apple"));
        // Add more test cases for various inputs
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String encodedString = (String) caverphone.encode("test");
        Assert.assertEquals("3333111111", encodedString);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        String encodedString = caverphone.encode("example");
        Assert.assertEquals("3X1M111111", encodedString);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        Assert.assertTrue(caverphone.isCaverphoneEqual("apple", "apel"));
        Assert.assertFalse(caverphone.isCaverphoneEqual("coffee", "koffee"));
        Assert.assertTrue(caverphone.isCaverphoneEqual("example", "examples"));
        // Add more test cases for comparing caverphones of different strings
    }
}